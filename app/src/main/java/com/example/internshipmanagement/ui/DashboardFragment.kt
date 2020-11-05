package com.example.internshipmanagement.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DashboardFragment : BaseFragment() {

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun setViewOnEventListener() {
        ibAddTask.setOnClickListener { switchToAddNewTaskActivity() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sayGreeting()
    }

    private fun sayGreeting() {
        val time = System.currentTimeMillis()
        val date = Date(time)
        val language = "en"
        val formattedDateAsShortMonth = SimpleDateFormat("dd MMM yyyy", Locale(language))
        tvDate.text = formattedDateAsShortMonth.format(date)
        when(TimeUnit.MILLISECONDS.toHours(time).toInt()) {
            in 4..10 -> {
                tvGreeting.text = getString(R.string.good_morning)
            }
            in 11..17 -> {
                tvGreeting.text = getString(R.string.good_afternoon)
            }
            else -> {
                tvGreeting.text = getString(R.string.good_evening)
            }
        }
    }

    private fun switchToAddNewTaskActivity() {
        startActivity(Intent(activity, AddNewTaskActivity::class.java))
    }
}