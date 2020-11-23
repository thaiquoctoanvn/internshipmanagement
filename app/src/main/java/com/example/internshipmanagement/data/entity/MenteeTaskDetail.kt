package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenteeTaskDetail(
    val id: String,
    val ownerId: String,
    val content: String,
    val deadline: String,
    val isSubmitted: String,
    val mark: String,
    val comment: String
)