package com.example.internshipmanagement.data.entity

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class MyMentee(
    val menteeId: String,
    val menteeName: String,
    val menteeNickName: String,
    val menteePosition: String,
    var isReferred: String,
    val avatarUrl: String,
    val gcmId: String
) : Parcelable