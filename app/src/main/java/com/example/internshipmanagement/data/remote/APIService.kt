package com.example.internshipmanagement.data.remote

import com.example.internshipmanagement.data.entity.PersonalInfo
import com.example.internshipmanagement.data.entity.UserProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {
    @FormUrlEncoded
    @POST("logIn.php")
    suspend fun logIn(@Field("user_name") userName: String, @Field("password") pwd: String): PersonalInfo

    @FormUrlEncoded
    @POST("checkToken.php")
    suspend fun checkToken(@Field("user_id") userId: String, @Field("token") token: String): String

    @GET("getUserProfile.php")
    suspend fun getUserProfile(@Query("user_id") userId: String): UserProfile

    @Multipart
    @POST("updateUserAvatar.php")
    suspend fun updateUserAvatar(
        @Part image: MultipartBody.Part,
        @Part("old_url") oldUrl: RequestBody,
        @Part("user_id") userId: RequestBody
    ): String

    @FormUrlEncoded
    @POST("updateUserProfile.php")
    suspend fun updateUserInfo(
        @Field("name") name: String,
        @Field("position") position: String,
        @Field("email") email: String
    ): String
}