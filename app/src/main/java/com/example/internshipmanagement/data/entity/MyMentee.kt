package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyMentee(
    val menteeId: String,
    val menteeName: String,
    val menteeNickName: String,
    val menteePosition: String,
    var isReferred: String,
    val avatarUrl: String
)