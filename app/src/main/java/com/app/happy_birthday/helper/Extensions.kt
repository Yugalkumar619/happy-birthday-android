package com.app.happy_birthday.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.format.DateUtils
import android.text.method.PasswordTransformationMethod
import android.util.DisplayMetrics
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.LayoutEmptyBinding
import com.app.happy_birthday.databinding.LvItemFooterLoadingBinding
import com.app.happy_birthday.helper.Constants.KEY_ACCESS_EXPIRY
import com.app.happy_birthday.helper.Constants.KEY_UNLOCKED_COUNT
import com.app.happy_birthday.helper.Global.folderName
import com.app.happy_birthday.helper.Global.showSnackBar
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.helper.helper_model.AddImageModel
import com.app.happy_birthday.helper.helper_model.CalenderDatesModel
import com.app.happy_birthday.helper.helper_model.ValentineDay
import com.app.happy_birthday.mvvm.category.model.CategoryDataModel
import com.app.happy_birthday.mvvm.wallpaper.model.WallpaperDataModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Extensions {


    fun String.printLog(tag: String = "") {
        println("$tag ::: $this ")
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

   /* fun Context.showCustomSnackBar(
        layoutSnackBar: LayoutSnackBarBinding,
        strMsg: String,
        showBelowStatus: Boolean = false,
        duration:Int = 2000, //pass this true where the theme is fullscreen
        gravity : Int = Gravity.TOP
    ) {

        val snackbar = Snackbar.make(layoutSnackBar.root, strMsg, Snackbar.LENGTH_LONG)
        val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)
        layoutParams.gravity = gravity
        snackbar.view.setPadding(0, 10, 0, 10)

        if (showBelowStatus) {//If true, adding top margin to show it below status bar,
            // because in fullscreen theme statusBar is overlapping snackBar
            var statusBarHeight = 0
            val resourceId: Int =
                resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resourceId)
            }
            layoutParams.topMargin = statusBarHeight
        }
        snackbar.view.layoutParams = layoutParams
        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        val context = snackbar.context
        layoutSnackBar.txtSnackBarText.text = strMsg
        val snackBarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackBarLayout.addView(layoutSnackBar.root)
        snackbar.setBackgroundTint(
            ContextCompat.getColor(
                context,
                R.color.black
            )
        )
        snackbar.duration = duration
        snackbar.show()
    }*/

    fun String.capitalizeFirstChar(): String {
        return this.replaceFirstChar { it.uppercase() }
    }

    fun ViewPager2.removeItemDecorations() {
        while (this.itemDecorationCount > 0) {
            this.removeItemDecorationAt(0)
        }
    }

    fun RecyclerView.removeItemDecoration() {
        while (this.itemDecorationCount > 0) {
            this.removeItemDecorationAt(0)
        }
    }

    fun Uri.getOriginalFileName(context: Context): String? {
        return context.contentResolver.query(this, null, null, null, null)?.use {
            val nameColumnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameColumnIndex)
        }
    }

    fun EditText.applyHintSize() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                textSize = if (text?.length == 0) {
                    context.resources.getDimension(R.dimen.hintTextSize) / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
                } else {
                    context.resources.getDimension(R.dimen.edtTextSize) / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
                }
            }
        })
    }

    fun TextView.applyHintSize() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                textSize = if (text?.length == 0) {
                    context.resources.getDimension(R.dimen.hintTextSize) / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
                } else {
                    context.resources.getDimension(R.dimen.edtTextSize) / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
                }
            }
        })
    }

    fun Activity.transparentStatusBarTheme() {
        setTheme(R.style.TransparentStatusBarTheme)
        val window: Window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        }
        window.statusBarColor = Color.TRANSPARENT
    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }


    fun EditText.applyMaxLength(maxLength: Int) {
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

    fun String?.appendComma(): String {
        if (!isNullOrEmpty()) {
            return "$this, "
        }
        return ""
    }

    fun String?.concat(char: String): String {
        if (!isNullOrEmpty()) {
            return "$this$char"
        }
        return ""
    }

    fun EditText.showHidePassword() {
        if (transformationMethod == null) {
            transformationMethod = PasswordTransformationMethod()
            setSelection(text?.length ?: 0)
        } else {
            transformationMethod = null
            setSelection(text?.length ?: 0)
        }
    }

    fun ViewPager2.setAutoScroll() {
        val position = currentItem
        try {
            if (position < (adapter?.itemCount ?: 0) - 1)
                currentItem = position + 1
            else
                setCurrentItem(0, true)
        } catch (e: Exception) {
        }
    }

    fun handler(delay: Long, block: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            block()
        }, delay)
    }

    fun Activity.getGalleryIntent(isMultipleSelect: Boolean = false, block: (i: Intent) -> Unit) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultipleSelect)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        block(intent)
    }

    fun Activity.getCameraIntent(block: (i: Intent, outputPath: String?, outputUri: Uri?) -> Unit) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            var filePath = ""
            try {
                photoFile = createImageFile(this).apply { filePath = absolutePath }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(this, packageName, it)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                block(takePictureIntent, filePath, photoURI)
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(activity: Activity): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    fun Activity.getFilePickerIntent(block: (i: Intent) -> Unit) {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        if (pdfIntent.resolveActivity(packageManager) != null) {
            block(pdfIntent)
        }
    }

    fun Activity.receiveDataFromCamera(
        image: String?,
        photoURI: Uri?,
        isBase64Enable : Boolean = true,
        block: (model: AddImageModel) -> Unit,
    ) {
        try {
            val cameraBitmap = image.getExifRotationInDegree()
            val title = photoURI?.getOriginalFileName(this) ?: ""
            val imgBase64 = if (isBase64Enable) Base64Converter.convertImgToBase64(cameraBitmap) else ""
            val model = AddImageModel(
                image = cameraBitmap,
                imgBase64 = imgBase64,
                title = title,
                filePath = image,
                imageID = "",
                imageUrl = ""
            )
            block(model)
        } catch (e: Exception) {
            return
        }
    }

    fun String?.toFileUri():Uri{
       return Uri.fromFile(this?.let { File(it) })
    }

    fun Activity.receiveMultipleDataFromGallery(
        result: Intent?,
        isBase64Enable : Boolean = true,
        block: (model: ArrayList<AddImageModel>) -> Unit,
    ) {
        try {
            val list = ArrayList<AddImageModel>()
            val count = result?.clipData?.itemCount
            if (result?.clipData != null) {
                //multiple image
                for (i in 0 until (count ?: 0)) {
                    val imageUri: Uri = result.clipData?.getItemAt(i)?.uri ?: "".toUri()
                    val galleryBitmap = getBitmapFromGallery(this, imageUri)
                    val title = imageUri.getOriginalFileName(this) ?: ""
                    val filePath = URIPathHelper.getPath(imageUri, this)
                    val imgBase64 = if (isBase64Enable) Base64Converter.convertImgToBase64(imageUri, this) else ""
                    val model = AddImageModel(
                        image = galleryBitmap,
                        imgBase64 = imgBase64,
                        title = title,
                        filePath = filePath,
                        imageID = "",
                        imageUrl = ""
                    )
                    list.add(model)
                }
            } else {
                //single image
                val imageUri: Uri = result?.data ?: "".toUri()
                val galleryBitmap = getBitmapFromGallery(this, imageUri)
                val title = imageUri.getOriginalFileName(this) ?: ""
                val filePath = URIPathHelper.getPath(imageUri, this)
                val imgBase64 = if(isBase64Enable) Base64Converter.convertImgToBase64(imageUri, this) else ""
                val model = AddImageModel(
                    image = galleryBitmap,
                    imgBase64 = imgBase64,
                    title = title,
                    filePath = filePath,
                    imageID = "",
                    imageUrl = ""
                )
                list.add(model)
            }
            block(list)
        } catch (e: Exception) {
            return
        }
    }

    private fun getBitmapFromGallery(activity: Activity, targetUri: Uri): Bitmap {
        return try {
            val imagePath = URIPathHelper.getPath(targetUri, activity)
            imagePath.getExifRotationInDegree()
        } catch (e: Exception) {
            BitmapFactory.decodeStream(activity.contentResolver.openInputStream(targetUri))
        }
    }


    fun String?.getExifRotationInDegree(): Bitmap {
        val bitmap = BitmapFactory.decodeFile(this)
        val exif = this?.let { ExifInterface(it) }
        val rotation = exif?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val degree = when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                90f
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                180f
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                270f
            }
            else -> 0f
        }

        return bitmap.rotate(degree)
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }


    fun Dialog?.dismissIfShowing() {
        try {
            if (this?.isShowing == true) {
                this.dismiss()
            }
        } catch (e: Exception) {
            return
        }
    }


    fun Date.isToday(): Boolean = DateUtils.isToday(this.time)

    private var searchJob: Job? = null
    fun EditText.search(lifecycleScope: LifecycleCoroutineScope, block: (query: String) -> Unit) {
        doAfterTextChanged {
            searchJob?.cancel()
            /** cancel previous job when user enters new letter */
            searchJob = lifecycleScope.launch {
                /** add some delay before search, this function checks if coroutine is canceled, if it is canceled it won't continue execution*/
                delay(400)
                block(it.toString())
            }
        }
    }

    fun NestedScrollView.scrollToPosition(view: View) {
        smoothScrollTo(0, view.y.toInt())
    }

    fun ViewPager2.reduceDragSensitivity() {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView
        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * 6)       // "8" was obtained experimentally
    }

    fun ViewPager2.setCarouselPagerTransformation(nextItemVisiblePx: Float, currentItemHorizontalMarginPx: Float) {
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
            //page.alpha = 0.75f + (1 - kotlin.math.abs(position))
        }
        setPageTransformer(pageTransformer)
    }

    fun Context.isInternetEnabled(view: View ?= null, swipeRefreshLayout: SwipeRefreshLayout? = null, block: () -> Unit) {
        if (NetworkUtil.getConnectivityStatus(this)) {
            block()
        } else {
            swipeRefreshLayout?.isRefreshing = false
            (this as Activity).showSnackBar(resources.getString(R.string.no_internet))
        }
    }

    fun String?.asDouble(): Double {
        if (!this.isNullOrEmpty()) {
            return try {
                if (this.contains(",")){this.replace(",","")}
                this.toDouble()
            } catch (e: Exception) {
                0.0
            }
        }
        return 0.0
    }

    fun Int?.asDouble(): Double {
        if (this != null) {
            return try {
                this.toDouble()
            } catch (e: Exception) {
                0.0
            }
        }
        return 0.0
    }

    fun String?.asInt(): Int {
        if (!this.isNullOrEmpty()) {
            return try {
                this.toInt()
            } catch (e: Exception) {
                0
            }
        }
        return 0
    }

    fun String?.asFloat(): Float {
        if (!this.isNullOrEmpty()) {
            return try {
                this.toFloat()
            } catch (e: Exception) {
                0f
            }
        }
        return 0f
    }

    fun Double?.asInt(): Int {
        if (this != null) {
            return try {
                this.toInt()
            } catch (e: Exception) {
                0
            }
        }
        return 0
    }

    fun Int?.asString(): String {
        if (this != null) {
            return try {
                this.toString()
            } catch (e: Exception) {
                "0"
            }
        }
        return "0"
    }


    fun Float?.asString():String{
        if (this != null) {
            return try {
                this.toString()
            } catch (e: Exception) {
                ""
            }
        }
        return ""
    }

    fun Float?.asInt():Int{
        if (this != null) {
            return try {
                this.toInt()
            } catch (e: Exception) {
                0
            }
        }
        return 0
    }

    fun String?.setPhoneCodeWithPlus(): String {
        //while showing adding plus from our side
        return if (!this.isNullOrEmpty()) {
            "+$this"
        } else {
            ""
        }
    }

    fun String?.getPhoneCodeWithoutPlus(): String {
        //while sending to server, remove plus
        return if (!this.isNullOrEmpty()) {
            this.replace("+", "")
        } else {
            ""
        }
    }


    fun Boolean.then(block: () -> Unit) {
        if (this) {
            block()
        } else {
            return
        }
    }

    fun String.toDate(
        dateFormat: String = Constants.DATE_FORMAT,
        timeZone: TimeZone = TimeZone.getTimeZone("UTC")
    ): Date? {
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }

    fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }


    fun Context.openWhatsapp(number:String) {
        var url = "https://api.whatsapp.com/send?phone=${number}"
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            try {
                url = "https://play.google.com/store/apps/details?id=com.whatsapp"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        } catch (e: ActivityNotFoundException) {
            url = "https://play.google.com/store/apps/details?id=com.whatsapp"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }


    fun LayoutEmptyBinding.init(
        title: String,
        subTitle: String,
        image: Int = 0,
        btnLabel: String = "",
        isIconVisible: Boolean = false,
        isBtnVisible: Boolean = false,
        listener: CommonInterfaceClickEvent?= null
    ) {
        this.txtEmptyMessage.text = title
        this.txtEmptySubMessage.text = subTitle

        this.ivEmptyIcon.isVisible = isIconVisible
        if (isIconVisible)this.ivEmptyIcon.setImageResource(image)

        this.btnNavigate.isVisible = isBtnVisible
        if (isBtnVisible)this.btnNavigate.text = btnLabel
        this.btnNavigate.setOnClickListener {
            listener?.onEmptyLayoutClick("B")
        }
    }

    fun <T : Serializable?> Intent.getSerializable(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializableExtra(key, m_class)!!
        else
            this.getSerializableExtra(key) as T
    }

    fun Context.isVisibleInvisible(condition :Boolean): Int {
        return if (condition) View.VISIBLE else View.INVISIBLE
    }

    fun getCurrentDate(inputFormat: String): String {
        val sdf = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        return sdf.format(Date())
    }

    fun getStringToDate(inputFormat: String, date: String): Date? {
        return try {
            val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            simpleDateFormat.parse(date)
        } catch (e: Exception) {
            val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            simpleDateFormat.parse(getCurrentDate(inputFormat))
        }
    }
    fun getAllDatesInMonth(year: Int, month: Int, context: Context, setTodaySelected: Boolean = true): ArrayList<CalenderDatesModel> {
        val arrListDays: ArrayList<CalenderDatesModel> = ArrayList()
        val fmt = SimpleDateFormat(Constants.DATE_DAY_FORMAT, Locale.ENGLISH)
        val cal = Calendar.getInstance()
        cal.clear()
        cal[year, month - 1] = 1
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val currentDate = getStringToDate(
            Constants.SIMPLE_DATE_FORMAT,
            getCurrentDate(Constants.SIMPLE_DATE_FORMAT)
        )
        for (i in 0 until daysInMonth) {
            val strDate = fmt.format(cal.time)
            val dateWithDay = Global.arabicToEnglish(strDate, context).split(" ")
            val date = getStringToDate(Constants.SIMPLE_DATE_FORMAT, dateWithDay[0])
            if (date?.before(currentDate) != true) {
                if (date == currentDate && setTodaySelected) {
                    arrListDays.add(CalenderDatesModel(dateWithDay[0], dateWithDay[1], true))
                } else {
                    arrListDays.add(CalenderDatesModel(dateWithDay[0], dateWithDay[1], false))
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        if (arrListDays.isNotEmpty()) {
            if (arrListDays[0].isSlotSelected != true && setTodaySelected) {
                arrListDays[0].isSlotSelected = true
            }
        }
        return arrListDays
    }

    fun NestedScrollView.setPaginationListener(
        pageSize: Int,
        layoutFooter: LvItemFooterLoadingBinding,
        currentPg:Int = 1,
        callback: (page: Int) -> Unit,
    ) {
        try {
            var currentPage = currentPg
            if (currentPage < pageSize) {
                currentPage += 1
                callback(currentPage)
                layoutFooter.root.isVisible = currentPage < pageSize
                handler(1000){
                    layoutFooter.root.isVisible = currentPage < pageSize
                }
            }
            this.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                if (pageSize > 0) {
                    val totalHeight = this.getChildAt(0)?.height ?: 0
                    if (scrollY >= totalHeight - this.height && currentPage < totalHeight / pageSize) {
                        if (currentPage < pageSize) {
                            currentPage += 1
                            callback(currentPage)
                            layoutFooter.root.isVisible = currentPage < pageSize
                            handler(1000){
                                layoutFooter.root.isVisible = currentPage < pageSize
                            }
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun Activity.addAnimations(vararg args : View) : ActivityOptionsCompat {
        val pairs: Array<Pair<View, String>> = Array(args.size){ index ->
            Pair(args[index] , args[index].transitionName)
        }
        return ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
    }


    fun Context.getWallpaperImages(isPremium: Boolean= false, premiumCount: Int = 0, day: ValentineDay ): java.util.ArrayList<WallpaperDataModel> {
        val images = arrayListOf<WallpaperDataModel>()
        // Add images from j1 to j141 dynamically
        when(day){
            ValentineDay.PREMIUM -> {
                for (i in 1..premiumCount) {
                    // Use the resource name dynamically
                    val imageResId = resources.getIdentifier("premium_$i", "drawable", packageName)

                    // Add the image to the list
                    images.add(WallpaperDataModel(imageResId))
                }
            }

            ValentineDay.ROSE_DAY -> {
                for (i in 1..20) {
                    // Use the resource name dynamically
                    val imageResId = resources.getIdentifier("rose_$i", "drawable", packageName)

                    // Add the image to the list
                    images.add(WallpaperDataModel(imageResId))
                }
            }

            ValentineDay.PROPOSE_DAY -> {
                for (i in 1..20) {
                    // Use the resource name dynamically
                    val imageResId = resources.getIdentifier("propose_$i", "drawable", packageName)

                    // Add the image to the list
                    images.add(WallpaperDataModel(imageResId))
                }
            }

            ValentineDay.CHOCOLATE_DAY -> {
                for (i in 1..20) {
                    // Use the resource name dynamically
                    val imageResId = resources.getIdentifier("chocolate_$i", "drawable", packageName)

                    // Add the image to the list
                    images.add(WallpaperDataModel(imageResId))
                }
            }

            ValentineDay.TEDDY_DAY -> {
                for (i in 1..20) {
                    // Use the resource name dynamically
                    val imageResId = resources.getIdentifier("teddy_$i", "drawable", packageName)

                    // Add the image to the list
                    images.add(WallpaperDataModel(imageResId))
                }
            }

            ValentineDay.PROMISE_DAY -> {
                for (i in 1..20) {
                    // Use the resource name dynamically
                    val imageResId = resources.getIdentifier("promise_$i", "drawable", packageName)

                    // Add the image to the list
                    images.add(WallpaperDataModel(imageResId))
                }
            }

            ValentineDay.HUG_DAY -> {
                for (i in 1..20) {
                    // Use the resource name dynamically
                    val imageResId = resources.getIdentifier("hug_$i", "drawable", packageName)

                    // Add the image to the list
                    images.add(WallpaperDataModel(imageResId))
                }
            }

            ValentineDay.KISS_DAY -> {
                for (i in 1..20) {
                    // Use the resource name dynamically
                    val imageResId = resources.getIdentifier("kiss_$i", "drawable", packageName)

                    // Add the image to the list
                    images.add(WallpaperDataModel(imageResId))
                }
            }

            ValentineDay.VALENTINES_DAY -> {
                for (i in 1..20) {
                    // Use the resource name dynamically
                    val imageResId = resources.getIdentifier("valentine_$i", "drawable", packageName)

                    // Add the image to the list
                    images.add(WallpaperDataModel(imageResId))
                }
            }

            else   -> {

            }

        }

        return images
    }

    fun Context.getCategoryData(): ArrayList<CategoryDataModel>{
        val data = arrayListOf<CategoryDataModel>()
        data.add(
            CategoryDataModel(
                image = R.drawable.ic_rose,
                day = ValentineDay.ROSE_DAY
            )
        )

        data.add(
            CategoryDataModel(
                image = R.drawable.ic_ring,
                day = ValentineDay.PROPOSE_DAY
            )
        )

        data.add(
            CategoryDataModel(
                image = R.drawable.ic_chocolate,
                day = ValentineDay.CHOCOLATE_DAY
            )
        )

        data.add(
            CategoryDataModel(
                image = R.drawable.ic_teddy,
                day = ValentineDay.TEDDY_DAY
            )
        )

        data.add(
            CategoryDataModel(
                image = R.drawable.ic_promise,
                day = ValentineDay.PROMISE_DAY
            )
        )

        data.add(
            CategoryDataModel(
                image = R.drawable.ic_hug,
                day = ValentineDay.HUG_DAY
            )
        )

        data.add(
            CategoryDataModel(
                image = R.drawable.ic_kiss,
                day = ValentineDay.KISS_DAY
            )
        )

        data.add(
            CategoryDataModel(
                image = R.drawable.ic_valentines,
                day = ValentineDay.VALENTINES_DAY
            )
        )


        return data
    }

    fun getAllImagesFromRamWallpaperFolder(context: Context): List<Uri> {
        val imageList = mutableListOf<Uri>()

        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // Only include RELATIVE_PATH on Android 10+
        val projection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
            )
        } else {
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
            )
        }

        println("MediaStore.Images.Media.DATA  :::: ${MediaStore.Images.Media.DATA}")
        println("MediaStore.Images.Media.RELATIVE_PATH  :::: ${MediaStore.Images.Media.RELATIVE_PATH}")
        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ : Use RELATIVE_PATH
            "${MediaStore.Images.Media.RELATIVE_PATH} = ?"
        } else {
            // Android 9 and below (fallback - less reliable)
            "${MediaStore.Images.Media.DATA} LIKE ?"
        }

        val selectionArgs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf("Pictures/${folderName}/")  // Note the trailing slash!
        } else {
//            val picturesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
//            arrayOf("${picturesPath}/${folderName}/%")
            arrayOf("%/$folderName/%")
        }

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageList.add(contentUri)
            }
        }

        return imageList
    }

    fun deleteFile(context: Context, uri: Uri?): Boolean {
        return try {
            if (uri==null){
                false
            }else{
                context.contentResolver.delete(uri, null, null) > 0
                        || DocumentsContract.deleteDocument(context.contentResolver, uri)
            }

        } catch (e: Exception) {
            false
        }
    }

    fun Activity.getAppVersionName(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    fun Activity.getAppVersionCode(): Long {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (e: Exception) {
            0L
        }
    }

    fun <T : Serializable?> Bundle.getSerializableViaArgument(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializable(key, m_class)!!
        else
            this.getSerializable(key) as T
    }

    // Kotlin (most common today)
    fun openPublisherOnPlayStore(context: Context) {
        val publisherName = "YUGAL KUMAR"
        val uri = Uri.parse("https://play.google.com/store/search?q=pub:$publisherName&c=apps")

        val playStoreIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // This package name forces opening in Play Store app
            setPackage("com.android.vending")
        }

        try {
            context.startActivity(playStoreIntent)
        } catch (e: ActivityNotFoundException) {
            // Play Store not installed → open in browser
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(browserIntent)
        }
    }

    fun openMyAppOnPlayStore(context: Context) {
        val uri = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")

        val playStoreIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // This package name forces opening in Play Store app
            setPackage("com.android.vending")
        }

        try {
            context.startActivity(playStoreIntent)
        } catch (e: ActivityNotFoundException) {
            // Play Store not installed → open in browser
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(browserIntent)
        }
    }

    // Add this function anywhere (in MainActivity or Utils.kt)
    fun Context.toastRamWallpaperFolder() {
        val folderPath = "/storage/emulated/0/Pictures/${folderName}"
        Toast.makeText(this, "Wallpapers saved here: $folderPath", Toast.LENGTH_LONG).show()
    }

    fun openPrivacyPolicy(context: Context) {
        val privacyPolicyUrl = "https://inspirationaljoker.blogspot.com/p/valentines-day-wishes-privacy-policy.html"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // Safe for non-activity context
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open Privacy Policy. Please visit our website.", Toast.LENGTH_LONG).show()
        }
    }

    fun TextView.makeCopyable(
        label: String = "text", // label shown in clipboard
        showToast: Boolean = true,
        toastText: String = "Copied to clipboard!",
        click: ImageView,
        hapticFeedback: Boolean = true,
        onCopied: ((copiedText: String) -> Unit)? = null // ✅ callback
    ) {
        isClickable = true
        isLongClickable = true

        click.setOnClickListener {
            val textToCopy = text?.toString() ?: return@setOnClickListener

            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label, textToCopy)
            clipboard.setPrimaryClip(clip)

            // Haptic feedback
            if (hapticFeedback) {
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            }

            // Optional toast
            if (showToast) {
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            }

            // ✅ CALLBACK
            onCopied?.invoke(textToCopy)
        }
    }


    fun Context.shareText(
        text: String,
        chooserTitle: String = "Share via"
    ) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(shareIntent, chooserTitle))
    }

    fun SharedPreferences.getUnlockedWallpaperCount(context: Activity): Int {
        return if (hasPremiumAccess(context)) {
            getInt(KEY_UNLOCKED_COUNT, 0)
        } else {
            // Access expired → reset unlocked count to 1 and save it
            edit()
                .putInt(KEY_UNLOCKED_COUNT, 0)
                .apply()
            0  // Return 1 as the current count
        }
    }

    fun SharedPreferences.hasPremiumAccess(context: Activity): Boolean {
//        val expiry = getLong(KEY_ACCESS_EXPIRY, 0L)
//        return System.currentTimeMillis() < expiry

        // Step 1: Check if user is paid premium via BillingManager
        val app = context.applicationContext as? AppController
        val isPaidPremium = app?.billingManager?.isPremium?.value == true

        if (isPaidPremium) {
            // Paid premium → always true, no expiry
            return true
        }

        // Step 2: Free users → check ad-based temporary expiry
        val expiry = getLong(KEY_ACCESS_EXPIRY, 0L)
        return System.currentTimeMillis() < expiry
    }


    object BitmapCache {
        var currentBitmap: Bitmap? = null
            private set

        fun setBitmap(newBitmap: Bitmap) {
            currentBitmap = newBitmap.copy(newBitmap.config ?: Bitmap.Config.ARGB_8888, true)
            // Do NOT recycle old one here — let onDestroy handle cleanup
        }

        fun getBitmap(): Bitmap? = currentBitmap

        fun clear() {
            currentBitmap?.let {
                if (!it.isRecycled) it.recycle()
            }
            currentBitmap = null
        }

    }

    fun downsampleBitmap(original: Bitmap, maxWidth: Int = 1080, maxHeight: Int = 1920): Bitmap {
        val ratio = minOf(maxWidth.toFloat() / original.width, maxHeight.toFloat() / original.height)
        if (ratio >= 1f) return original.copy(original.config ?: Bitmap.Config.ARGB_8888, true)

        val newWidth = (original.width * ratio).toInt()
        val newHeight = (original.height * ratio).toInt()
        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true)
    }


}