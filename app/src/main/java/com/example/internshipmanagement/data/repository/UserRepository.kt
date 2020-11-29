package com.example.internshipmanagement.data.repository

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.internshipmanagement.data.entity.*
import com.example.internshipmanagement.data.remote.APIService
import com.example.internshipmanagement.util.FCMHelper
import com.example.internshipmanagement.util.FunctionHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class UserRepository(private val apiService: APIService) {

    suspend fun logIn(userName: String, pwd: String): Response<PersonalInfo> = apiService.logIn(userName, pwd)
    suspend fun checkToken(userId: String, token: String): Response<String> {
        var response: Response<String>
        withContext(Dispatchers.IO) {
            response = apiService.checkToken(userId, token)
        }
        return response
    }

    suspend fun logOut(userId: String) = apiService.logOut(userId)

    suspend fun getUserProfile(userId: String): Response<UserProfile> {
        var response: Response<UserProfile>
        withContext(Dispatchers.IO) {
            response = apiService.getUserProfile(userId)
        }
        return response
    }

    suspend fun uploadImage(
        context: Context,
        any: Any,
        userName:
        String,
        oldAvatar: String,
        userId: String
    ): Response<String> {
        var response: Response<String>
        withContext(Dispatchers.IO) {
            var file: File? = null
            if(any is Uri) {
                file = File(FunctionHelper.getPathFromUri(context, any))
            } else if(any is Bitmap) {
                file = File(FunctionHelper.getPathFromBitmap(context, any))
            }

            val fileName = "$userName${System.currentTimeMillis()}${Random.nextInt(1000, 9999)}.jpg"
//        println("FileName: $fileName")
            val oldAvatarUrlRequest = RequestBody.create(MediaType.parse("text/plain"), oldAvatar)
            val userIdRequest = RequestBody.create(MediaType.parse("text/plain"), userId)
            val requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val imageFile = MultipartBody.Part.createFormData("upload_file", fileName, requestBodyImage)

            response = apiService.updateUserAvatar(imageFile, oldAvatarUrlRequest, userIdRequest)
        }

        return response
    }

    suspend fun updateUserInfo(
        userId: String,
        name: String,
        position: String,
        email: String
    ): Response<String> {
        var response: Response<String>
        withContext(Dispatchers.IO) {
            response = apiService.updateUserInfo(userId, name, position, email)
        }
        return response
    }

    suspend fun updateFCMId(userId: String, fcmId: String): Response<String> {
        var response: Response<String>
        withContext(Dispatchers.IO) {
            response = apiService.updateFCMId(userId, fcmId)
        }
        return response
    }

    suspend fun searchAllUsers(key: String): Response<MutableList<UserProfile>> {
        var response: Response<MutableList<UserProfile>>
        withContext(Dispatchers.IO) {
            response = apiService.searchAllUsers(key)
        }
        return response
    }

    suspend fun getCriteriaPoints(menteeId: String): Response<MutableList<CriterionPoint>> {
        var response: Response<MutableList<CriterionPoint>>
        withContext(Dispatchers.IO) {
            response = apiService.getCriteriaPoints(menteeId)
        }
        return response
    }

    suspend fun getUsersNotifications(toId: String): Response<MutableList<Notification>> {
        return withContext(Dispatchers.IO) {
            apiService.getUsersNotifications(toId)
        }
    }

    suspend fun getDayEvents(
        requestedBy: String,
        time: String,
        mentorId: String,
        menteeId: String
    ): Response<MutableList<DayEvent>> {
        return withContext(Dispatchers.IO) {
            apiService.getDayEvents(requestedBy, time, mentorId, menteeId)
        }
    }

}