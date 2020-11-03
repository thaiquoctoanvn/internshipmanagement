package com.example.internshipmanagement.util

// Cần dùng địa chỉ IP, ko dùng localhost
const val SERVER_URL = "http://172.16.10.65:83/miniproject/"

// File name
const val SHARED_PREF_NAME = "InternManagement"

// Message
const val SUCCEED_MESSAGE = "OK"
const val ERROR_MESSAGE = "ERROR"
const val AVATAR_UPDATE_FAILED = "Avatar update error"
const val INFO_UPDATE_FAILED = "Info update error"
const val INFO_UPDATE_SUCCEED = "Info update succeed"

// Code
const val GALLERY_REQUEST_CODE = 98
const val CAMERA_REQUEST_CODE = 99

// Permission
const val READ_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
const val WRITE_TO_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
