package com.example.internshipmanagement.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.example.internshipmanagement.util.*
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository, private val sharedPref: SharedPreferences) : BaseViewModel() {

    private val isLogInSucceed = MutableLiveData<Boolean>()
    private val userProfile = MutableLiveData<UserProfile>()

    fun getIsSucceedValue() = isLogInSucceed
    fun getUserProfileValue() = userProfile

    fun getSharedPref() = sharedPref

    // Xác thực đăng nhập với server
    fun logIn(userName: String, pwd: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val personalInfo = userRepository.logIn(userName, pwd)
            // Lưu thông tin người dùng vào Pref
            if(personalInfo != null) {
                setUserInfoToPref(
                    personalInfo.userId,
                    personalInfo.token,
                    personalInfo.type,
                    personalInfo.avatarUrl
                )
                isLogInSucceed.value = true
            }
            super.setIsLoadingValue(false)
        }
    }


    // Xác thực token với server nếu đã đăng nhập trước đó
    fun checkToken() {
        viewModelScope.launch {
            val userId = sharedPref.getString("userId", "")
            val token = sharedPref.getString("token", "")
            if (userId != null && token != null) {
                val result = userRepository.checkToken(userId, token)
                if(result == SUCCEED_MESSAGE) {
                    isLogInSucceed.value = true
                }
            }
        }
    }

    // Lấy thông tin người dùng theo id
    fun getUserProfile(userId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            userProfile.value = userRepository.getUserProfile(userId)
            super.setIsLoadingValue(false)
        }
    }

    fun updateAvatar(context: Context, any: Any, userName: String, oldAvatar: String, userId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.uploadImage(context, any, userName, oldAvatar, userId)
            super.setIsLoadingValue(false)
            if(res == ERROR_MESSAGE) {
                super.setMessageResponseValue(AVATAR_UPDATE_FAILED)
            } else {
                sharedPref.edit().apply {
                    putString("avatarUrl", res.trim())
                    apply()
                }
            }
        }
    }

    fun updateUserInfo(name: String, position: String, email: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.updateUserInfo(name, position, email)
            super.setIsLoadingValue(false)
            if(res == ERROR_MESSAGE) {
                super.setMessageResponseValue(INFO_UPDATE_FAILED)
            } else {
                refreshInfoAfterEditing(name, position, email)
                super.setMessageResponseValue(INFO_UPDATE_SUCCEED)
            }
        }
    }

    private fun refreshInfoAfterEditing(updatedName: String, updatedPosition: String, updatedEmail: String) {
        viewModelScope.launch {
            val updatedAvatarUrl = sharedPref.getString("avatarUrl", "")
            if(updatedAvatarUrl != null) {
                val updatedInfo = userProfile.value?.copy(name = updatedName, role = updatedPosition, avatarUrl = updatedAvatarUrl)
                userProfile.value = updatedInfo
            }
        }
    }

    private fun setUserInfoToPref(
        userId: String,
        token: String,
        type: String,
        avatarUrl: String
    ) {
        sharedPref.edit().apply {
            putString("userId", userId)
            putString("token", token)
            putString("type", type)
            putString("avatarUrl", avatarUrl)
        }.apply()
    }

}