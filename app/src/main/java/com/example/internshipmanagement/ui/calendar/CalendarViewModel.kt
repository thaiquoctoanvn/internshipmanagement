package com.example.internshipmanagement.ui.calendar

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.DayEvent
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _dayEvents = MutableLiveData<MutableList<DayEvent>>()
    val dayEvents: LiveData<MutableList<DayEvent>>
        get() = _dayEvents


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

    fun getMyAccountType() = sharedPref.getString("type", "")

}