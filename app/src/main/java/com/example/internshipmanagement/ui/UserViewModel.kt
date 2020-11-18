package com.example.internshipmanagement.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.PersonalInfo
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.example.internshipmanagement.util.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val isSucceed = MutableLiveData<Boolean>()
    private val userProfile = MutableLiveData<UserProfile>()
    private val searchResult = MutableLiveData<MutableList<UserProfile>>()

    fun getIsSucceedValue() = isSucceed
    fun getUserProfileValue() = userProfile
    fun getSearchResultValue() = searchResult

    fun getSharedPref() = sharedPref

    // Xác thực đăng nhập với server
    fun logIn(userName: String, pwd: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.logIn(userName, pwd)
            val personalInfo = res.body()
            // Lưu thông tin người dùng vào Pref
            if(personalInfo != null) {
                setUserInfoToPref(
                    personalInfo.userId,
                    personalInfo.token,
                    personalInfo.type,
                    personalInfo.avatarUrl
                )
                isSucceed.value = true
            } else {
                isSucceed.value = false
            }
            super.setIsLoadingValue(false)
        }
    }


    // Xác thực token với server nếu đã đăng nhập trước đó
    fun checkToken() {
        viewModelScope.launch {
            val userId = sharedPref.getString("userId", "")
            val token = sharedPref.getString("token", "")
            if (!userId.isNullOrEmpty() && !token.isNullOrEmpty()) {
                val res = userRepository.checkToken(userId, token).body()
                if(res != null) {
                    // isLogInSucceed.value = res == SUCCEED_MESSAGE
                    isSucceed.value = true
                }
            } else {
                isSucceed.value = false
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            val userId = sharedPref.getString("userId", "")
            val res = userRepository.logOut(userId!!).body()
            if(res != null) {
                sharedPref.edit().apply {
                    remove("userId")
                    remove("token")
                    remove("type")
                    remove("avatarUrl")
                }.apply()
                isSucceed.value = true
            } else {
                isSucceed.value = false
            }
        }
    }

    // Lấy thông tin người dùng theo id
    fun getUserProfile(userId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.getUserProfile(userId).body()
            if(res != null) {
                userProfile.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun updateAvatar(
        context: Context,
        any: Any,
        userName: String,
        oldAvatar: String,
        userId: String
    ) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.uploadImage(context, any, userName, oldAvatar, userId).body()
            super.setIsLoadingValue(false)
            if(res != null) {
                super.setMessageResponseValue(AVATAR_UPDATE_SUCCEED)
            } else {
                super.setMessageResponseValue(AVATAR_UPDATE_FAILED)
            }
        }
    }

    fun updateUserInfo(name: String, position: String, email: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val userId = sharedPref.getString("userId", "")
            val res = userRepository.updateUserInfo(userId!!, name, position, email).body()
            super.setIsLoadingValue(false)
            if(res != null) {
                super.setMessageResponseValue(INFO_UPDATE_SUCCEED)
                isSucceed.value = true
            } else {
                super.setMessageResponseValue(INFO_UPDATE_FAILED)
                isSucceed.value = false
            }
        }
    }

    fun registerFCM() {
        viewModelScope.launch {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
                if(!it.isSuccessful) {
                    Log.d("###", "Fetching FCM registration token failed: ${it.exception}")
                } else {
                    updateFCMId(it.result!!.toString())
                    Log.d("###", "RegisterToken: ${it.result}")
                    // Cập nhật fcmId lên db
                }
            })
        }
    }

    suspend fun searchAllUsers(key: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.searchAllUsers(key).body()
            if(res != null) {
                searchResult.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    private fun updateFCMId(fcmId: String) {
        viewModelScope.launch {
            val userId = sharedPref.getString("userId", "")
            if(!userId.isNullOrEmpty()) {
                val res = userRepository.updateFCMId(userId, fcmId).body()
                if(res != null) {
                    Log.d("###", "Update FCM OK")
                }
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