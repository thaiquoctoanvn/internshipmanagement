package com.example.internshipmanagement.ui.notification

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.Notification
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _menteeNotifications = MutableLiveData<MutableList<Notification>>()
    val menteeNotifications: LiveData<MutableList<Notification>>
        get() = _menteeNotifications


    fun getMenteesNotifications() {
        viewModelScope.launch {
            val toId = sharedPref.getString("userId", "")
            val res = userRepository.getUsersNotifications(toId.toString()).body()
            if(res != null) {
                res.sortByDescending { it.createdAt.toLong() }
                _menteeNotifications.value = res
            }
        }
    }
}