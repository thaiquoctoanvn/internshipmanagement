package com.example.internshipmanagement.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class LogInViewModel(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _isInfoValid = MutableLiveData<Boolean>()
    val isInfoValid: LiveData<Boolean>
        get() = _isInfoValid

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful


    // Xác thực đăng nhập với server
    fun logIn(userName: String, pwd: String) {
        if(userName.isEmpty() || pwd.isEmpty()) {
            _isInfoValid.value = false
        } else {
            viewModelScope.launch {
                try {
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
                        _isSuccessful.value = true
                    } else {
                        _isSuccessful.value = false
                    }
                    super.setIsLoadingValue(false)
                } catch (e: Exception) {
                    Log.d("###", "Error: ${e.message}")
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