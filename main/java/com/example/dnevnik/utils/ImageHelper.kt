package com.example.dnevnik.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageHelper {

    private const val TAG = "ImageHelper"
    private const val IMAGE_DIR = "diary_images"
    fun copyImageToAppStorage(context: Context, uri: Uri): String? {
        return try {
            val imageDir = File(context.filesDir, IMAGE_DIR)
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "IMAGE_$timeStamp.jpg"
            val destinationFile = File(imageDir, fileName)
            copyFile(context.contentResolver, uri, destinationFile)
            destinationFile.absolutePath

        } catch (e: Exception) {
            Log.e(TAG, "Failed to copy image: ${e.message}")
            null
        }
    }
    private fun copyFile(contentResolver: ContentResolver, srcUri: Uri, dstFile: File) {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            inputStream = contentResolver.openInputStream(srcUri)
            outputStream = FileOutputStream(dstFile)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream!!.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            Log.d(TAG, "Image copied to: ${dstFile.absolutePath}")

        } catch (e: Exception) {
            Log.e(TAG, "Error copying file: ${e.message}")
            throw e
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }
    fun getImageUriFromPath(context: Context, imagePath: String?): Uri? {
        if (imagePath.isNullOrEmpty()) return null

        return try {
            val file = File(imagePath)
            if (file.exists()) {
                Uri.fromFile(file)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting URI from path: ${e.message}")
            null
        }
    }

    fun deleteImage(context: Context, imagePath: String?) {
        if (imagePath.isNullOrEmpty()) return

        try {
            val file = File(imagePath)
            if (file.exists()) {
                file.delete()
                Log.d(TAG, "Image deleted: $imagePath")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting image: ${e.message}")
        }
    }
}