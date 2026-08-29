package com.app.happy_birthday.helper

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.LayoutCustomAlertBinding
import com.app.happy_birthday.helper.Extensions.dismissIfShowing
import com.app.happy_birthday.helper.Global.getDimension
import com.app.happy_birthday.helper.interfaces.AlertDialogInterface
import com.app.happy_birthday.helper.interfaces.ImagePickerDialogInterface


object Dialogs {

    var customDialog: Dialog? = null
    var bottomSheetContinueShopping: Dialog? = null

    val dialogInterface: AlertDialogInterface = object : AlertDialogInterface {
        override fun onCloseDialog() {
            bottomSheetContinueShopping.dismissIfShowing()
        }
    }

    fun showCustomAlert(
        activity: Context,
        title: String = "",
        msg: String = "",
        img: Int = 0,
        yesBtn: String,
        noBtn: String,
        singleBtn: Boolean = false,
        isCancellable: Boolean? = true,
        reverseFont: Boolean? = false, //for change language alert
        alertDialogInterface: AlertDialogInterface,
    ) {
        try {
            customDialog = Dialog(activity)
            customDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            customDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val binding: LayoutCustomAlertBinding = LayoutCustomAlertBinding.inflate(
                LayoutInflater.from(activity)
            )
            customDialog?.setContentView(binding.root)
            val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
            lp.copyFrom(customDialog?.window?.attributes)
            lp.width = getDimension(activity as Activity, 300.00)
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            customDialog?.window?.attributes = lp
            customDialog?.setCanceledOnTouchOutside(isCancellable ?: true)
            customDialog?.setCancelable(isCancellable ?: true)
            if (reverseFont == true) {
                binding.txtAlertTitle.typeface = Global.getTypeFace(activity, Constants.fontRegularRev)
                binding.txtAlertMessage.typeface =
                    Global.getTypeFace(activity, Constants.fontRegularRev)
                binding.btnAlertNegative.typeface =
                    Global.getTypeFace(activity, Constants.fontRegularRev)
                binding.btnAlertPositive.typeface =
                    Global.getTypeFace(activity, Constants.fontRegularRev)
            }

            binding.txtAlertTitle.text = title
            binding.txtAlertMessage.text = msg
            binding.btnAlertNegative.text = noBtn
            binding.btnAlertPositive.text = yesBtn
            binding.ivAlert.isVisible = img != 0
            binding.ivAlert.setImageResource(img)

            binding.btnAlertNegative.visibility = if (singleBtn) View.GONE else View.VISIBLE
            binding.btnAlertNegative.setOnClickListener {
                customDialog?.dismiss()
                alertDialogInterface.onNoClick()
            }
            binding.btnAlertPositive.setOnClickListener {
                customDialog?.dismiss()
                alertDialogInterface.onYesClick()
            }
            customDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showImagePickerDialog(
        activity: Context,
        imagePickerDialogInterface: ImagePickerDialogInterface,
        isFileEnabled: Boolean = false
    ) {
        try {
            val dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val view = LayoutInflater.from(activity).inflate(R.layout.layout_image_picker, null)
            dialog.setContentView(view)
            val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window?.attributes)
            lp.width = getDimension(activity as Activity, 300.00)
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            dialog.window?.attributes = lp
            dialog.setCanceledOnTouchOutside(false)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val camera = view.findViewById(R.id.cvCamera) as CardView
            val gallery = view.findViewById(R.id.cvGallery) as CardView
            val btnCancel = view.findViewById(R.id.btnCancel) as TextView

            dialog.setCancelable(true)
            camera.setOnClickListener {
                dialog.dismiss()
                imagePickerDialogInterface.onClickCamera()
            }
            gallery.setOnClickListener {
                dialog.dismiss()
                imagePickerDialogInterface.onClickGallery()
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
