package com.example.internshipmanagement.data.repository

import com.example.internshipmanagement.data.entity.MenteesTask
import com.example.internshipmanagement.data.remote.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MenteeRepository(private val apiService: APIService) {

    suspend fun getMenteesTasks(menteeId: String): Response<MutableList<MenteesTask>> {
        var response: Response<MutableList<MenteesTask>>
        withContext(Dispatchers.IO) {
            response = apiService.getMenteesTasks(menteeId)
        }
        return response
    }
}