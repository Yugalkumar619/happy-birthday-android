package com.app.happy_birthday.helper

import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RingtoneHelper(private val context: Context) {

    suspend fun saveRawToRingtones(
        rawId: Int,
        displayName: String,
        type: Int,
        onPermissionNeeded: (() -> Unit)? = null,  // Called when user must grant permission
        onSuccess: (() -> Unit)? = null           // Called when ringtone is set
    ) = withContext(Dispatchers.IO) {
        try {
            // Step 1: Save file to MediaStore (always works)
            val input = context.resources.openRawResource(rawId)

            val values = ContentValues().apply {
                put(MediaStore.Audio.Media.DISPLAY_NAME, "$displayName.mp3")
                put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg")
                put(MediaStore.Audio.Media.RELATIVE_PATH, "Ringtones/RamBhajan")
                put(MediaStore.Audio.Media.IS_RINGTONE, type == RingtoneManager.TYPE_RINGTONE)
                put(MediaStore.Audio.Media.IS_ALARM, type == RingtoneManager.TYPE_ALARM)
                put(MediaStore.Audio.Media.IS_NOTIFICATION, type == RingtoneManager.TYPE_NOTIFICATION)
            }

            val uri = context.contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)!!
            context.contentResolver.openOutputStream(uri)?.use { output ->
                input.copyTo(output)
            }

            // Step 2: Try to set as default
            if (Settings.System.canWrite(context)) {
                // Permission already granted → set ringtone
                RingtoneManager.setActualDefaultRingtoneUri(context, type, uri)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "$displayName set successfully!", Toast.LENGTH_LONG).show()
                    onSuccess?.invoke()
                }
            } else {
                // Permission NOT granted → ask user
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Allow 'Modify system settings' to set as default ringtone",
                        Toast.LENGTH_LONG
                    ).show()
                    onPermissionNeeded?.invoke()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}