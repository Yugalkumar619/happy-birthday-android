package com.app.happy_birthday.helper

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned

import android.util.*
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.ComponentRegistry
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.LayoutCommonSpinnerBinding
import com.app.happy_birthday.databinding.LayoutSnackBarBinding
import com.app.happy_birthday.helper.Extensions.asDouble
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.app.happy_birthday.helper.PrefUtils.getPrefCurrencyAR
import com.app.happy_birthday.helper.PrefUtils.getPrefCurrencyEN
import com.app.happy_birthday.helper.PrefUtils.isEnglishLanguage
import us.egek.proteinbar.Protein
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("StaticFieldLeak")
object Global {

    /** set to false when latest build uploading on play store */
    const val isTestModeEnabled = true
    fun Boolean.isEnabled(block: () -> Unit) {
        if (this) block() else return
    }
    val folderName = "ValentinesDay"
    var isMessageShown = false
    val imageTitle = "valentines_day"
    val moreApps = "https://play.google.com/store/search?q=pub%3A%20YUGAL%20KUMAR&c=apps&hl=en_IN"

    const val INSTABUG_KEY_DEBUG = "77d14f56b5ac2a9fe470162c71b47b15"

    private lateinit var context: Context

    fun showCustomToast(context: Context, strMsg: String) = Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show()

    fun getDeviceWidthInDouble(activity: Activity): Double {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels.toDouble()
    }

    fun getDimension(activity: Activity, size: Double): Int {
        return if (Constants.DEVICE_DENSITY > 0) {
            //density saved in constant calculated on first time in splash if in case its 0 then calculate again
            (Constants.DEVICE_DENSITY * size).toInt()
        } else {
            ((getDeviceWidthInDouble(activity) / 320) * size).toInt()

        }
    }

