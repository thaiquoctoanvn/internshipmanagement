package com.example.internshipmanagement.ui.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabStatisticAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> {
                val criteriaStatisticsFragment = CriteriaStatisticsFragment()
                criteriaStatisticsFragment.arguments = Bundle().apply {
                    putString("userProfileTabName", "criteriaTab")
                }
                return criteriaStatisticsFragment
            }
            else -> {
                val taskStatisticsFragment = TaskStatisticsFragment()
                taskStatisticsFragment.arguments = Bundle().apply {
                    putString("userProfileTabName", "taskTab")
                }
                return taskStatisticsFragment
            }
        }
    }
}