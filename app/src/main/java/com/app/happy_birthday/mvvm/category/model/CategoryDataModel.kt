package com.app.happy_birthday.mvvm.category.model

import android.net.Uri
import com.app.happy_birthday.helper.helper_model.ValentineDay
import java.io.Serializable

data class CategoryDataModel(
    var image :Int ?= null,
    var imageUri : Uri ?= null,
    var position :Int ?= null,
    var day: ValentineDay = ValentineDay.NONE
): Serializable
