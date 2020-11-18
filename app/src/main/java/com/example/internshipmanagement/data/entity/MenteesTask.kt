package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenteesTask(
    val id: String,
    val taskId: String,
    val content: String,
    val deadline: String,
    val isSubmitted: String,
    val isReviewed: String
)