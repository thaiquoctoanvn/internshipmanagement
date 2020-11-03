package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonalInfo(
    val userId: String,
    val token: String,
    val type: String,
    val avatarUrl: String
)