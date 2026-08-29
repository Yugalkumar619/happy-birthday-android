package com.app.happy_birthday.mvvm.wallpaper.model

import android.net.Uri
import java.io.Serializable

data class WallpaperDataModel(
    var image :Int ?= null,
    var imageUri : Uri ?= null,
    var position :Int ?= null
): Serializable
