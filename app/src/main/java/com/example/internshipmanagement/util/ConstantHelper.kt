package com.example.internshipmanagement.util

// Cần dùng địa chỉ IP, ko dùng localhost 10.0.1.224, 172.16.10.68
const val SERVER_URL = "http://10.0.1.244:83/miniproject/"

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

// Transformer Effect
const val MIN_SCALE = 0.75f
const val MIN_ALPHA = 0.5f
