package com.app.happy_birthday.helper

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.app.happy_birthday.helper.Extensions.printLog
import com.app.happy_birthday.helper.Global.folderName
import java.io.ByteArrayOutputStream


object UtilExtension {
    fun saveImageToGallery(context: Context, title: String, imageData: ByteArray?): String {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "$title.jpg")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore API with content resolver for Android 10 and above
            // Try to find existing image (to replace it)
            val fileName = "$title.jpg"
            val existingUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                findExistingImageUri(context, fileName)
            } else null

            values.put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, 1)
            uri =
                existingUri ?: context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            if (uri != null) {
                try {
                    val outputStream = context.contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        outputStream.write(imageData)
                        outputStream.close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    context.contentResolver.update(uri, values, null, null)
                }
            }
        } else {
            // For devices running Android 9 and below
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/$folderName/" + title + ".jpg"
            values.put(MediaStore.Images.Media.DATA, path)
            try {
                uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    try {
                        val outputStream = context.contentResolver.openOutputStream(uri)
                        if (outputStream != null) {
                            outputStream.write(imageData)
                            outputStream.close()

                            // ✅ Notify media scanner
                            MediaScannerConnection.scanFile(
                                context,
                                arrayOf(path),
                                arrayOf("image/jpeg"),
                                null
                            )
                            return "ok"
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }catch (exception: Exception){
                "Here is my exception:: ${exception.message}".printLog("OOOOOOOOOOO")
                return "StorageError"
            }

        }
        return "ok"
    }

    fun drawableToByteArray(context: Context, drawableId: Int): ByteArray? {
        val options = BitmapFactory.Options().apply {
            inScaled = false
        }
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableId,options)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    private fun findExistingImageUri(context: Context, fileName: String): Uri? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return null

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} = ? AND ${MediaStore.Images.Media.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(fileName, "Pictures/$folderName/")  // Note the trailing slash

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            }
        }
        return null
    }

    // NEW: Add this function
    fun bitmapToByteArray(bitmap: Bitmap, quality: Int = 100): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }


}

