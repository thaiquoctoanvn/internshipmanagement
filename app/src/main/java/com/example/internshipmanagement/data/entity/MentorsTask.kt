package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MentorsTask(
    val taskId: String,
    val ownerId: String,
    val content: String,
    val deadline: String,
    var isClosed: String,
    val referenceNumber: String,
    val numberSubmitted: String
)