package com.app.happy_birthday.helper

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object FileHelper {

    suspend fun saveImage(
        context: Context,
        imageUrl: String,
        title: String,
        listener: OnFileDownloadListener
    ) {
        try {
            val bitmap = withContext(Dispatchers.IO) {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            }

            val imageUri = saveImageToStorage(context, bitmap, title)

            if (imageUri != null) {
                listener.onDownloadCompleted(imageUri)
            } else {
                listener.onDownloadError()
            }
        } catch (e: Exception) {
            listener.onDownloadError()
            Log.e("saveFile", e.toString())
        }
    }

    private fun saveImageToStorage(context: Context, bitmap: Bitmap, title: String): Uri? {
        val resolver: ContentResolver = context.contentResolver
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$title.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }

        val imageUri = resolver.insert(imageCollection, imageDetails)

        imageUri?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream: OutputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
            }
        }

        return imageUri
    }

    fun getBitmapFromUrl(strUrl: String): Bitmap? {
        var bitmap: Bitmap? = null
        var strImage: String? = ""
        try {
            if (strUrl.contains("http://")) {
                strImage = strUrl.replace("http://", "https://")
            }
            val url = URL(strImage)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            val input: InputStream = connection.inputStream
            bitmap = BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            bitmap = null
        }
        return bitmap
    }
}

interface OnFileDownloadListener {
    fun onDownloadCompleted(filePath: Uri?)
    fun onDownloadError()
}