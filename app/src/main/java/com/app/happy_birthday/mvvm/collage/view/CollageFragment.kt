package com.app.happy_birthday.mvvm.collage.view

import AdsManager
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.FragmentCollageBinding
import com.app.happy_birthday.helper.BaseFragment
import com.app.happy_birthday.mvvm.collage.adapter.SelectedPhotoAdapter
import com.app.happy_birthday.mvvm.collage.adapter.TemplateAdapter
import com.app.happy_birthday.mvvm.collage.model.CollageLayout
import com.app.happy_birthday.mvvm.collage.model.CollageTemplates
import com.app.happy_birthday.mvvm.collage.view_model.CollageSaveState
import com.app.happy_birthday.mvvm.collage.view_model.CollageViewModel
import com.app.happy_birthday.mvvm.home.view.HomeActivity
import com.google.android.material.tabs.TabLayout

class CollageFragment : BaseFragment() {

    private lateinit var mActivity: HomeActivity
    private lateinit var binding: FragmentCollageBinding
    private lateinit var viewModel: CollageViewModel

    private val photoAdapter = SelectedPhotoAdapter { uri -> viewModel.removePhoto(uri) }
    private val templateAdapter = TemplateAdapter(CollageTemplates.all) { template ->
        viewModel.selectTemplate(template)
    }

    private val pickPhotos = registerForActivityResult(
        PickMultipleVisualMedia(CollageLayout.MAX_PHOTOS)
    ) { uris ->
        if (uris.isNotEmpty() && viewModel.addPhotos(uris)) {
            showPhotoLimitMessage()
        }
    }

