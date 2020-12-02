package com.example.internshipmanagement.ui.people

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.searchusers.SearchResultActivity
import com.example.internshipmanagement.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_search_user_tab.view.*

class SearchFragment : BaseFragment() {

    private lateinit var tabPagerAdapter: TabPagerAdapter

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun setViewOnEventListener() {
        tabLayoutSearch.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                highlightSelectedTab(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                highlightSelectedTab(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        ibSearchUser.setOnClickListener { startActivity(Intent(activity, SearchResultActivity::class.java)) }
    }

    override fun setObserverFragment() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchTab()
    }

    private fun launchTab() {
        if(!this::tabPagerAdapter.isInitialized) {
            tabPagerAdapter = TabPagerAdapter(this)
        }
        val dpi = resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
        val tabHeaderWidth = ((resources.displayMetrics.widthPixels / dpi) / 2).toInt()
        vpUser.adapter = tabPagerAdapter
        TabLayoutMediator(tabLayoutSearch, vpUser) { tab, position ->
            when(position) {
                0 -> {
                    tab.customView = createTabItem("All mentees", tabHeaderWidth)
                }
                else -> {
                    tab.customView = createTabItem("My mentees", tabHeaderWidth)
                }
            }
        }.attach()
    }

    private fun createTabItem(title: String, maxWidth: Int): View {
        val tab = LayoutInflater.from(context).inflate(R.layout.item_search_user_tab, null)
        val tvTabTitle = tab.findViewById<TextView>(R.id.tvTabTitle)
        tvTabTitle.width = maxWidth
        tvTabTitle.text = title
        return tab
    }

    private fun highlightSelectedTab(tab: TabLayout.Tab?, isSelected: Boolean) {
        var backgroundColorTabHeader = R.color.white
        var tabTitleColor = R.color.icon_or_text_color
        if(isSelected) {
            backgroundColorTabHeader = R.color.selected_tab_background
            tabTitleColor = R.color.black
        }
        tab?.customView?.tvTabTitle?.let {
            it.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(it.context, backgroundColorTabHeader))
            it.setTextColor(ContextCompat.getColor(it.context, tabTitleColor))
        }
    }
}