    fun setFontSize(activity: Activity, value: Float): Float {
        return value / (activity.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun setHtmlTextView(strData: String?): Spanned? {
        //android does not support \r\n so handle it manually
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(strData.toString().replace("\r\n", "<br/>"), Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(strData.toString().replace("\r\n", "<br/>"))
        }
    }

    fun setTextViewData(value: String?): String {
        return if (!value.isNullOrEmpty() && value.trim().isNotEmpty()) {
            value.trim()
        } else {
            ""
        }
    }

    fun Context.showMessage(message:String){
        Toast.makeText(this , message , Toast.LENGTH_SHORT).show()
    }

    fun String?.setPriceWithCurrency(context: Context): String =
        if (this.isNullOrEmpty()) {
            ""
        } else {
            try {
                if (context.isEnglishLanguage()) {
                    arabicToEnglish("${String.format("%.2f", this.asDouble())} ${context.getPrefCurrencyEN()}", context).uppercase()
                } else {
                    arabicToEnglish("${context.getPrefCurrencyAR()} ${String.format("%.2f", this.asDouble())}", context)
                }
            } catch (e: Exception) {
                ""
            }
        }

    fun String?.setPriceWithCurrencyEnd(context: Context): String =
        if (this.isNullOrEmpty()) {
            ""
        } else {
            try {
                if (context.isEnglishLanguage()) {
                    arabicToEnglish("${String.format("%.2f", this.asDouble())} ${context.getPrefCurrencyEN()}", context).uppercase()
                } else {
                    arabicToEnglish("${context.getPrefCurrencyAR()} ${String.format("%.2f", this.asDouble())}", context)
                }
            } catch (e: Exception) {
                ""
            }
        }

    fun String?.setOnlyPrice(context: Activity): String =
        if (this.isNullOrEmpty()) {
            ""
        } else {
            try {
                if (context.isEnglishLanguage()) {
                    String.format("%.3f", java.lang.Double.parseDouble(this.replace(",", "")))
                } else {
                    arabicToEnglish(
                        String.format(
                            "%.3f",
                            java.lang.Double.parseDouble(this.replace(",", ""))
                        ), context
                    )
                }
            } catch (e: java.lang.Exception) {
                ""
            }
        }

    fun String?.setOnlyPriceWithTwoDecimal(context: Activity, ): String =
        if (this.isNullOrEmpty()) {
            ""
        } else {
            try {
                if (context.isEnglishLanguage()) {
                    String.format("%.2f", java.lang.Double.parseDouble(this.replace(",", "")))
                } else {
                    arabicToEnglish(
                        String.format(
                            "%.2f",
                            java.lang.Double.parseDouble(this.replace(",", ""))
                        ), context
                    )
                }
            } catch (e: java.lang.Exception) {
                ""
            }
        }

    fun arabicToEnglish(strGetNumber: String?, context: Context): String {
        var strNumber = strGetNumber
        if (strNumber != null && strNumber.isNotEmpty()) {
            if (strNumber.contains("١")) strNumber = strNumber.replace("١".toRegex(), "1")
            if (strNumber.contains("٢")) strNumber = strNumber.replace("٢".toRegex(), "2")
            if (strNumber.contains("٣")) strNumber = strNumber.replace("٣".toRegex(), "3")
            if (strNumber.contains("٤")) strNumber = strNumber.replace("٤".toRegex(), "4")
            if (strNumber.contains("٥")) strNumber = strNumber.replace("٥".toRegex(), "5")
            if (strNumber.contains("٦")) strNumber = strNumber.replace("٦".toRegex(), "6")
            if (strNumber.contains("٧")) strNumber = strNumber.replace("٧".toRegex(), "7")
            if (strNumber.contains("٨")) strNumber = strNumber.replace("٨".toRegex(), "8")
            if (strNumber.contains("٩")) strNumber = strNumber.replace("٩".toRegex(), "9")
            if (strNumber.contains("٠")) strNumber = strNumber.replace("٠".toRegex(), "0")
            if (strNumber.contains("٫")) strNumber = strNumber.replace("٫".toRegex(), ".")
        } else {
            strNumber = ""
        }
        return monthsEnglishToArabic(strNumber, context)
    }

    fun getIsTodayFromDate(
        activity: Context,
        inputFormat: String,
        value: String
    ): Int {
        val calendarToday: Calendar = GregorianCalendar()
        val calendarTomorrow: Calendar = GregorianCalendar()
        val formatterDisplay = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        var localTime = ""

        localTime = getTimeConvertKuwaitToLocal(value, inputFormat)

        var a = ""
        if (localTime.isNotEmpty()) {
            val inputFormatter = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            val dateObj: Date?
            try {
                dateObj = inputFormatter.parse(localTime)
                val outputFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                a = outputFormatter.format(dateObj!!)
            } catch (e: ParseException) {
                a = localTime
            }
        }
        localTime = a

        calendarTomorrow.add(Calendar.DATE, 1)
        val strToday = formatterDisplay.format(calendarToday.time)
        val strTomorrow = formatterDisplay.format(calendarTomorrow.time)

        return when (localTime) {
            strToday -> {
                0
            }

            strTomorrow -> {
                1
            }

            else -> {
                2
            }
        }
    }

    @SuppressLint("NewApi")
    fun getTimeConvertKuwaitToLocal(aDate: String, inputFormat: String): String {
        val formatter = DateTimeFormatter.ofPattern(inputFormat, Locale.ENGLISH)
        return LocalDateTime.parse(aDate, formatter)
            .atOffset(getKuwaitZoneOffSet())
            .atZoneSameInstant(ZoneId.systemDefault())
            .format(formatter)
    }

    @SuppressLint("NewApi")
    fun getTimeConvertLocalToKuwait(aDate: String, inputFormat: String): String {
        val formatter = DateTimeFormatter.ofPattern(inputFormat, Locale.ENGLISH)
        return LocalDateTime.parse(aDate, formatter)
            .atOffset(getLocalZoneOffSet())
            .atZoneSameInstant(ZoneId.of("Asia/Kuwait"))
            .format(formatter)
    }

    @SuppressLint("NewApi")
    private fun getLocalZoneOffSet(): ZoneOffset {
        val instantObject = Instant.now()
        val systemDefaultZone = ZoneId.systemDefault()
        return systemDefaultZone.rules.getOffset(instantObject)
    }

    @SuppressLint("NewApi")
    private fun getKuwaitZoneOffSet(): ZoneOffset {
        val instantObject = Instant.now()
        val systemDefaultZone = ZoneId.of("Asia/Kuwait")
        return systemDefaultZone.rules.getOffset(instantObject)
    }

    fun monthShortEnglishToArabic(mMonth: String, context: Context): String {
        var strNumber = mMonth.lowercase()
        val arrWeekDays = context.resources.getStringArray(R.array.MonthsShort)
        val list: java.util.ArrayList<String> = java.util.ArrayList()
        for (i in arrWeekDays) {
            list.add(i.toString())
        }
        if (strNumber != null && !strNumber.isEmpty()) {
            if (strNumber.contains("jan")) strNumber = strNumber.replace("jan".toRegex(), list[0])
            if (strNumber.contains("feb")) strNumber = strNumber.replace("feb".toRegex(), list[1])
            if (strNumber.contains("mar")) strNumber = strNumber.replace("mar".toRegex(), list[2])
            if (strNumber.contains("apr")) strNumber = strNumber.replace("apr".toRegex(), list[3])
            if (strNumber.contains("may")) strNumber = strNumber.replace("may".toRegex(), list[4])
            if (strNumber.contains("jun")) strNumber = strNumber.replace("jun".toRegex(), list[5])
            if (strNumber.contains("jul")) strNumber = strNumber.replace("jul".toRegex(), list[6])
            if (strNumber.contains("aug")) strNumber = strNumber.replace("aug".toRegex(), list[7])
            if (strNumber.contains("sep")) strNumber = strNumber.replace("sep".toRegex(), list[8])
            if (strNumber.contains("oct")) strNumber = strNumber.replace("oct".toRegex(), list[9])
            if (strNumber.contains("nov")) strNumber = strNumber.replace("nov".toRegex(), list[10])
            if (strNumber.contains("dec")) strNumber = strNumber.replace("dec".toRegex(), list[11])
        } else {
            strNumber = ""
        }
        return strNumber
    }

    fun monthsEnglishToArabic(mMonth: String, context: Context): String {
        var strNumber = mMonth.lowercase()
        val arrWeekDays = context.resources.getStringArray(R.array.MonthsList)
        val list: ArrayList<String> = ArrayList()
        for (i in arrWeekDays) {
            list.add(i.toString())
        }
        if (strNumber.isNotEmpty()) {
            if (strNumber.contains("january")) strNumber =
                strNumber.replace("january".toRegex(), list[0])
            if (strNumber.contains("february")) strNumber =
                strNumber.replace("february".toRegex(), list[1])
            if (strNumber.contains("march")) strNumber =
                strNumber.replace("march".toRegex(), list[2])
            if (strNumber.contains("april")) strNumber =
                strNumber.replace("april".toRegex(), list[3])
            if (strNumber.contains("may")) strNumber = strNumber.replace("may".toRegex(), list[4])
            if (strNumber.contains("june")) strNumber = strNumber.replace("june".toRegex(), list[5])
            if (strNumber.contains("july")) strNumber = strNumber.replace("july".toRegex(), list[6])
            if (strNumber.contains("august")) strNumber =
                strNumber.replace("august".toRegex(), list[7])
            if (strNumber.contains("september")) strNumber =
                strNumber.replace("september".toRegex(), list[8])
            if (strNumber.contains("october")) strNumber =
                strNumber.replace("october".toRegex(), list[9])
            if (strNumber.contains("november")) strNumber =
                strNumber.replace("november".toRegex(), list[10])
            if (strNumber.contains("december")) strNumber =
                strNumber.replace("december".toRegex(), list[11])
        } else {
            strNumber = ""
        }
        strNumber = monthListEnglishToArabic(strNumber, context)
        return amPmEnglishToArabic(strNumber, context)
    }

    private fun monthListEnglishToArabic(mMonth: String, context: Context): String {
        var strMonth = mMonth.lowercase()
        val arrWeekDays = context.resources.getStringArray(R.array.MonthsList)
        val list: ArrayList<String> = ArrayList()
        for (i in arrWeekDays) {
            list.add(i.toString())
        }
        if (strMonth.isNotEmpty()) {
            if (strMonth.contains("jan")) strMonth = strMonth.replace("jan", list[0])
            if (strMonth.contains("feb")) strMonth = strMonth.replace("feb", list[1])
            if (strMonth.contains("mar")) strMonth = strMonth.replace("mar", list[2])
            if (strMonth.contains("apr")) strMonth = strMonth.replace("apr", list[3])
            if (strMonth.contains("may")) strMonth = strMonth.replace("may", list[4])
            if (strMonth.contains("jun")) strMonth = strMonth.replace("jun", list[5])
            if (strMonth.contains("jul")) strMonth = strMonth.replace("jul", list[6])
            if (strMonth.contains("aug")) strMonth = strMonth.replace("aug", list[7])
            if (strMonth.contains("sep")) strMonth = strMonth.replace("sep", list[8])
            if (strMonth.contains("oct")) strMonth = strMonth.replace("oct", list[9])
            if (strMonth.contains("nov")) strMonth = strMonth.replace("nov", list[10])
            if (strMonth.contains("dec")) strMonth = strMonth.replace("dec", list[11])
        } else {
            strMonth = ""
        }
        return strMonth
    }

    private fun amPmEnglishToArabic(strValue: String, context: Context): String {
        var value = strValue
        context.apply {
            // if (!isEnglishLanguage()) {
            if (value.contains(" pm")) value = value.replace(" pm".toRegex(), " م ")
            if (value.contains(" am")) value = value.replace(" am".toRegex(), " صباحا ")
//            } else {
//                if (value.contains(" pm")) value = value.replace(" pm".toRegex(), " PM")
//                if (value.contains(" am")) value = value.replace(" am".toRegex(), " AM")
//            }
        }
        return value
    }

    private fun currentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    @SuppressLint("NewApi")
    fun getFormattedDate(
        inputFormat: String,
        outputFormat: String,
        value: String,
        zone: String,
        context: Context,
    ): String {
        var newDateStr = ""
        var localTime = ""
        localTime = if (zone == Constants.NO_ZONE) {
            value
        } else {
            getTimeConvertToUTC(value, inputFormat, zone)
        }
        if (localTime.isNotEmpty()) {
            val inputFormatter = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            val dateObj: Date?
            try {
                dateObj = inputFormatter.parse(localTime)
                val outputFormatter = SimpleDateFormat(outputFormat, Locale.ENGLISH)
//                newDateStr = monthListEnglishToArabic(dateObj?.let { outputFormatter.format(it) }.toString(), context)
                newDateStr = outputFormatter.format(dateObj!!)
            } catch (e: ParseException) {
                newDateStr = localTime
            }
        }
        return newDateStr
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeConvertToUTC(aDate: String, inputFormat: String, zone: String): String {
        var finalResult = ""
        if (zone == Constants.ZONE_KUWAIT) {
            try {
                val formatter = DateTimeFormatter.ofPattern(inputFormat, Locale.ENGLISH)
                finalResult = LocalDateTime.parse(aDate, formatter)
                    .atOffset(getKuwaitZoneOffSet())
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter)
            } catch (e: Exception) {
                //e.printStackTrace()
                val inputFormatter = SimpleDateFormat(inputFormat, Locale.ENGLISH)
                inputFormatter.timeZone = TimeZone.getTimeZone("Asia/Kuwait")
                val dateObj: Date?
                try {
                    dateObj = inputFormatter.parse(aDate)
                    inputFormatter.timeZone = TimeZone.getDefault()
                    finalResult = inputFormatter.format(dateObj!!)
                } catch (e: ParseException) {
                    //e.printStackTrace()
                    finalResult = aDate
                }
            }
        } else {
            try {
                val formatter = DateTimeFormatter.ofPattern(inputFormat, Locale.ENGLISH)
                finalResult = LocalDateTime.parse(aDate, formatter)
                    .atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter)
            } catch (e: Exception) {
                //e.printStackTrace()
                val inputFormatter = SimpleDateFormat(inputFormat, Locale.ENGLISH)
                inputFormatter.timeZone = TimeZone.getTimeZone("UTC")
                val dateObj: Date?
                try {
                    dateObj = inputFormatter.parse(aDate)
                    inputFormatter.timeZone = TimeZone.getDefault()
                    finalResult = inputFormatter.format(dateObj!!)
                } catch (e: ParseException) {
                    e.printStackTrace()
                    finalResult = aDate
                }
            }
        }
        return finalResult
    }


    fun getDurationBetweenCurrentDateTime(
        activity: Activity,
        inputFormat: String,
        strDate: String
    ): String {
        var duration: String = ""
        val ONE_SECOND = 1000
        val ONE_MINUTE = ONE_SECOND * 60
        val ONE_HOUR = ONE_MINUTE * 60
        val ONE_DAY = ONE_HOUR * 24
        val ONE_WEEK = 7
        val ONE_MONTH = 30
        val ONE_YEAR = 12

        val SECONDS = 60
        val MINUTES = 60
        val HOURS = 24

        val givenDate = getLocalTimeZoneDate(inputFormat, strDate)
        val currentDate: Date = Calendar.getInstance().time
        val diff: Long = currentDate.time - givenDate!!.time
        val diffSeconds = diff / 1000 % 60
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000)
        val diffInDays = ((currentDate.time - givenDate.time) / (1000 * 60 * 60 * 24)).toInt()
        val diffInWeeks = diffInDays / 7
        val diffInMonth = diffInDays / 30
        val diffInYears = diffInMonth / 12
        when {
            diff in ONE_SECOND..ONE_MINUTE -> {
                duration = activity.resources.getQuantityString(
                    R.plurals.seconds,
                    diffSeconds.toInt(),
                    diffSeconds.toString()
                )
            }
            diff in ONE_MINUTE..ONE_HOUR -> {
                duration = activity.resources.getQuantityString(
                    R.plurals.minutes,
                    diffMinutes.toInt(),
                    diffMinutes.toString()
                )
            }
            diff in ONE_HOUR..ONE_DAY -> {
                duration = activity.resources.getQuantityString(
                    R.plurals.hours,
                    diffHours.toInt(),
                    diffHours.toString()
                )
            }
            diffInDays in 1..ONE_WEEK -> {
                duration = activity.resources.getQuantityString(
                    R.plurals.days,
                    diffInDays,
                    diffInDays.toString()
                )
            }
            diffInDays in 1..ONE_MONTH -> {
                duration = activity.resources.getQuantityString(
                    R.plurals.week,
                    diffInWeeks,
                    diffInWeeks.toString()
                )
            }
            diffInMonth in 1..ONE_YEAR -> {
                duration = activity.resources.getQuantityString(
                    R.plurals.month,
                    diffInMonth,
                    diffInMonth.toString()
                )
            }
            else -> {
                duration = activity.resources.getQuantityString(
                    R.plurals.years,
                    diffInYears,
                    diffInYears.toString()
                )
            }

        }
        return duration
    }

