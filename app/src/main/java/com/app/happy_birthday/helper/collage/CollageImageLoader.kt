package com.app.happy_birthday.helper.collage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import coil.size.Scale

/** Loads picked photos at about the size of their slot, so large photos fit in memory. */
object CollageImageLoader {

    /** @return the photo, or null if it can no longer be read */
    suspend fun load(context: Context, uri: Uri, width: Int, height: Int): Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(uri)
            .size(width, height)
            .scale(Scale.FILL)
            .precision(Precision.INEXACT)
            // The renderer reads these pixels on a software canvas.
            .allowHardware(false)
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .build()

        val result = context.imageLoader.execute(request)
        return (result as? SuccessResult)?.drawable?.toBitmap()
    }
}
