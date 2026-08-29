package com.app.happy_birthday.mvvm.wallpaper.view

import AdsManager
import android.app.Dialog
import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.dispose
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.ActivityImagaViewBinding
import com.app.happy_birthday.databinding.LvItemPagerBinding
import com.app.happy_birthday.helper.BaseActivity
import com.app.happy_birthday.helper.Extensions
import com.app.happy_birthday.helper.Extensions.downsampleBitmap
import com.app.happy_birthday.helper.Global.imageTitle
import com.app.happy_birthday.helper.Global.loadPhotoUsingCoil
import com.app.happy_birthday.helper.UtilExtension
import com.app.happy_birthday.mvvm.edit_image.view.EditImageActivity
import com.app.happy_birthday.mvvm.wallpaper.model.WallpaperDataModel
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.Serializable

data class ImageViewerObj(
    var images: ArrayList<WallpaperDataModel?>? = null,
    var currentPosition: Int? = null
) : Serializable

class ImageViewerActivity : BaseActivity() {

    /** Define variables */
    lateinit var binding: ActivityImagaViewBinding
    private var editedBitmaps = mutableMapOf<Int, Bitmap>()

    private var imageObj: ImageViewerObj? = null
    private var thumbnail: Int? = null
    private var wallpaperImage: Int? = null
    private var currentPage: Int = 0
    private var height: Int = 0
    private lateinit var wallpaperManager: WallpaperManager

    private lateinit var cResolver: ContentResolver
    var shareBtnClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagaViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cResolver = this.contentResolver
        wallpaperManager = WallpaperManager.getInstance(this)

        initializedFields()
        initializeClickListener()
    }

    override fun getTopInsetView(): View? {
        return binding.clMain
    }

    /** Initialize Section */
    private fun initializedFields() {
        MobileAds.initialize(this) {  // Callback for init complete
            Log.d("AdMob", "Initialized")
        }

        /** Set Full Screen */
        setFullScreen()

        /** Set Display */
        setDisplay()

        /** Set Data */
        if(intent.getSerializableExtra("ImageViewerObj") != null)  {
            imageObj = ImageViewerObj()
            imageObj = intent.getSerializableExtra("ImageViewerObj") as ImageViewerObj
            wallpaperImage = imageObj?.images?.get(imageObj?.currentPosition ?: 0)?.image
            setPager()
        }

        /** Set Back Press */
        setBackPress()
    }
    private fun initializeClickListener() {

        binding.fabEdit.setOnClickListener {
            startEditActivity()
            binding.fabMenu.collapse()
        }

        binding.ivSave.setOnClickListener {
            saveImage()
        }


        binding.fabDownload.setOnClickListener {
            saveImage()
        }

        binding.fabShare.setOnClickListener {
            shareBtnClicked = true
            share(cResolver)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.fabWallpaper.setOnClickListener {
            showWallpaperOptionsDialog()
            binding.fabMenu.collapse()
        }

        binding.fabDetails.setOnClickListener {
            showWallpaperDetailsDialog(wallpaperImage ?: 0)
        }

    }


    /** Setup Section */
    private fun setFullScreen() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
    private fun setDisplay(){
        val metrics = resources.displayMetrics
        height = metrics.heightPixels

        val param = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, height
        )
        binding.clMain.layoutParams = param
        binding.pager.layoutParams = param
    }
    private fun setBackPress(){

        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                // Back is pressed... Finishing the activity
                binding.ivBack.performClick()
            }
        } else {
            onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        // Back is pressed... Finishing the activity
                        binding.ivBack.performClick()
                    }
                })
        }
    }


    /** Common Function Section */
