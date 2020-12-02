package com.example.internshipmanagement.ui.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

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

    fun getMyAccountId() = sharedPref.getString("userId", "")
    fun getMyAccountType() = sharedPref.getString("type", "")
    fun getMyAvatarUrl() = sharedPref.getString("avatarUrl", "")

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
}