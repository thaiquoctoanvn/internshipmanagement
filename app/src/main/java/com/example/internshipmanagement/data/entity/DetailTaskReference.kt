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
    val content: String,
    val isReviewed: String,
    val deadline: String,
    val dateModified: String,
    var comment: String,
    val mark: String,
    val materials: MutableList<String>
)