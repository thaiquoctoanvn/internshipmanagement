package com.example.internshipmanagement.data.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class UserProfile(
    val userId: String,
    val name: String,
    val nickName: String,
    val email: String,
    val type: String,
    val role: String,
    val avatarUrl: String
) : Parcelable