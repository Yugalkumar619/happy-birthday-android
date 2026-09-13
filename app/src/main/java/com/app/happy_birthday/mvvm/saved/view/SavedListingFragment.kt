package com.app.happy_birthday.mvvm.saved.view

import AppNavigation.navigateToSavedImageViewer
import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.FragmentSavedListingBinding
import com.app.happy_birthday.helper.BaseFragment
import com.app.happy_birthday.helper.Constants
import com.app.happy_birthday.helper.Extensions.deleteFile
import com.app.happy_birthday.helper.Extensions.getAllImagesFromRamWallpaperFolder
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.home.view.HomeActivity
import com.app.happy_birthday.mvvm.saved.view_model.SavedViewModel
import com.app.happy_birthday.mvvm.wallpaper.model.WallpaperDataModel
import com.app.happy_birthday.mvvm.wallpaper.view.ImageViewerObj


class SavedListingFragment : BaseFragment() {

    private lateinit var mActivity: HomeActivity
    private lateinit var binding: FragmentSavedListingBinding
    private lateinit var viewModel: SavedViewModel
    private var wallpaperImage: Uri? = null
    private lateinit var wallpaperManager: WallpaperManager




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as HomeActivity
        viewModel = ViewModelProvider(this)[SavedViewModel::class.java]
        wallpaperManager = WallpaperManager.getInstance(mActivity)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedListingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()
        initOnClickListener()

    }


    private fun initializeFields(){

        binding.root.post {
            AdsManager.loadBanner(requireActivity(), R.id.banner_container)
        }

        binding.ilToolbar.root.backgroundTintList = ContextCompat.getColorStateList(mActivity,R.color.color_green)

        setUpToolbar(binding.ilToolbar,
            title = getString(R.string.label_saved),
            isBackArrow = true,
            toolbarClickListener = object : CommonInterfaceClickEvent {
                override fun onToolBarListener(type: String) {
                    if (type == Constants.TOOLBAR_ICON_ONE){

                    }
                    if (type == Constants.TOOLBAR_ICON_TWO){

                    }
                }
            }
        )

        binding.layoutEmpty.ivEmptyIcon.setImageResource(R.drawable.ic_file_location)
        binding.layoutEmpty.ivEmptyIcon.isVisible = true
        binding.layoutEmpty.txtEmptyMessage.text = getString(R.string.label_no_wishes_saved_yet)
        binding.layoutEmpty.txtEmptySubMessage.text = getString(R.string.label_start_saving_birthday)




    }

    override fun onResume() {
        super.onResume()

        binding.rvWallpaper.adapter = viewModel.adapterPhotos
        viewModel.adapterPhotos
        viewModel.arrListPhotosData.clear()
        var data = getAllImagesFromRamWallpaperFolder(mActivity)
        println("Wooooo my images:::: ${data.toString()}")
        data.forEach {
            viewModel.arrListPhotosData.add(WallpaperDataModel(imageUri = it))
        }
//        viewModel.arrListPhotosData.addAll(mActivity.getWallpaperImages())
        viewModel.adapterPhotos.onClickEvent = onItemClickListener
        viewModel.updateSaveGridAdapter()
        binding.layoutEmpty.root.isVisible = viewModel.arrListPhotosData.isEmpty()
    }

    private val onItemClickListener = object :  CommonInterfaceClickEvent{
        override fun onItemClick(type: String, position: Int) {
//            navigateToImageDetails(ImageDetailsObj(viewModel.arrListPhotosData,position))
            wallpaperImage = viewModel.arrListPhotosData.get(position)?.imageUri
            val images =  arrayListOf<WallpaperDataModel?>()

            when(type){
                "delete" -> {
                    if (deleteFile(mActivity, wallpaperImage)) {
                        Toast.makeText(mActivity, "Deleted!", Toast.LENGTH_SHORT).show()
                        viewModel.arrListPhotosData.clear()
                        var data = getAllImagesFromRamWallpaperFolder(mActivity)
                        data.forEach {
                            viewModel.arrListPhotosData.add(WallpaperDataModel(imageUri = it))
                        }
                        viewModel.updateSaveGridAdapter()
                        binding.layoutEmpty.root.isVisible = viewModel.arrListPhotosData.isEmpty()
                    }
                }
                "set" -> {
                    showWallpaperOptionsDialog()
                }
                else -> {
                    viewModel.arrListPhotosData.forEach {
                        images.add(
                            WallpaperDataModel(
                                image  = it?.image,
                            )
                        )
                    }
                    mActivity.navigateToSavedImageViewer(
                        ImageViewerObj(
                            images = arrayListOf(),
                            currentPosition = position
                        )
                    )
                }
            }

        }
    }

    private fun showWallpaperOptionsDialog() {
        val options = arrayOf(getString(R.string.label_home_screen),
            getString(R.string.label_lock_screen), getString(R.string.label_both_screens))

        AlertDialog.Builder(mActivity)
            .setTitle(getString(R.string.label_set_as_wallpaper))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> setWallpaper(WallpaperManager.FLAG_SYSTEM)   // Home only
                    1 -> setWallpaper(WallpaperManager.FLAG_LOCK)     // Lock only
                    2 -> setWallpaper(WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK) // Both
                }
            }
            .setNegativeButton(getString(R.string.label_cancel), null)
            .show()
    }
    private fun setWallpaper(flag: Int) {
        mActivity.showProgressDialog("F")
        Thread {
            try {
//                val wallpaperBitmap = BitmapFactory.decodeResource(resources, wallpaperImage ?: 0)
                wallpaperImage?.let { uri ->
                    val wallpaperBitmap =
                        mActivity.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager.setBitmap(wallpaperBitmap, null, true, flag)
                    } else {
                        wallpaperManager.setBitmap(wallpaperBitmap)
                    }

                    mActivity.runOnUiThread {
                        mActivity.hideProgressDialog()
                        Toast.makeText(
                            mActivity,
                            getString(R.string.label_jai_shri_ram_wallpaper_set_successfully),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                mActivity.runOnUiThread {
                    mActivity.hideProgressDialog()
                    Toast.makeText(
                        mActivity,
                        getString(R.string.label_failed, e.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
    }
    private fun initOnClickListener(){

    }

}