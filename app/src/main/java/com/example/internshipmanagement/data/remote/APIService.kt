package com.example.internshipmanagement.data.remote

import com.example.internshipmanagement.data.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {
    @FormUrlEncoded
    @POST("logIn.php")
    suspend fun logIn(
        @Field("user_name") userName: String,
        @Field("password") pwd: String
    ): Response<PersonalInfo>

    @FormUrlEncoded
    @POST("checkToken.php")
    suspend fun checkToken(
        @Field("user_id") userId: String,
        @Field("token") token: String
    ): Response<String>

    @FormUrlEncoded
    @POST("updateFCMId.php")
    suspend fun updateFCMId(
        @Field("user_id") userId: String,
        @Field("fcm_id") fcmId: String
    ): Response<String>

    @GET("logOut.php")
    suspend fun logOut(@Query("user_id") userId: String): Response<String>

    @GET("getUserProfile.php")
    suspend fun getUserProfile(@Query("user_id") userId: String): Response<UserProfile>

    @Multipart
    @POST("updateUserAvatar.php")
    suspend fun updateUserAvatar(
        @Part image: MultipartBody.Part,
        @Part("old_url") oldUrl: RequestBody,
        @Part("user_id") userId: RequestBody
    ): Response<String>

    @FormUrlEncoded
    @POST("updateUserProfile.php")
    suspend fun updateUserInfo(
        @Field("userId") userId: String,
        @Field("name") name: String,
        @Field("position") position: String,
        @Field("email") email: String
    ): Response<String>

    @GET("getMyMenteesForTaskReference.php")
    suspend fun getMyMenteesForTaskReference(@Query("mentor_id") mentorId: String):
            Response<MutableList<MyMentee>>

    @GET("getAllMentees.php")
    suspend fun getAllMentees(@Query("mentor_id") mentorId: String): Response<MutableList<MyMentee>>

    @POST("addNewTask.php")
    suspend fun addNewTask(@Body taskRequestBody: TaskRequestBody): Response<String>

    @GET("getMentorsTasks.php")
    suspend fun getMentorsTasks(@Query("mentor_id") mentorId: String):
            Response<MutableList<MentorsTask>>

    @GET("getReferencesOfTask.php")
    suspend fun getReferencesOfTask(@Query("task_id") taskId: String):
            Response<MutableList<TaskReference>>

    @GET("getMenteesTasks.php")
    suspend fun getMenteesTasks(@Query("mentee_id") menteeId: String):
            Response<MutableList<MenteesTask>>

    @GET("getMyMenteesForTaskReference.php")
    suspend fun getMyMentees(@Query("mentor_id") mentorId: String): Response<MutableList<MyMentee>>

    @GET("searchAllUsers.php")
    suspend fun searchAllUsers(@Query("key") key: String): Response<MutableList<UserProfile>>

    @GET("getMenteesEvaluations.php")
    suspend fun getMenteesEvaluations(@Query("user_id") userId: String):
            Response<MutableList<MenteesEvaluation>>

    @GET("isMyMentee.php")
    suspend fun isMyMentee(
        @Query("user_id") userId: String,
        @Query("my_id") myId: String
    ): Response<String>

    @FormUrlEncoded
    @POST("addEvaluation.php")
    suspend fun addEvaluation(
        @Field("mentor_id") mentorId: String,
        @Field("mentee_id") menteeId: String,
        @Field("from_date") fromDate: String,
        @Field("to_date") toDate: String,
        @Field("evaluation") evaluation: String,
        @Field("behavior_mark") mark1: String,
        @Field("knowledge_mark") mark2: String,
        @Field("proactive_mark") mark3: String
    ): Response<String>

    // Lấy chi tiết bài nộp của mentee
    @GET("getSpecificReferenceOfTask.php")
    suspend fun getDetailReference(@Query("reference_id") referenceId: String):
            Response<DetailTaskReference>

    @FormUrlEncoded
    @POST("updateSpecificReferenceOfTask.php")
    suspend fun updateSpecificReferenceOfTask(
        @Field("refer_id") referenceId: String,
        @Field("mark") mark: String,
        @Field("comment") comment: String
    ): Response<String>

    @GET("getMenteeTaskDetail.php")
    suspend fun getMenteeTaskDetail(
        @Query("taskId") taskId: String,
        @Query("menteeId") menteeId: String
    ): Response<MenteeTaskDetail>

    @Multipart
    @POST("uploadTaskMaterials.php")
    suspend fun uploadTaskMaterials(
        @Part materials: MutableList<MultipartBody.Part>,
        @Part("refer_id") referId: RequestBody,
        @Part("note") taskNote: RequestBody,
        @Part("image_number")  materialsSize: RequestBody
    ): Response<String>

    @GET("getMyMentor.php")
    suspend fun getMyMentor(@Query("mentee_id") menteeId: String): Response<MutableList<MyMentor>>

    @GET("getCriteriaPoints.php")
    suspend fun getCriteriaPoints(@Query("mentee_id") menteeId: String): Response<MutableList<CriterionPoint>>

    @GET("getTaskPoints.php")
    suspend fun getTaskPoints(@Query("mentee_id") menteeId: String): Response<MutableList<TaskPoint>>

    @GET("getUsersNotifications.php")
    suspend fun getUsersNotifications(@Query("to_id") toId: String): Response<MutableList<Notification>>

    @GET("getDayEvents.php")
    suspend fun getDayEvents(
        @Query("requested_by") requestedBy: String,
        @Query("millisecond") time: String,
        @Query("mentor_id") mentorId: String,
        @Query("mentee_id") menteeId: String
    ): Response<MutableList<DayEvent>>

    @GET("getMonthEvents.php")
    suspend fun getMonthEvents(
        @Query("requested_by") requestedBy: String,
        @Query("month_year") time: String,
        @Query("mentor_id") mentorId: String,
        @Query("mentee_id") menteeId: String
    ): Response<MutableList<String>>

    @FormUrlEncoded
    @POST("addToMyMentee.php")
    suspend fun addToMyMentee(
        @Field("mentor_id") mentorId: String,
        @Field("mentee_id") menteeId: String
    ): Response<MyMentee>
}