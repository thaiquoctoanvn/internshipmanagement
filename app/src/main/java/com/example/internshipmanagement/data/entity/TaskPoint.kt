package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskPoint(
    val id: String,
    val taskId: String,
    val isSubmitted: String,
    val isReviewed: String,
    val mark: String
)