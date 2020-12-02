package com.example.internshipmanagement.ui.userprofile.personal

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class PersonalViewModel(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private val _personalProfile = MutableLiveData<UserProfile>()
    val personalProfile: LiveData<UserProfile>
        get() = _personalProfile


    fun getPersonalProfile() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.getUserProfile(getMyAccountId().toString()).body()
            if(res != null) {
                _personalProfile.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            val res = userRepository.logOut(getMyAccountId().toString()).body()
            if(res != null) {
                sharedPref.edit().apply {
                    remove("userId")
                    remove("token")
                    remove("type")
                    remove("avatarUrl")
                }.apply()
                _isSuccessful.value = true
            } else {
                _isSuccessful.value = false
            }
        }
    }

    fun getMyAccountId() = sharedPref.getString("userId", "")

    fun getMyAccountType() = sharedPref.getString("type", "")
}