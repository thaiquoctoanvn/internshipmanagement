package com.example.internshipmanagement.data.repository

import com.example.internshipmanagement.data.entity.*
import com.example.internshipmanagement.data.remote.APIService
import retrofit2.Response

class MentorRepository(private val apiService: APIService) {
    suspend fun getMyMenteesForTaskReference(mentorId: String): Response<MutableList<MyMentee>> =
        apiService.getMyMenteesForTaskReference(mentorId)

    suspend fun addNewTask(
        ownerId: String,
        deadline: String,
        taskBody: String,
        menteeIds: MutableList<String>,
        gcmIds: MutableList<String>
    ): Response<String> {
        return apiService.addNewTask(TaskRequestBody(ownerId, deadline, taskBody, menteeIds, gcmIds))
    }

    suspend fun getMentorsTasks(mentorId: String): Response<MutableList<MentorsTask>> =
        apiService.getMentorsTasks(mentorId)

    suspend fun getTaskReferences(taskId: String): Response<MutableList<TaskReference>> =
        apiService.getReferencesOfTask(taskId)

    suspend fun getAllMentees(): Response<MutableList<MyMentee>> = apiService.getAllMentees()

    suspend fun getMyMentee(mentorId: String): Response<MutableList<MyMentee>> =
        apiService.getMyMentees(mentorId)

    suspend fun getMenteesEvaluations(userId: String): Response<MutableList<MenteesEvaluation>> =
        apiService.getMenteesEvaluations(userId)

    suspend fun isMyMentee(userId: String, myId: String): Response<String> =
        apiService.isMyMentee(userId, myId)

    suspend fun addEvaluation(
        mentorId: String,
        menteeId: String,
        fromDate: String,
        toDate: String,
        evaluation: String,
        mark: String
    ): Response<String> {
        return apiService.addEvaluation(mentorId, menteeId, fromDate, toDate, evaluation, mark)
    }

    suspend fun getDetailReference(referenceId: String): Response<DetailTaskReference> {
        return apiService.getDetailReference(referenceId)
    }
}