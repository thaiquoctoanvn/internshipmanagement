package com.example.internshipmanagement.data.repository

import com.example.internshipmanagement.data.entity.*
import com.example.internshipmanagement.data.remote.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
        return apiService.addNewTask(
            TaskRequestBody(
                ownerId,
                deadline,
                taskBody,
                menteeIds,
                gcmIds
            )
        )
    }

    suspend fun getMentorsTasks(mentorId: String): Response<MutableList<MentorsTask>> =
        apiService.getMentorsTasks(mentorId)

    suspend fun getTaskReferences(taskId: String): Response<MutableList<TaskReference>> =
        apiService.getReferencesOfTask(taskId)

    suspend fun getAllMentees(mentorId: String): Response<MutableList<MyMentee>> =
        apiService.getAllMentees(mentorId)

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
        marks: MutableList<Criterion>
    ): Response<String> {
        return apiService.addEvaluation(
            mentorId,
            menteeId,
            fromDate,
            toDate,
            evaluation,
            marks[0].mark,
            marks[1].mark,
            marks[2].mark
        )
    }

    suspend fun getDetailReference(referenceId: String): Response<DetailTaskReference> {
        return apiService.getDetailReference(referenceId)
    }

    suspend fun updateSpecificReferenceOfTask(
        referenceId: String,
        mark: String,
        comment: String
    ): Response<String> {
        var response: Response<String>
        withContext(Dispatchers.IO) {
            response = apiService.updateSpecificReferenceOfTask(referenceId, mark, comment)
        }
        return response
    }

    suspend fun addToMyMentee(mentorId: String, menteeId: String): Response<MyMentee> {
        return withContext(Dispatchers.IO) {
            apiService.addToMyMentee(mentorId, menteeId)
        }
    }
}