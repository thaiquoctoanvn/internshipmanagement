package com.example.internshipmanagement.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.internshipmanagement.data.entity.MenteeTaskDetail
import com.example.internshipmanagement.data.entity.MenteesTask
import com.example.internshipmanagement.data.entity.MyMentor
import com.example.internshipmanagement.data.remote.APIService
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import kotlin.random.Random

class MenteeRepository(private val apiService: APIService) {

    suspend fun getMenteesTasks(menteeId: String): Response<MutableList<MenteesTask>> {
        var response: Response<MutableList<MenteesTask>>
        withContext(Dispatchers.IO) {
            response = apiService.getMenteesTasks(menteeId)
        }
        return response
    }

    suspend fun getMenteeTaskDetail(referId: String): Response<MenteeTaskDetail> {
        var response: Response<MenteeTaskDetail>
        withContext(Dispatchers.IO) {
            response = apiService.getMenteeTaskDetail(referId)
        }
        return response
    }

    suspend fun uploadTaskMaterialsFromUri(
        context: Context,
        urls: MutableList<String>,
        userName: String,
        referId: String,
        taskNote: String
    ): Response<String> {
        var response: Response<String>
        withContext(Dispatchers.IO) {
            val materials = mutableListOf<MultipartBody.Part>()
            urls.forEachIndexed { index, uri ->
                val file = File(FunctionHelper.getPathFromUri(context, Uri.parse(uri)))
                val fileName = "$referId$userName${System.currentTimeMillis()}${Random.nextInt(1000, 9999)}.jpg"

                val requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                // Truyền thêm index vào name để khớp với trên server
                val imageFile = MultipartBody.Part.createFormData("upload_file$index", fileName, requestBodyImage)
                materials.add(imageFile)
            }
            val referIdRequest = RequestBody.create(MediaType.parse("text/plain"), referId)
            val taskNoteRequest = RequestBody.create(MediaType.parse("text/plain"), taskNote)
            val imageNumberRequest = RequestBody.create(MediaType.parse("text/plain"), materials.size.toString())

            response = apiService.uploadTaskMaterials(materials, referIdRequest, taskNoteRequest, imageNumberRequest)
        }
        return response
    }

    suspend fun uploadTaskMaterialsFromBitmap(
        context: Context,
        bitmap: Bitmap,
        userId: String,
        referId: String,
        taskNote: String
    ): Response<String> {
        var response: Response<String>
        withContext(Dispatchers.IO) {
            val materials = mutableListOf<MultipartBody.Part>()
            val file = File(FunctionHelper.getPathFromBitmap(context, bitmap))
            val fileName =
                "$referId$userId${System.currentTimeMillis()}${Random.nextInt(1000, 9999)}.jpg"

            val requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            // Truyền thêm index vào name để khớp với trên server
            val imageFile =
                MultipartBody.Part.createFormData("upload_file0", fileName, requestBodyImage)
            materials.add(imageFile)

            val referIdRequest = RequestBody.create(MediaType.parse("text/plain"), referId)
            val taskNoteRequest = RequestBody.create(MediaType.parse("text/plain"), taskNote)
            val imageNumberRequest =
                RequestBody.create(MediaType.parse("text/plain"), materials.size.toString())

            response = apiService.uploadTaskMaterials(
                materials,
                referIdRequest,
                taskNoteRequest,
                imageNumberRequest
            )
        }
        return response
    }

    suspend fun getMyMentor(menteeId: String): Response<MutableList<MyMentor>> {
        var response: Response<MutableList<MyMentor>>
        withContext(Dispatchers.IO) {
            response = apiService.getMyMentor(menteeId)
        }
        return response
    }
}