package com.example.internshipmanagement.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskRequestBody(
    @field:Json(name = "ownerId")
    val mentorId: String,
    @field:Json(name = "deadline")
    val deadline: String,
    @field:Json(name = "content")
    val taskDecryption: String,
    @field:Json(name = "menteeIds")
    val menteeIds: MutableList<String>,
    @field:Json(name = "gcmIds")
    val gcmIds: MutableList<String>
)