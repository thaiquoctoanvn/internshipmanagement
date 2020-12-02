package com.example.internshipmanagement.ui.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.internshipmanagement.ui.people.mentor.AllMenteeFragment
import com.example.internshipmanagement.ui.people.mentor.MyMenteeFragment

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