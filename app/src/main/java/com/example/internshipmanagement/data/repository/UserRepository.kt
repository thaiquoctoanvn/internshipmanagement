package com.example.internshipmanagement.data.repository

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.internshipmanagement.data.entity.PersonalInfo
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.data.remote.APIService
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class UserRepository(private val apiService: APIService) {

    suspend fun logIn(userName: String, pwd: String): PersonalInfo = apiService.logIn(userName, pwd)
    suspend fun checkToken(userId: String, token: String): String {
        var state = "ERROR"
        withContext(Dispatchers.IO) {
            state = apiService.checkToken(userId, token)
        }
        return state
    }

    suspend fun getUserProfile(userId: String): UserProfile {
        var userProfile: UserProfile
        withContext(Dispatchers.IO) {
            userProfile = apiService.getUserProfile(userId)
        }
        println(userProfile)
        return userProfile
    }

    suspend fun uploadImage(context: Context, any: Any, userName: String, oldAvatar: String, userId: String): String {
        var file: File? = null
        if(any is Uri) {
            file = File(FunctionHelper.getPathFromUri(context, any))
        } else if(any is Bitmap) {
            file = File(FunctionHelper.getPathFromBitmap(context, any))
        }

        val fileName = "$userName${System.currentTimeMillis()}${Random.nextInt(1000, 9999)}.jpg"
        println("FileName: $fileName")
        val oldAvatarUrlRequest = RequestBody.create(MediaType.parse("text/plain"), oldAvatar)
        val userIdRequest = RequestBody.create(MediaType.parse("text/plain"), userId)
        val requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val imageFile = MultipartBody.Part.createFormData("upload_file", fileName, requestBodyImage)
        return apiService.updateUserAvatar(imageFile, oldAvatarUrlRequest, userIdRequest)
    }

    suspend fun updateUserInfo(name: String, position: String, email: String): String {
        return apiService.updateUserInfo(name, position, email)
    }
}