    fun getLocalTimeZoneDate(inputFormat: String, strDate: String): Date? {
        val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("kuwait")
        return simpleDateFormat.parse(strDate)
    }

    fun getWebViewData(strData: String): String {
        val strHead: String
        val strHtmlData: String
        strHead = "<head><style>@font-face {font-family: 'poppins_regular';src: url('file:///android_asset/poppins_regular.ttf');}body {font-family: 'poppins_regular';text-align:left;font-size:14px}</style></head>"
        strHtmlData =
            "<html>$strHead<body style=\"font-family: poppins_regular\">${
                strData.replace(
                    "font-family",
                    ""
                )
            }</body></html>"
        return strHtmlData
    }

    fun ImageView.loadImagesUsingCoil(
        strUrl: String?,
        errorImage: Int? = null,
    ) {
        this.load(strUrl) {
            crossfade(true)
            if (errorImage != null) {
                error(errorImage)
            }
            allowConversionToBitmap(true)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            allowHardware(true)
            listener(object : ImageRequest.Listener {
                override fun onError(request: ImageRequest, result: ErrorResult) {
                }
            })
        }
    }


    fun ImageView.loadGifUsingCoil(
        context: Activity,
        gif: Int
    ) {
        val imageLoader = ImageLoader.Builder(context).components(fun ComponentRegistry.Builder.() {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }).build()
        val request = ImageRequest.Builder(context)
            //.data(R.drawable.gif_loading_image)
            .data(gif)
            .target(this)
            .listener(object : ImageRequest.Listener {

            })
            .build()

        imageLoader.enqueue(request)
    }


    fun ImageView.loadGifUsingCoil(
        context: Context,
        gif: Int
    ) {
        val imageLoader = ImageLoader.Builder(context).components(fun ComponentRegistry.Builder.() {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }).build()
        val request = ImageRequest.Builder(context)
            //.data(R.drawable.gif_loading_image)
            .data(gif)
            .target(this)
            .listener(object : ImageRequest.Listener {

            })
            .build()

        imageLoader.enqueue(request)
    }

    fun Activity.showSnackBar(strMsg: String) {
        try {
            val spanText = SpannableString(strMsg)
            val span = SpanMaker(spanText).textFont(strMsg, Constants.fontRegular, this)
            val proteinBar = Protein.builder()
            proteinBar.setActivity(this)
                .text(span.spanText)
                .backgroundColor(ContextCompat.getColor(this, R.color.primary_color))
                .textColor(ContextCompat.getColor(this, R.color.color_white))
                .textSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    (this.resources?.getDimension(com.intuit.ssp.R.dimen._10ssp)!!)
                )
                .build()
                .setDuration(2000)
                .show()
        }catch (e: Exception) {
            Toast.makeText(this, strMsg, Toast.LENGTH_SHORT).show()
        }

    }


    fun View?.showSnackBar(
        strMsg: String,
        showBelowStatus: Boolean = false,
        duration: Int = 2000, //pass this true where the theme is fullscreen
        gravity: Int = Gravity.TOP
    ) {

        if (this != null) {
            val snackbar = Snackbar.make(this, strMsg, Snackbar.LENGTH_LONG)
            val layoutParams = ActionBar.LayoutParams(snackbar.view.layoutParams)
            layoutParams.gravity = gravity
            snackbar.view.setPadding(0, 0, 0, 0)

            if (showBelowStatus) {//If true, adding top margin to show it below status bar,
                // because in fullscreen theme statusBar is overlapping snackBar
                var statusBarHeight = 0
                val resourceId: Int =
                    context.resources.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
                }
                layoutParams.topMargin = statusBarHeight
            }
            snackbar.view.layoutParams = layoutParams
            snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
            val context = snackbar.context
            val binding: LayoutSnackBarBinding = LayoutSnackBarBinding.inflate(
                LayoutInflater.from(context)
            )
            val spanText = SpannableString(strMsg)
            val span = SpanMaker(spanText).textFont(strMsg, Constants.fontRegular, context)
            binding.txtSnackBarText.text = span.spanText
            val snackBarLayout = snackbar.view as Snackbar.SnackbarLayout
            snackBarLayout.addView(binding.root)
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    context,
                    R.color.color_black
                )
            )
            snackbar.duration = duration
            snackbar.show()
        }
    }

    @SuppressLint("NewApi")
    fun getTypeFace(context: Context, fontStyle: String): Typeface {
        return when (fontStyle) {
            Constants.fontRegular -> context.resources.getFont(R.font.font_regular)
            Constants.fontBold -> context.resources.getFont(R.font.font_bold)
            Constants.fontMedium -> context.resources.getFont(R.font.font_medium)
            Constants.fontRegularRev -> context.resources.getFont(R.font.font_regular_rev)
            else -> context.resources.getFont(R.font.font_regular)
        }
    }

    fun ImageView.loadPhotoUsingCoil(
        strUrl: Int? = null,
        errorImage: Int? = null,
        spinnerView: LayoutCommonSpinnerBinding? = null,
    ) {
        spinnerView?.ivSpinner?.loadGifUsingCoil(this.context,R.drawable.ic_spinner)
        this.load(strUrl) {
            crossfade(false)
            if (errorImage != null) {
                error(errorImage)
            }
            allowConversionToBitmap(true)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            allowHardware(true)
            listener(object : ImageRequest.Listener {
                override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                    spinnerView?.root?.isVisible = false
                }

                override fun onError(request: ImageRequest, result: ErrorResult) {
                    spinnerView?.root?.isVisible = false
                }

                override fun onCancel(request: ImageRequest) {
                }

                override fun onStart(request: ImageRequest) {
                }
            })
        }
    }

    fun ImageView.loadPhotoUsingCoil(
        strUrl: Uri? = null,
        errorImage: Int? = null,
        spinnerView: LayoutCommonSpinnerBinding? = null,
    ) {
        spinnerView?.ivSpinner?.loadGifUsingCoil(this.context,R.drawable.ic_spinner)
        this.load(strUrl) {
            crossfade(false)
            if (errorImage != null) {
                error(errorImage)
            }
            allowConversionToBitmap(true)
            bitmapConfig(Bitmap.Config.ARGB_8888)
            allowHardware(true)
            listener(object : ImageRequest.Listener {
                override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                    spinnerView?.root?.isVisible = false
                }

                override fun onError(request: ImageRequest, result: ErrorResult) {
                    spinnerView?.root?.isVisible = false
                }

                override fun onCancel(request: ImageRequest) {
                }

                override fun onStart(request: ImageRequest) {
                }
            })
        }
    }



}
