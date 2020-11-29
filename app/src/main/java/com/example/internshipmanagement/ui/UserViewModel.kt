package com.example.internshipmanagement.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.*
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.example.internshipmanagement.util.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile>
        get() = _userProfile

    private val _searchResult = MutableLiveData<MutableList<UserProfile>>()
    val searchResult: LiveData<MutableList<UserProfile>>
        get() = _searchResult

    private val _criteriaPoints = MutableLiveData<MutableList<CriterionPoint>>()
    val criteriaPoints: LiveData<MutableList<CriterionPoint>>
        get() = _criteriaPoints

    private val _notifications = MutableLiveData<MutableList<Notification>>()
    val notifications: LiveData<MutableList<Notification>>
        get() = _notifications

    private val _dayEvents = MutableLiveData<MutableList<DayEvent>>()
    val dayEvents: LiveData<MutableList<DayEvent>>
        get() = _dayEvents


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
                _isSuccessful.value = true
            } else {
                _isSuccessful.value = false
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
                    _isSuccessful.value = true
                }
            } else {
                _isSuccessful.value = false
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
                _isSuccessful.value = true
            } else {
                _isSuccessful.value = false
            }
        }
    }

    // Lấy thông tin người dùng theo id
    fun getUserProfile(userId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.getUserProfile(userId).body()
            if(res != null) {
                _userProfile.value = res
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
                delay(2000)
                _isSuccessful.value = true
            } else {
                super.setMessageResponseValue(INFO_UPDATE_FAILED)
                delay(2000)
                _isSuccessful.value = false
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
            val res = userRepository.searchAllUsers(key).body()
            if(res != null) {
                _searchResult.value = res
            }
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

    fun getCriteriaPoints() {
        viewModelScope.launch {
            val menteeId = userProfile.value?.userId.toString()
            val res = userRepository.getCriteriaPoints(menteeId).body()
            if(res != null) {
                _criteriaPoints.value = res
            }
        }
    }

    fun getUsersNotifications() {
        viewModelScope.launch {
            val toId = sharedPref.getString("userId", "")
            val res = userRepository.getUsersNotifications(toId.toString()).body()
            if(res != null) {
                res.sortByDescending { it.createdAt.toLong() }
                _notifications.value = res
            }
        }
    }

    fun getDayEvents(calendar: java.util.Calendar) {
        viewModelScope.launch {
            delay(2000)

            var mentorId = ""
            var menteeId = ""
            val requestedBy = sharedPref.getString("type", "").toString()
            if(requestedBy == "1") {
                mentorId = sharedPref.getString("userId", "").toString()
            } else {
                menteeId = sharedPref.getString("userId", "").toString()
            }
            val res = userRepository.getDayEvents(requestedBy, calendar.timeInMillis.toString(), mentorId, menteeId).body()
            if(res != null) {
                _dayEvents.value = res
            }
        }
    }

}