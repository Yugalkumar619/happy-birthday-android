package com.app.happy_birthday.helper.collage

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.app.happy_birthday.helper.Global.folderName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/** Saves collages to the gallery folder that the Saved screen reads. */
object CollageStorage {

    private const val JPEG_QUALITY = 92
    private const val MIME_TYPE = "image/jpeg"

    /** @return the saved image, or null if saving failed */
    suspend fun save(context: Context, bitmap: Bitmap): Uri? = withContext(Dispatchers.IO) {
        val fileName = "birthday_collage_${System.currentTimeMillis()}.jpg"
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveWithMediaStore(context, bitmap, fileName)
            } else {
                saveToPublicFolder(context, bitmap, fileName)
            }
        } catch (e: IOException) {
            null
        } catch (e: SecurityException) {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveWithMediaStore(context: Context, bitmap: Bitmap, fileName: String): Uri? {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE)
            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/$folderName")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: return null

        val written = try {
            resolver.openOutputStream(uri)?.use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
            } == true
        } catch (e: IOException) {
            false
        }

        if (!written) {
            resolver.delete(uri, null, null)
            return null
        }

        val published = ContentValues().apply { put(MediaStore.Images.Media.IS_PENDING, 0) }
        resolver.update(uri, published, null, null)
        return uri
    }

    /** Android 9 and below. The caller must hold WRITE_EXTERNAL_STORAGE. */
    @Suppress("DEPRECATION")
    private fun saveToPublicFolder(context: Context, bitmap: Bitmap, fileName: String): Uri? {
        val folder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            folderName
        )
        if (!folder.isDirectory && !folder.mkdirs()) return null

        val file = File(folder, fileName)
        val written = file.outputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
        }
        if (!written) {
            file.delete()
            return null
        }

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DATA, file.absolutePath)
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE)
        }
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri == null) file.delete()
        return uri
    }
}
