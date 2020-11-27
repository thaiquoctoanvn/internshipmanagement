package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Notification(
    val id: String,
    val imageUrl: String,
    val type: String,
    val body: String,
    val idContent: String,
    val createdAt: String
)