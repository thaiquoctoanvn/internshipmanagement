package com.example.internshipmanagement.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailTaskReference(
    val referenceId: String,
    val menteeId: String,
    val menteeName: String,
    val menteeNickName: String,
    val menteeAvatar: String,
    val isSubmitted: String,
    val isReviewed: String,
    val dateModified: String,
    val comment: String,
    val mark: String,
    val materials: MutableList<String>
)