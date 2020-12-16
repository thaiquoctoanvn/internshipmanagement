package com.example.internshipmanagement.ui.calendar

import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.DayEvent
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.haibin.calendarview.Calendar
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

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

    private val _monthEvents = MutableLiveData<MutableMap<String, Calendar>>()
    val monthEvents: LiveData<MutableMap<String, Calendar>> get() = _monthEvents

    fun getMonthEvents(month: Int, year: Int) {
        viewModelScope.launch {
            var mentorId = ""
            var menteeId = ""
            var monthYear = ""
            val requestedBy = sharedPref.getString("type", "").toString()
            if(requestedBy == "1") {
                mentorId = sharedPref.getString("userId", "").toString()
            } else {
                menteeId = sharedPref.getString("userId", "").toString()
            }
            monthYear = if(month < 10) {
                "/0$month/$year"
            } else {
                "/$month/$year"
            }
            val res = userRepository.getMonthEvents(requestedBy, monthYear, mentorId, menteeId).body()
            if(res != null) {
                _monthEvents.value = parseToHaibinCalendar(res)
            }
        }
    }

    private suspend fun parseToHaibinCalendar(events: MutableList<String>): MutableMap<String, Calendar> {
        return withContext(Dispatchers.IO) {
            val haibinSchemes = mutableMapOf<String, Calendar>()
            val schemeTheme = Color.parseColor("#1AA2F6")
            events.forEach {
                val calendar = java.util.Calendar.getInstance()
                calendar.time = Date(it.toLong())
                val haibinCalendar = Calendar()
                haibinCalendar.apply {
                    year = calendar[java.util.Calendar.YEAR]
                    month = calendar[java.util.Calendar.MONTH] + 1
                    day = calendar[java.util.Calendar.DAY_OF_MONTH]
                    scheme = "E"
                    schemeColor = schemeTheme
                    addScheme(Calendar.Scheme())
                }
                haibinSchemes[haibinCalendar.toString()] = haibinCalendar
            }
            haibinSchemes
        }
    }

    fun getMyAccountType() = sharedPref.getString("type", "")

}