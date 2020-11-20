package com.example.internshipmanagement.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import com.example.internshipmanagement.data.entity.Criterion
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
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

        fun getDateFromTimeMilliSecond(timeStamp: String): String {
            if(timeStamp.isNotEmpty()) {
                val date = Date(timeStamp.toLong())
                val language = "en"
                val formattedDateAsShortMonth = SimpleDateFormat("dd MMM yyyy HH:mm", Locale(language))
                return formattedDateAsShortMonth.format(date)
            }
            return ""
        }

        fun getMilliSecondFromDate(dateInString: String): Long? {
            if(dateInString.isNotEmpty()) {
                val language = "en"
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale(language))
                val date = simpleDateFormat.parse(dateInString)
                return date.time
            }
            return null
        }

        fun provideCriteria(): MutableList<Criterion> {
            return mutableListOf(
                Criterion("Behavior", "-1"),
                Criterion("Knowledge", "-1")
            )
        }

        fun provideMarkLevel(): MutableList<Int> {
            return mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        }
    }
}