    private val requestStoragePermission = registerForActivityResult(
        RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.save()
        } else {
            showMessage(getString(R.string.label_collage_storage_permission))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as HomeActivity
        viewModel = ViewModelProvider(this)[CollageViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initialize(
            title = getString(R.string.label_collage_title),
            defaultWish = getString(R.string.label_collage_default_wish)
        )

        initializeFields()
        initOnClickListener()
        observeViewModel()

        // Open the picker only on first entry, not when the screen is restored.
        if (savedInstanceState == null && !viewModel.hasOpenedPicker) {
            viewModel.hasOpenedPicker = true
            openPhotoPicker()
        }
    }

    private fun initializeFields() {
        binding.root.post {
            AdsManager.loadBanner(requireActivity(), R.id.banner_container)
        }

        binding.ilToolbar.root.backgroundTintList =
            ContextCompat.getColorStateList(mActivity, R.color.color_pink)

        setUpToolbar(
            binding.ilToolbar,
            title = getString(R.string.label_collage_wishes),
            isBackArrow = true
        )

        binding.rvSelectedPhotos.adapter = photoAdapter
        binding.rvTemplates.adapter = templateAdapter

        CollagePreviewGestures(binding.ivPreview, viewModel).attach()

        setUpTabs()
    }

    private fun setUpTabs() {
        listOf(
            R.string.label_collage_tab_photos,
            R.string.label_collage_tab_name,
            R.string.label_collage_tab_wish,
            R.string.label_collage_tab_template
        ).forEach { title ->
            binding.tabsCollage.addTab(binding.tabsCollage.newTab().setText(title))
        }

        binding.tabsCollage.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) = showTab(tab.position)
            override fun onTabUnselected(tab: TabLayout.Tab) = Unit
            override fun onTabReselected(tab: TabLayout.Tab) = Unit
        })
        showTab(binding.tabsCollage.selectedTabPosition)
    }

    private fun showTab(position: Int) {
        binding.linPhotosTab.isVisible = position == TAB_PHOTOS
        binding.linNameTab.isVisible = position == TAB_NAME
        binding.linWishTab.isVisible = position == TAB_WISH
        binding.rvTemplates.isVisible = position == TAB_TEMPLATE
    }

    private fun initOnClickListener() {
        binding.ilToolbar.linBackArrow.setOnClickListener { leaveScreen() }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = leaveScreen()
            }
        )

        binding.btnAddPhotos.setOnClickListener {
            if (viewModel.isPhotoLimitReached()) {
                showPhotoLimitMessage()
            } else {
                openPhotoPicker()
            }
        }

        binding.edtName.doAfterTextChanged { text ->
            val name = text?.toString().orEmpty().trim()
            viewModel.setTitle(
                if (name.isEmpty()) {
                    getString(R.string.label_collage_title)
                } else {
                    getString(R.string.label_collage_title_with_name, name)
                }
            )
        }

        binding.edtName.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.clearFocus()
                WindowCompat.getInsetsController(mActivity.window, view)
                    .hide(WindowInsetsCompat.Type.ime())
                true
            } else {
                false
            }
        }

        binding.btnChooseWish.setOnClickListener { openWishPicker() }
        binding.txtSelectedWish.setOnClickListener { openWishPicker() }
        binding.btnSave.setOnClickListener { onSaveClicked() }
        binding.btnShare.setOnClickListener { shareSavedCollage() }

        binding.btnCreateAnother.setOnClickListener {
            viewModel.startNewCollage()
            binding.edtName.text?.clear()
            binding.tabsCollage.getTabAt(TAB_PHOTOS)?.select()
            openPhotoPicker()
        }
    }

    private fun observeViewModel() {
        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            photoAdapter.submitList(photos)
            templateAdapter.setPhotoCount(photos.size)
            binding.tabsCollage.getTabAt(TAB_PHOTOS)?.text = getString(
                R.string.label_collage_photos_count,
                photos.size,
                CollageLayout.MAX_PHOTOS
            )
            binding.rvSelectedPhotos.isVisible = photos.isNotEmpty()
            binding.txtNoPhotos.isVisible = photos.isEmpty()
            binding.txtAdjustHint.isVisible = photos.isNotEmpty()
            binding.btnSave.isEnabled = photos.isNotEmpty()
            binding.btnSave.alpha = if (photos.isNotEmpty()) 1f else DISABLED_ALPHA
        }

        viewModel.template.observe(viewLifecycleOwner) { template ->
            templateAdapter.setSelected(template)
        }

        viewModel.wish.observe(viewLifecycleOwner) { wish ->
            binding.txtSelectedWish.text = wish
        }

        viewModel.preview.observe(viewLifecycleOwner) { preview ->
            binding.ivPreview.setImageBitmap(preview)
        }

        viewModel.photosRemoved.observe(viewLifecycleOwner) { removed ->
            if (removed) {
                showMessage(getString(R.string.label_collage_photo_load_failed))
                viewModel.onPhotosRemovedShown()
            }
        }

        viewModel.saveState.observe(viewLifecycleOwner) { state -> renderSaveState(state) }
    }

    private fun renderSaveState(state: CollageSaveState) {
        binding.linEditor.isVisible = state !is CollageSaveState.Saved
        binding.linResult.isVisible = state is CollageSaveState.Saved

        when (state) {
            CollageSaveState.Editing,
            is CollageSaveState.Saved -> mActivity.hideProgressDialog()

            CollageSaveState.Saving -> mActivity.showProgressDialog("F")

            is CollageSaveState.ReadyForAd -> {
                mActivity.hideProgressDialog()
                viewModel.onAdStarted()
                // This also continues straight away for premium users or when no ad is ready.
                AdsManager.showInterstitial(mActivity) { viewModel.onAdFinished(state.uri) }
            }

            CollageSaveState.ShowingAd -> Unit

            CollageSaveState.Failed -> {
                mActivity.hideProgressDialog()
                showMessage(getString(R.string.label_collage_save_failed))
                viewModel.onFailureShown()
            }
        }
    }

    private fun onSaveClicked() {
        val needsPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED

        if (needsPermission) {
            requestStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            viewModel.save()
        }
    }

    private fun openPhotoPicker() {
        pickPhotos.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    private fun openWishPicker() {
        if (childFragmentManager.findFragmentByTag(WishPickerBottomSheet.TAG) == null) {
            WishPickerBottomSheet().show(childFragmentManager, WishPickerBottomSheet.TAG)
        }
    }

    private fun shareSavedCollage() {
        val state = viewModel.saveState.value as? CollageSaveState.Saved ?: return
        share(state.uri)
    }

    private fun share(uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(
                Intent.EXTRA_TEXT,
                getString(
                    R.string.label_check_out_app,
                    getString(R.string.app_name),
                    mActivity.packageName
                )
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.label_share_image)))
    }

    private fun leaveScreen() {
        if (!viewModel.hasUnsavedCollage()) {
            findNavController().navigateUp()
            return
        }

        AlertDialog.Builder(mActivity)
            .setTitle(R.string.label_collage_discard_title)
            .setMessage(R.string.label_collage_discard_message)
            .setPositiveButton(R.string.label_collage_discard) { _, _ ->
                findNavController().navigateUp()
            }
            .setNegativeButton(R.string.label_cancel, null)
            .show()
    }

    private fun showPhotoLimitMessage() {
        showMessage(getString(R.string.label_collage_photo_limit, CollageLayout.MAX_PHOTOS))
    }

    private fun showMessage(message: String) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show()
    }

    private companion object {
        const val TAB_PHOTOS = 0
        const val TAB_NAME = 1
        const val TAB_WISH = 2
        const val TAB_TEMPLATE = 3
        const val DISABLED_ALPHA = 0.5f
    }
}
