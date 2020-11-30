package com.example.internshipmanagement.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.DayEvent
import com.example.internshipmanagement.ui.adapter.DayEventAdapter
import com.example.internshipmanagement.ui.base.BaseFragment
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarFragment : BaseFragment() {

    private lateinit var dayEventAdapter: DayEventAdapter

    private val userViewModel by viewModel<UserViewModel>()

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_calendar
    }

    override fun setViewOnEventListener() {
        calendarView.setOnMonthChangeListener { year, month ->
            val time = "$month | $year"
            tvGreeting.text = time
        }
        calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar?) {

            }

            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                Log.d("###", "Select: ${calendar?.month}")
                calendar?.let {
                    val date = java.util.Calendar.getInstance().apply {
                        set(it.year, it.month - 1, it.day, 0, 0, 0)
                    }
                    pbEventLoading.visibility = View.VISIBLE
                    tvNoEvent.visibility = View.GONE
                    rvCalendarEvent.visibility = View.GONE
                    userViewModel.getDayEvents(date)
                }
            }
        })
    }

    override fun setObserverFragment() {
        userViewModel.dayEvents.observe(viewLifecycleOwner, Observer {
            updateEventContainer(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCurrentMonth()
        setUpEventContainer()
    }

    private fun setCurrentMonth() {
        val current = "${calendarView.curMonth} | ${calendarView.curYear}"
        tvGreeting.text = current
        val calendar = java.util.Calendar.getInstance().apply {
            set(calendarView.curYear, calendarView.curMonth, calendarView.curDay, 0, 0, 0)
        }
        userViewModel.getDayEvents(calendar)
    }

    private fun updateEventContainer(dayEvents: MutableList<DayEvent>) {
        pbEventLoading.visibility = View.GONE
        if(dayEvents.size > 0) {
            tvNoEvent.visibility = View.GONE
            dayEventAdapter.submitList(dayEvents)
            rvCalendarEvent.visibility = View.VISIBLE
        } else {
            tvNoEvent.visibility = View.VISIBLE
        }
    }

    private fun setUpEventContainer() {
        if(!this::dayEventAdapter.isInitialized) {
            dayEventAdapter = DayEventAdapter(onItemEventClick)
        }
        rvCalendarEvent.adapter = dayEventAdapter
    }

    private val onItemEventClick: (event: DayEvent) -> Unit = {
        // Is mentor
        if(userViewModel.getMyAccountType() == "1") {
            val mentorTaskIntent = Intent(requireActivity(), MentorTaskDetailActivity::class.java).apply {
                putExtra("taskDeadline", it.deadline)
                putExtra("taskContent", it.taskContent)
                putExtra("taskId", it.taskId)
            }
            startActivity(mentorTaskIntent)
        } else {
            // Is mentee
            val menteeTaskIntent = Intent(requireActivity(), MenteeTaskDetailActivity::class.java).apply {
                putExtra("taskId", it.taskId)
            }
            startActivity(menteeTaskIntent)
        }
    }

}