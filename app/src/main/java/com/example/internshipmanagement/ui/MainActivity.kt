package com.example.internshipmanagement.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.ui.calendar.CalendarFragment
import com.example.internshipmanagement.ui.dashboard.mentor.DashboardFragment
import com.example.internshipmanagement.ui.dashboard.mentee.MenteeDashBoardFragment
import com.example.internshipmanagement.ui.people.mentee.MenteeSearchFragment
import com.example.internshipmanagement.ui.people.SearchFragment
import com.example.internshipmanagement.ui.userprofile.personal.PersonalFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private lateinit var activeFragment: Fragment

    private lateinit var baseScreen1: Fragment
    private lateinit var baseScreen2: Fragment
    private lateinit var baseScreen3: Fragment
    private lateinit var baseScreen4: Fragment

    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var menteeDashBoardFragment: MenteeDashBoardFragment
    private lateinit var menteeSearchFragment: MenteeSearchFragment

    private lateinit var calendarFragment: CalendarFragment
    private lateinit var personalFragment: PersonalFragment

//    private val userViewModel by viewModel<UserViewModel>()
    private val mainViewModel by viewModel<MainViewModel>()

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
                R.id.itemDashBoard -> switchFragment(activeFragment, baseScreen1)
                R.id.itemSearchUser -> switchFragment(activeFragment, baseScreen2)
                R.id.itemEventCalendar -> switchFragment(activeFragment, baseScreen3)
                R.id.itemProfile -> switchFragment(activeFragment, baseScreen4)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun setObserver() {}


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
        if(super.isMentorAccount(mainViewModel.getMyAccountType().toString())) {
            dashboardFragment = DashboardFragment()
            searchFragment = SearchFragment()
            calendarFragment = CalendarFragment()
            personalFragment = PersonalFragment()
            baseScreen1 = dashboardFragment
            baseScreen2 = searchFragment
            baseScreen3 = calendarFragment
            baseScreen4 = personalFragment

            activeFragment = dashboardFragment
        } else {
            menteeDashBoardFragment = MenteeDashBoardFragment()
            menteeSearchFragment = MenteeSearchFragment()
            calendarFragment = CalendarFragment()
            personalFragment = PersonalFragment()
            baseScreen1 = menteeDashBoardFragment
            baseScreen2 = menteeSearchFragment
            baseScreen3 = calendarFragment
            baseScreen4 = personalFragment

            activeFragment = menteeDashBoardFragment
        }
        supportFragmentManager.beginTransaction().apply {
            add(R.id.frameLayout, baseScreen1, "baseScreen1")
            add(R.id.frameLayout, baseScreen2, "baseScreen2").hide(baseScreen2)
            add(R.id.frameLayout, baseScreen3, "baseScreen3").hide(baseScreen3)
            add(R.id.frameLayout, baseScreen4, "baseScreen4").hide(baseScreen4)
        }.commit()

        mainViewModel.registerFCM()
    }

    private fun switchFragment(current: Fragment, preparing: Fragment) {
        supportFragmentManager.beginTransaction().hide(current).show(preparing).commit()
        activeFragment = preparing
    }

    private fun selectTheme() {
        
    }
}