//    fun saveImage(){
//        wallpaperImage.let { urlData ->
//
//            showProgressDialog("F")
//
//            /** Saving the image using coroutine to a specific folder */
//            CoroutineScope(Dispatchers.IO).launch {
//                val status = UtilExtension.saveImageToGallery(
//                    this@ImageViewerActivity,
//                    "${imageTitle}_$currentPage",
//                    UtilExtension.drawableToByteArray(
//                        this@ImageViewerActivity,
//                        urlData!!
//                    )
//                )
//
//                /** checking the status and displaying the message*/
//                withContext(Dispatchers.Main) {
//                    hideProgressDialog()
//                    if (status == "ok") {
//                        Toast.makeText(
//                            this@ImageViewerActivity,
//                            "Image saved successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        Toast.makeText(
//                            this@ImageViewerActivity,
//                            "Please enable storage permission",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    /** After saving displaying the Ad while going back*/
//
//                    AdsManager.showInterstitial(this@ImageViewerActivity,{})
//                    binding.ivBack.performClick()
//
//                }
//            }
//
//        }
//    }

    fun saveImage() {
        val currentBitmap = editedBitmaps[currentPage]
            ?: BitmapFactory.decodeResource(resources, wallpaperImage!!)

        showProgressDialog("F")

        CoroutineScope(Dispatchers.IO).launch {
            val status = UtilExtension.saveImageToGallery(
                this@ImageViewerActivity,
                "${imageTitle}_$currentPage",
                UtilExtension.bitmapToByteArray(currentBitmap) // Pass bitmap, not drawable
            )

            withContext(Dispatchers.Main) {
                hideProgressDialog()
                if (status == "ok") {
                    Toast.makeText(this@ImageViewerActivity, "Image saved successfully", Toast.LENGTH_SHORT).show()
                    Extensions.BitmapCache.clear()
                } else {
                    Toast.makeText(this@ImageViewerActivity, "Storage permission needed. Go to Permissions > Allow Storage", Toast.LENGTH_LONG).show()

                    // Open app settings so user can grant permission
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this@ImageViewerActivity, "Cannot open settings", Toast.LENGTH_SHORT).show()
                    }                }

                AdsManager.showInterstitial(this@ImageViewerActivity) {}
                binding.ivBack.performClick()
            }
        }
    }

    fun share(contentResolver: ContentResolver) {
        val options = BitmapFactory.Options().apply {
            inScaled = false
        }
        val bitPhoto = BitmapFactory.decodeResource(resources, wallpaperImage!!.toInt(), options)
        val icon: Bitmap = bitPhoto
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "title")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri: Uri? = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )


        val outstream: OutputStream
        try {
            outstream = contentResolver.openOutputStream(uri!!)!!
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
            outstream.close()
        } catch (e: Exception) {
            System.err.println(e.toString())
        }

        share.putExtra(Intent.EXTRA_STREAM, uri)
        share.putExtra(
            Intent.EXTRA_TEXT,
            getString(
                R.string.label_check_out_app,
                getString(R.string.app_name),
                packageName
            )

        )
        startActivity(Intent.createChooser(share, getString(R.string.label_share_image)))
    }
    private fun showWallpaperOptionsDialog() {
        val options = arrayOf(getString(R.string.label_home_screen),
            getString(R.string.label_lock_screen), getString(R.string.label_both_screens))

        AlertDialog.Builder(this)
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
        showProgressDialog("F")
        Thread {
            try {
                val wallpaperBitmap = BitmapFactory.decodeResource(resources, wallpaperImage ?: 0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(wallpaperBitmap, null, true, flag)
                } else {
                    wallpaperManager.setBitmap(wallpaperBitmap)
                }

                runOnUiThread {
                    hideProgressDialog()
                    Toast.makeText(
                        this,
                        getString(R.string.label_jai_shri_ram_wallpaper_set_successfully),
                        Toast.LENGTH_LONG
                    ).show()
                    AdsManager.showInterstitial(this@ImageViewerActivity,{})
                }
            } catch (e: Exception) {
                runOnUiThread {
                    hideProgressDialog()
                    Toast.makeText(this,
                        getString(R.string.label_failed, e.message),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
    private fun showWallpaperDetailsDialog(drawableId: Int) {
        // Decode bitmap to get real width/height and calculate size
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeResource(resources, drawableId, options)

        val width = options.outWidth
        val height = options.outHeight
        val resolution = "$width × $height px"

        // Calculate approximate size in MB (drawable ≈ actual file size in most cases)
        val bitmap = BitmapFactory.decodeResource(resources, drawableId)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 97, baos)
        val realSizeMB = String.format("%.2f MB", baos.size() / (1024.0 * 1024.0))
        bitmap.recycle()

        // Create custom dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_wallpaper_details) // ← create this layout (see below)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Bind views
        dialog.findViewById<TextView>(R.id.tvResolution).text = resolution
        dialog.findViewById<TextView>(R.id.tvSize).text = realSizeMB

        // Close button (×)
        dialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { dialog.dismiss() }

        dialog.show()
    }


    /** View Pager Section */
    inner class DetailOnPageChangeListener : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.ivSave.isVisible = false
            binding.fabMenu.collapse()
            currentPage = position
            wallpaperImage = imageObj?.images?.get(position)?.image
        }
    }
    private fun setPager() {
        binding.pager.adapter =
            imageObj?.images?.let { ImagePagerAdapter((it ?: arrayListOf()) as ArrayList<String>) }
        val listener = DetailOnPageChangeListener()
        binding.pager.registerOnPageChangeCallback(listener)
        binding.pager.currentItem = imageObj?.currentPosition?.toInt() ?: 0
    }
    private inner class ImagePagerAdapter(private val images: ArrayList<String>) :
        RecyclerView.Adapter<ImagePagerAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val binding: LvItemPagerBinding = LvItemPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MyViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            val small_param = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height
            )
            holder.binding.snoop.layoutParams = small_param
            holder.binding.relBanner.layoutParams = small_param

            // === CRITICAL: Always check for edited bitmap FIRST ===
            val editedBitmap = editedBitmaps[position]

            if (editedBitmap != null && !editedBitmap.isRecycled) {
                // Cancel any ongoing Coil load to prevent overwrite
                holder.binding.snoop.dispose()
                // This cancels Coil request if running

                // Directly set the edited bitmap
                holder.binding.snoop.setImageBitmap(editedBitmap)

                // Hide spinner immediately
                holder.binding.layoutSpinner.root.visibility = View.GONE
            } else {
                // Only load original if NO edit exists
                val drawableId = imageObj?.images?.get(position)?.image
                if (drawableId != null) {
                    holder.binding.snoop.loadPhotoUsingCoil(
                        drawableId,
                        R.drawable.ic_hindu_flag,
                        holder.binding.layoutSpinner
                    )
                } else {
                    holder.binding.snoop.setImageResource(R.drawable.ic_hindu_flag)
                    holder.binding.layoutSpinner.root.visibility = View.GONE
                }
            }

//            if (imageObj?.images?.get(position)?.image != null) {
//                thumbnail = imageObj?.images?.get(position)?.image
//                holder.binding.snoop.loadPhotoUsingCoil(
//                    thumbnail,
//                    R.drawable.ic_hindu_flag,
//                    holder.binding.layoutSpinner
//                )
//
//            }

            holder.binding.snoop.setOnClickListener {
                binding.fabMenu.collapse()
            }

        }

        override fun getItemCount(): Int {
            return images.size
        }

        inner class MyViewHolder(var binding: LvItemPagerBinding) :
            RecyclerView.ViewHolder(binding.root)

    }

    private fun startEditActivity() {
        val currentDrawableId = wallpaperImage ?: return

        // Decode the original bitmap (full size, but only once)
        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val original = BitmapFactory.decodeResource(resources, currentDrawableId, options)
            ?: return

        // Downsample to safe size (1080p max) to prevent OOM/ANR
        val downsampled = downsampleBitmap(original)

        // Store the downsampled version in cache for editing
        Extensions.BitmapCache.setBitmap(downsampled)

        // Safely recycle the full-size original — we don't need it anymore
        if (!original.isRecycled) {
            original.recycle()
        }

        // Now start the editing activity
        val intent = Intent(this, EditImageActivity::class.java)
        startActivityForResult(intent, EDIT_REQUEST_CODE)
    }

    companion object {
        private const val EDIT_REQUEST_CODE = 1001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            Extensions.BitmapCache.getBitmap()?.let { rawEditedBitmap ->
                val currentPosition = binding.pager.currentItem

                // OFF MAIN THREAD: Copy + downsample
                lifecycleScope.launch(Dispatchers.IO) {
                    val safeCopy = downsampleBitmap(rawEditedBitmap)
                        .copy(rawEditedBitmap.config ?: Bitmap.Config.ARGB_8888, true)

                    withContext(Dispatchers.Main) {
                        editedBitmaps[currentPosition] = safeCopy
                        binding.pager.adapter?.notifyItemChanged(currentPosition)

                        Extensions.BitmapCache.clear() // Now safe
                    }
                }

                binding.ivSave.isVisible = true
            }
        }
    }
    // Create a new file: BitmapCache.kt

    override fun onDestroy() {
        Extensions.BitmapCache.clear() // This should recycle the cached bitmap
        editedBitmaps.values.forEach {
            if (!it.isRecycled) it.recycle()
        }
        editedBitmaps.clear()
        super.onDestroy()
    }


}