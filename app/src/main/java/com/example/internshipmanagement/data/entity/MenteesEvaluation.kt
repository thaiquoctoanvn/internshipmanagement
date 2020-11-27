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
    val mark1: String,
    val mark2: String,
    val mark3: String
)