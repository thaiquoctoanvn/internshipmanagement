package com.example.internshipmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewAmbient
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.adapter.TabStatisticAdapter
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.SERVER_URL
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileActivity : BaseActivity() {

    private lateinit var tabStatisticAdapter: TabStatisticAdapter

    private val userViewModel by viewModel<UserViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_user_profile
    }

    override fun setViewOnEventListener() {
        ibUserProfileBack.setOnClickListener { this.finish() }
        ibProfileEvaluation.setOnClickListener { switchToEvaluationScreen() }

        tabLayoutPieChart.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("###", "${tab?.text}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun setObserver() {
        userViewModel.userProfile.observe(this, Observer {
            updateUserProfileUI(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent
        if(data != null) {
            data.getStringExtra("userId")?.let { userViewModel.getUserProfile(it) }
        }
    }

    private fun updateUserProfileUI(userProfile: UserProfile) {
        val amIMentor = userViewModel.getMyAccountType() == "1"
        val isItMentor = isMentorAccount(userProfile.type)
        when {
            isItMentor || !amIMentor -> {
                if(isItMentor) {
                    tvUserProfileStatus.text = getString(R.string.mentor)
                } else if(!amIMentor) {
                    tvUserProfileStatus.text = getString(R.string.mentee)
                }
                ibProfileEvaluation.visibility = View.GONE
                tabLayoutPieChart.visibility = View.GONE
                vpPieChart.visibility = View.GONE
            }
            else -> {
                tvUserProfileStatus.text = getString(R.string.mentee)
                launchTab()
            }
        }
        tvProfileName.text = userProfile.name
        tvNickName.text = "@" + userProfile.nickName
        tvProfilePosition.text = userProfile.role
        tvProfileEmail.text = userProfile.email
        Glide.with(this)
            .load("$SERVER_URL${userProfile.avatarUrl}")
            .circleCrop()
            .placeholder(R.drawable.default_avatar)
            .into(ivAvatarProfile)
    }

    private fun switchToEvaluationScreen() {
        val userId = userViewModel.userProfile.value?.userId
        val myId = userViewModel.getSharedPref().getString("userId", "")
        val intent = Intent(this, EvaluationProfileActivity::class.java)
        intent.apply {
            putExtra("userId", userId)
            putExtra("myId", myId)
        }
        startActivity(intent)
    }

    private fun launchTab() {
        if(!this::tabStatisticAdapter.isInitialized) {
            tabStatisticAdapter = TabStatisticAdapter(supportFragmentManager, this.lifecycle)
        }
        vpPieChart.adapter = tabStatisticAdapter
        TabLayoutMediator(tabLayoutPieChart, vpPieChart) { tab: TabLayout.Tab, position: Int ->
            when(position) {
                0 -> tab.text = "Criteria statistic"
                else -> tab.text = "Task statistic"
            }
        }.attach()
    }
}