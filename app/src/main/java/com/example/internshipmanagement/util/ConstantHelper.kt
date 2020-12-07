package com.example.internshipmanagement.util

// Cần dùng địa chỉ IP, ko dùng localhost 10.0.1.244, 172.16.10.68
// Local host: http://172.16.10.68:83/miniproject/
// Online host: https://bracteolate-honks.000webhostapp.com/miniproject/
const val SERVER_URL = "https://bracteolate-honks.000webhostapp.com/miniproject/"

// File name
const val SHARED_PREF_NAME = "InternManagement"

// Message
const val SUCCEED_MESSAGE = "OK"
const val ERROR_MESSAGE = "ERROR"
const val AVATAR_UPDATE_FAILED = "Avatar update error"
const val AVATAR_UPDATE_SUCCEED = "Avatar update succeed"
const val INFO_UPDATE_FAILED = "Info update error"
const val INFO_UPDATE_SUCCEED = "Info update succeed"
const val ADD_TASK_SUCCESSFULLY = "Added"

// Code
const val GALLERY_REQUEST_CODE = 98
const val CAMERA_REQUEST_CODE = 99
const val CHANNEL_ID = "1752"
const val CHANNEL_NAME = "eden"
const val CHANNEL_DECRYPTION = "Channel was created"

// Permission
const val READ_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
const val WRITE_TO_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA

// Broadcast
const val REFERENCES_PUSH = "references"
const val FCM_PUSH = "gcm"
const val INFO_UPDATED = "infoUpdated"
const val SUBMISSION_PUSH = "submitted"
const val TASK_ADDING_PUSH = "task added"
const val TASK_REVIEWING_PUSH = "reviewed"

// Transformer Effect
const val MIN_SCALE = 0.75f
const val MIN_ALPHA = 0.5f

// Time delay
const val DURATION: Long = 500
