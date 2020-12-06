package com.example.internshipmanagement.ui.profileediting

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.example.internshipmanagement.util.AVATAR_UPDATE_FAILED
import com.example.internshipmanagement.util.AVATAR_UPDATE_SUCCEED
import com.example.internshipmanagement.util.INFO_UPDATE_FAILED
import com.example.internshipmanagement.util.INFO_UPDATE_SUCCEED
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileEditingViewModel(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {


    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private val _profileInfo = MutableLiveData<UserProfile>()
    val profileInfo: LiveData<UserProfile>
        get() = _profileInfo


    // Lấy thông tin người dùng
    fun getProfileInfo() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            sharedPref.getString("userId", "")?.let {
                val res = userRepository.getUserProfile(it).body()
                if(res != null) {
                    _profileInfo.value = res
                }
            }
            super.setIsLoadingValue(false)
        }
    }

    fun updateAvatar(
        context: Context,
        any: Any
    ) {
        viewModelScope.launch {

            profileInfo.value?.let {
                super.setIsLoadingValue(true)

                val userName = it.nickName
                val oldAvatar = it.avatarUrl
                val userId = it.userId

                val res = userRepository.uploadImage(context, any, userName, oldAvatar, userId).body()
                super.setIsLoadingValue(false)
                if(res != null) {
                    super.setMessageResponseValue(AVATAR_UPDATE_SUCCEED)
                } else {
                    super.setMessageResponseValue(AVATAR_UPDATE_FAILED)
                }
            }
        }
    }

    fun updateProfileInfo(name: String, position: String, email: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val userId = sharedPref.getString("userId", "")
            val res = userRepository.updateUserInfo(userId!!, name, position, email).body()
            super.setIsLoadingValue(false)
            if(res != null) {
                super.setMessageResponseValue(INFO_UPDATE_SUCCEED)
                delay(500)
                _isSuccessful.value = true
            } else {
                super.setMessageResponseValue(INFO_UPDATE_FAILED)
                delay(500)
                _isSuccessful.value = false
            }
        }
    }
}