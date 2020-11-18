package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskReference(
    val id: String,
    val menteeId: String,
    val name: String,
    val nickName: String,
    val avatarUrl: String,
    val isSubmitted: String,
    var isReviewed: String
)