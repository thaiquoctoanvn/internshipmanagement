package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DayEvent(
    val taskId: String,
    val taskContent: String,
    val deadline: String,
    val taskOwner: String,
    val ownerAvatar: String
)