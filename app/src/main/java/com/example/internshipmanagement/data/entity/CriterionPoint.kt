package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CriterionPoint(
    val id: String,
    val mentorId: String,
    val behaviorMark: String,
    val knowledgeMark: String,
    val proactiveMark: String
)