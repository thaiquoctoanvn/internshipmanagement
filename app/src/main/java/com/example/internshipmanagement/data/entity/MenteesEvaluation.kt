package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenteesEvaluation(
    val id: String,
    val mentorId: String,
    val mentorName: String,
    val mentorAvatarUrl: String,
    val fromDate: String,
    val toDate: String,
    val evaluation: String,
    val behaviorMark: String,
    val knowledgeMark: String,
    val proactiveMark: String
)