package com.example.internshipmanagement.data.repository

import com.example.internshipmanagement.data.remote.APIService

class MentorRepository(private val apiService: APIService) {
    suspend fun getMyMenteesForTaskReference(mentorId: String) = apiService.getMyMenteesForTaskReference(mentorId)
}