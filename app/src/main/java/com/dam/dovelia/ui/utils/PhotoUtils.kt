package com.dam.dovelia.ui.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun uriToFile(context: Context, uri: Uri): File? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        return tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}