package com.example.internshipmanagement.ui.notification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.Notification
import com.example.internshipmanagement.ui.UserViewModel
import com.example.internshipmanagement.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_notification.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationActivity : BaseActivity() {

    private lateinit var notificationAdapter: NotificationAdapter

//    private val userViewModel by viewModel<UserViewModel>()
    private val notificationViewModel by viewModel<NotificationViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_notification
    }

    override fun setViewOnEventListener() {
        slNotifications.setOnRefreshListener { refreshNotifications() }
        ibNotificationBack.setOnClickListener { this.finish() }
    }

    override fun setObserver() {
        notificationViewModel.menteeNotifications.observe(this, Observer {
            updateNotificationContainer(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationViewModel.getMenteesNotifications()
    }

    private fun updateNotificationContainer(notifications: MutableList<Notification>) {
        slNotifications.isRefreshing = false
        if(!this::notificationAdapter.isInitialized) {
            notificationAdapter = NotificationAdapter()
        }
        if(notifications.size > 0) {
            pbNotificationLoading.visibility = View.GONE
            tvNotificationNoData.visibility = View.GONE
            rvNotifications.visibility = View.VISIBLE
            notificationAdapter.submitList(notifications)
            rvNotifications.adapter = notificationAdapter
        } else {
            pbNotificationLoading.visibility = View.GONE
            tvNotificationNoData.visibility = View.VISIBLE
            rvNotifications.visibility = View.GONE
        }
    }

    private fun refreshNotifications() {
        slNotifications.setColorSchemeResources(
            R.color.behavior_color,
            R.color.knowledge_color,
            R.color.proactive_color
        )
        notificationViewModel.getMenteesNotifications()
    }
}