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
//        setUpPieChart()
    }

    private fun updateUserProfileUI(userProfile: UserProfile) {
        if(isMentorAccount(userProfile.type)) {
            ibProfileEvaluation.visibility = View.GONE
            tabLayoutPieChart.visibility = View.GONE
            vpPieChart.visibility = View.GONE
            tvUserProfileStatus.text = getString(R.string.mentor)
        } else {
            tvUserProfileStatus.text = getString(R.string.mentee)
            launchTab()
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
                0 -> tab.text = "Criteria"
                else -> tab.text = "Task mark"
            }
        }.attach()
    }

//    private fun setUpPieChart() {
//        menteePieChart.apply {
//
//            isDrawHoleEnabled = true
//            holeRadius = 58f
//            transparentCircleRadius = 100f
//            rotationAngle = 0f
//            isRotationEnabled = true
//            isHighlightPerTapEnabled = true
//
//            // Tắt chart decryption
//            description.isEnabled = false
//
//            // Vị trí, khoảng cách giữa các phần trong chú thích
////            setExtraOffsets(5f, 30f, 5f, 5f)
//
////            setUsePercentValues(true)
//            setDrawCenterText(true)
//
//
//            // set màu phần lỗ tròn ở giữa
//            setHoleColor(ContextCompat.getColor(this@UserProfileActivity, R.color.white))
//
//            // Set màu và opacity phần giữa lỗ tròn và vòng màu chính
//            setTransparentCircleColor(ContextCompat.getColor(this@UserProfileActivity, R.color.top_or_bottom_view_color))
//            setTransparentCircleAlpha(102)
//
//            animateY(5000, Easing.EaseInOutQuad)
////            animateXY(2000, 2000)
//
//            setEntryLabelColor(ContextCompat.getColor(this@UserProfileActivity, R.color.black))
//            setEntryLabelTextSize(14f)
//
//            // Điều chỉnh vị trí của phần chú thích
//            legend.apply {
//                verticalAlignment = Legend.LegendVerticalAlignment.TOP
//                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
//                orientation = Legend.LegendOrientation.VERTICAL
//                setDrawInside(false)
//                xEntrySpace = 0f
//                yEntrySpace = 24f
//                yOffset = 10f
//                isWordWrapEnabled = true
//                isEnabled = false
//            }
//
//            setOnChartValueSelectedListener(this@UserProfileActivity)
//        }
//
//        setChartData()
//    }
//
//    private fun setChartData() {
//
//        // PieEntry('giá trị sẽ hiển thị trên chart', 'giá trị sẽ get khi click lên chart')
//        val pieEntries = mutableListOf<PieEntry>(
//            PieEntry(50f, 0),
//            PieEntry(13f, 1),
//            PieEntry(5f, 2),
//            PieEntry(26f, 3)
//        )
//        val pieDataSet = PieDataSet(pieEntries, "Task Statistical").apply {
//            setDrawIcons(false)
//
//            // Khoảng cách giữa các phần
//            sliceSpace = 3f
//
//            // Độ dày phần được chọn
//            selectionShift = 8f
//
//
//            // Bộ màu dùng cho chart
//            colors = ColorTemplate.LIBERTY_COLORS.toMutableList()
//
//
//        }
//        val chartNotes = mutableListOf<String>("1", "2", "3", "4")
//        val pieData = PieData(pieDataSet).apply {
//            setValueFormatter(PercentFormatter())
//            setValueTextSize(11f)
//            setValueTextColor(ContextCompat.getColor(this@UserProfileActivity, R.color.white))
//        }
//        menteePieChart.apply {
//            data = pieData
//            highlightValue(null)
//            invalidate()
//        }
//
//    }

//    override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
//        Log.d("###", "Entry selected: ${entry?.data}")
//        menteePieChart.highlightValue(highlight)
//    }
//
//    override fun onNothingSelected() {
//        Log.d("###", "Nothing selected")
//    }
}