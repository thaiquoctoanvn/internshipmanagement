package com.example.internshipmanagement.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class FunctionHelper {
    companion object {
        // Copy file gốc ra một file tạm và trả về link file tạm
        fun getPathFromUri(context: Context, uri: Uri): String {
            val id = DocumentsContract.getDocumentId(uri)
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir.absolutePath + "/" + id)
            val filePath = file.absolutePath
            if (inputStream != null) {
                val outputStream = FileOutputStream(file)
                val buf = ByteArray(1024)
                var len: Int
                while (inputStream.read(buf).also { len = it } > 0) {
                    outputStream.write(buf, 0, len)
                }
            }
            return filePath
        }

        fun getPathFromBitmap(context: Context, bitmap: Bitmap): String {
            val id = "${System.currentTimeMillis()}${Random(100)}"
            val file = File("${context.cacheDir.absolutePath}/$id")
            val filePath = file.absolutePath
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return filePath
        }
    }
}