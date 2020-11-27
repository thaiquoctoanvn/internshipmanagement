package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyMentor(
    val mentorId: String,
    val mentorName: String,
    val mentorNickName: String,
    val position: String,
    val avatarUrl: String
)