package com.example.internshipmanagement.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.internshipmanagement.R
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DashboardFragment : BaseFragment() {

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun setViewOnEventListener() {

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
}