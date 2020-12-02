package com.example.internshipmanagement.ui.applaunching

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class LaunchingViewModel(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful


    // Xác thực token với server nếu đã đăng nhập trước đó
    fun authenticateToken() {
        viewModelScope.launch {
            val userId = sharedPref.getString("userId", "")
            val token = sharedPref.getString("token", "")
            if (!userId.isNullOrEmpty() && !token.isNullOrEmpty()) {
                val res = userRepository.checkToken(userId, token).body()
                _isSuccessful.value = res != null
            } else {
                _isSuccessful.value = false
            }
        }
    }
}