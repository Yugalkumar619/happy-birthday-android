package com.app.happy_birthday.mvvm.saved.view

import android.app.Dialog
import android.app.WallpaperManager
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
import android.provider.OpenableColumns
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
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.ActivitySavedImageBinding
import com.app.happy_birthday.databinding.LvItemPagerBinding
import com.app.happy_birthday.helper.BaseActivity
import com.app.happy_birthday.helper.Extensions.deleteFile
import com.app.happy_birthday.helper.Extensions.getAllImagesFromRamWallpaperFolder
import com.app.happy_birthday.helper.Global.loadPhotoUsingCoil
import com.app.happy_birthday.mvvm.wallpaper.model.WallpaperDataModel
import com.app.happy_birthday.mvvm.wallpaper.view.ImageViewerObj
import java.io.OutputStream


class SavedImageActivity : BaseActivity() {
    /** Define variables */
    lateinit var binding: ActivitySavedImageBinding
    private var imageObj: ImageViewerObj? = null
    private var thumbnail: Int? = null
    private var wallpaperImage: Uri? = null
    private var currentPage: Int = 0
    private var height: Int = 0
    private lateinit var wallpaperManager: WallpaperManager

    //    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var cResolver: ContentResolver
    var shareBtnClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cResolver = this.contentResolver
        wallpaperManager = WallpaperManager.getInstance(this)

//        initializeAds()
        initializedFields()
        initializeClickListener()
    }

    override fun getTopInsetView(): View? {
        return binding.root
    }

    /** Initialize Section */
    private fun initializedFields() {

        /** Set Full Screen */
        setFullScreen()

        /** Set Display */
        setDisplay()

        /** Set Data */
        if(intent.getSerializableExtra("ImageViewerObj") != null)  {
            imageObj = ImageViewerObj()
            imageObj = intent.getSerializableExtra("ImageViewerObj") as ImageViewerObj
            var data = getAllImagesFromRamWallpaperFolder(this)
            data.forEach {
                imageObj?.images?.add(WallpaperDataModel(imageUri = it))
            }
            wallpaperImage = imageObj?.images?.get(imageObj?.currentPosition ?: 0)?.imageUri
            setPager()
        }

        /** Set Back Press */
        setBackPress()
    }
    private fun initializeClickListener() {

        binding.fabDelete.setOnClickListener {
            if (deleteFile(this, wallpaperImage)) {
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.fabShare.setOnClickListener {
            shareBtnClicked = true
            share(cResolver)
        }

        binding.ivBack.setOnClickListener {
            finish()

            // Don't show adds too frequently
//            if (mInterstitialAd != null) {
//                mInterstitialAd?.show(this)
//            } else {
//                Log.d("TAG", "The interstitial ad wasn't ready yet.")
//            }
        }

        binding.fabWallpaper.setOnClickListener {
            showWallpaperOptionsDialog()
            binding.fabMenu.collapse()
        }

        binding.fabDetails.setOnClickListener {
            showWallpaperDetailsDialog(wallpaperImage)
        }

    }

    //    private fun initializeAds() {
//
//        // Initialize AdMob
//        MobileAds.initialize(this) {}
//
//        var adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(
//            this,
//            Context.getString(R.string.google_ad_unit_id_dev),
//            adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    adError.toString().let { Log.d(TAG, it) }
//                    mInterstitialAd = null
//                }
//
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    Log.d(TAG, "Ad was loaded.")
//                    mInterstitialAd = interstitialAd
//                }
//            })
//
//
//        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//            override fun onAdClicked() {
//                // Called when a click is recorded for an ad.
//                Log.d(TAG, "Ad was clicked.")
//            }
//
//            override fun onAdDismissedFullScreenContent() {
//                // Called when ad is dismissed.
//                Log.d(TAG, "Ad dismissed fullscreen content.")
//                mInterstitialAd = null
//            }
//
//            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                // Called when ad fails to show.
//                super.onAdFailedToShowFullScreenContent(adError)
//                Log.e(TAG, "Ad failed to show fullscreen content.")
//                InterstitialAd.load(
//                    this@ImageViewerActivity,
//                    Context.getString(R.string.google_ad_unit_id_dev),
//                    adRequest,
//                    object : InterstitialAdLoadCallback() {
//                        override fun onAdFailedToLoad(adError: LoadAdError) {
//                            adError.toString().let { Log.d(TAG, it) }
//                            mInterstitialAd = null
//                        }
//
//                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                            Log.d(TAG, "Ad was loaded.")
//                            mInterstitialAd = interstitialAd
//                        }
//                    })
//            }
//
//            override fun onAdImpression() {
//                // Called when an impression is recorded for an ad.
//                Log.d(TAG, "Ad recorded an impression.")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                // Called when ad is shown.
//                Log.d(TAG, "Ad showed fullscreen content.")
//            }
//        }
//    }


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
//                    this@SavedImageActivity,
//                    "${imageTitle}_$currentPage",
//                    UtilExtension.drawableToByteArray(
//                        this@SavedImageActivity,
//                        urlData!!
//                    )
//                )
//
//                /** checking the status and displaying the message*/
//                withContext(Dispatchers.Main) {
//                    hideProgressDialog()
//                    if (status == "ok") {
//                        Toast.makeText(
//                            this@SavedImageActivity,
//                            "Image saved successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        Toast.makeText(
//                            this@SavedImageActivity,
//                            "Please enable storage permission",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    /** After saving displaying the Ad while going back*/
////                        if (mInterstitialAd != null) {
////                            mInterstitialAd?.show(this@ImageViewerActivity)
////                        } else {
////                            Log.d("TAG", "The interstitial ad wasn't ready yet.")
////                        }
//                    binding.ivBack.performClick()
//
//                }
//            }
//
//        }
//    }
    fun share(contentResolver: ContentResolver) {
        wallpaperImage?.let { uri ->

            val options = BitmapFactory.Options().apply {
                inScaled = false
            }
//            val bitPhoto = BitmapFactory.decodeResource(resources, thumbnail!!.toInt(), options)
            val bitPhoto = contentResolver.openInputStream(uri).use { BitmapFactory.decodeStream(it) }

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
//                val wallpaperBitmap = BitmapFactory.decodeResource(resources, wallpaperImage ?: 0)
                wallpaperImage?.let { uri ->
                    val wallpaperBitmap = contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
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
                    }
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
    private fun showWallpaperDetailsDialog(imageUri: Uri?) {
        if (imageUri == null) {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
            return
        }

        // Get resolution
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        contentResolver.openInputStream(imageUri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }

        val width = options.outWidth
        val height = options.outHeight
        if (width <= 0 || height <= 0) {
            Toast.makeText(this, "Invalid image", Toast.LENGTH_SHORT).show()
            return
        }

        // Get 100% exact size using ParcelFileDescriptor
        val sizeInBytes = getExactFileSize(imageUri)

        val sizeText = if (sizeInBytes <= 0) {
            "Unknown size"
        } else if (sizeInBytes >= 1024 * 1024) {
            String.format("%.2f MB", sizeInBytes / (1024.0 * 1024.0))
        } else if (sizeInBytes >= 1024) {
            String.format("%.1f KB", sizeInBytes / 1024.0)
        } else {
            "$sizeInBytes bytes"
        }

        // Show dialog
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_wallpaper_details)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialog.findViewById<TextView>(R.id.tvResolution).text = "$width × $height px"
        dialog.findViewById<TextView>(R.id.tvSize).text = sizeText
        dialog.findViewById<ImageView>(R.id.ivClose).setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
    private fun getExactFileSize(uri: Uri): Long {
        // 1. Try MediaStore / OpenableColumns first (fastest)
        val projection = arrayOf(OpenableColumns.SIZE, MediaStore.MediaColumns.SIZE)
        try {
            contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex != -1 && cursor.moveToFirst() && !cursor.isNull(sizeIndex)) {
                    val size = cursor.getLong(sizeIndex)
                    if (size > 0) return size
                }

                val mediaSizeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)
                if (mediaSizeIndex != -1 && !cursor.isNull(mediaSizeIndex)) {
                    val size = cursor.getLong(mediaSizeIndex)
                    if (size > 0) return size
                }
            }
        } catch (e: Exception) { /* ignore */ }

        // 2. Fallback: Use ParcelFileDescriptor (this is the golden method – always works)
        return try {
            contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                pfd.statSize
            } ?: 0L
        } catch (e: Exception) {
            0L
        }
    }



    /** View Pager Section */
    inner class DetailOnPageChangeListener : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.fabMenu.collapse()
            currentPage = position
            wallpaperImage = imageObj?.images?.get(position)?.imageUri
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

            if (imageObj?.images?.get(position)?.imageUri != null) {
                thumbnail = imageObj?.images?.get(position)?.image
                holder.binding.snoop.loadPhotoUsingCoil(
                    imageObj?.images?.get(position)?.imageUri,
                    R.drawable.ic_hindu_flag,
                    holder.binding.layoutSpinner
                )

            }

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

}