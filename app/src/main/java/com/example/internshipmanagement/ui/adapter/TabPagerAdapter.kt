package com.example.internshipmanagement.ui.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.internshipmanagement.ui.AllMenteeFragment
import com.example.internshipmanagement.ui.MyMenteeFragment

class TabPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> {
                val allMenteeTab = AllMenteeFragment()
                allMenteeTab.arguments = Bundle().apply {
                    putString("tabName", "allMenteeTab")
                }
                return allMenteeTab
            }
            else -> {
                val myMenteeTab = MyMenteeFragment()
                myMenteeTab.arguments = Bundle().apply {
                    putString("tabName", "myMenteeTab")
                }
                return myMenteeTab
            }
        }
    }
}