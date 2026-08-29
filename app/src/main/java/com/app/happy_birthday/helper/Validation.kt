package com.app.happy_birthday.helper

import android.text.TextUtils
import android.util.Patterns

object Validation {

    fun isValidEmail(target: CharSequence): Boolean =
        !TextUtils.isEmpty(target.trim()) && Patterns.EMAIL_ADDRESS.matcher(target.trim()).matches()

    fun isValidCellPhone(number: String?): Boolean =
        Patterns.PHONE.matcher(number.toString()).matches()
}