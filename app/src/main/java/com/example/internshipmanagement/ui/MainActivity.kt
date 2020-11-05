package com.example.internshipmanagement.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var activeFragment: Fragment

    private val dashboardFragment = DashboardFragment()
    private val calendarFragment = CalendarFragment()
    private val personalFragment = PersonalFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set theme theo loại tài khoản
//        theme.applyStyle(R.style.Theme_Theme_Mentor, true)
        launchFragment()
    }

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_main
    }

    override fun setViewOnEventListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.itemDashBoard -> switchFragment(activeFragment, dashboardFragment)
                R.id.itemEventCalendar -> switchFragment(activeFragment, calendarFragment)
                R.id.itemProfile -> switchFragment(activeFragment, personalFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(
                supportFragmentManager.findFragmentByTag("dashBoardFragment")!!.isVisible or
                supportFragmentManager.findFragmentByTag("calendarFragment")!!.isVisible or
                supportFragmentManager.findFragmentByTag("profileFragment")!!.isVisible
//            supportFragmentManager.findFragmentByTag("addNewTaskFragment")!!.isVisible
        ) {
            bottomNavigationView.visibility = View.VISIBLE
            Log.d("###", "add new task frag is visible")
        } else {
            bottomNavigationView.visibility = View.GONE
        }
    }

    private fun launchFragment() {
        activeFragment = dashboardFragment
        supportFragmentManager.beginTransaction().apply {
            add(R.id.frameLayout, dashboardFragment, "dashBoardFragment")
            add(R.id.frameLayout, calendarFragment, "calendarFragment").hide(calendarFragment)
            add(R.id.frameLayout, personalFragment, "profileFragment").hide(personalFragment)
        }.commit()
    }

    private fun switchFragment(current: Fragment, preparing: Fragment) {
        supportFragmentManager.beginTransaction().hide(current).show(preparing).commit()
        activeFragment = preparing
    }

    private fun selectTheme() {
        
    }
}