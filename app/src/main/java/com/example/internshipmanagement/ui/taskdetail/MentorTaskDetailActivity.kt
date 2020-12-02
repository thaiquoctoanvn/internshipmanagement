package com.example.internshipmanagement.ui.taskdetail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.TaskReference
import com.example.internshipmanagement.ui.MentorViewModel
import com.example.internshipmanagement.ui.taskreviewing.TaskReferenceDetailActivity
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.FunctionHelper
import com.example.internshipmanagement.util.TASK_REVIEWING_PUSH
import kotlinx.android.synthetic.main.activity_mentor_task_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MentorTaskDetailActivity : BaseActivity() {

    private lateinit var mentorTaskDetailAdapter: MentorTaskDetailAdapter
    private lateinit var reviewingPush: BroadcastReceiver

    private val mentorViewModel by viewModel<MentorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listenBroadcast()
        registerBroadcast()
        loadTaskInfo()
    }

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_mentor_task_detail
    }

    override fun setViewOnEventListener() {
        ibMentorTaskDetailBack.setOnClickListener { this.finish() }
    }

    override fun setObserver() {
        mentorViewModel.taskReferences.observe(this, Observer {
            updateReferencesUI(it)
        })
    }

    private fun loadTaskInfo() {
        val intent = intent
        intent?.let {
            val deadline = it.getStringExtra("taskDeadline")
            val taskContent = it.getStringExtra("taskContent")
            val taskId = it.getStringExtra("taskId")
            if(!deadline.isNullOrEmpty() && !taskContent.isNullOrEmpty() && !taskId.isNullOrEmpty()) {
                tvNoData.visibility = View.GONE
                val now = System.currentTimeMillis()
                var expired = ""
                if(deadline.toLong() < now) {
                    expired = "(Expired)"
                }
                tvMentorTaskDetailDeadline.text = "Deadline: ${FunctionHelper.getDateFromTimeMilliSecond(deadline)} $expired"
                tvMentorTaskDetailName.text = taskContent

                mentorViewModel.getTaskReferences(taskId)
            }

        }
    }

    private fun updateReferencesUI(references: MutableList<TaskReference>) {
        if(!this::mentorTaskDetailAdapter.isInitialized) {
            mentorTaskDetailAdapter = MentorTaskDetailAdapter(onItemClick)
        }
        mentorTaskDetailAdapter.submitList(references)
        rvMentorTaskDetail.adapter = mentorTaskDetailAdapter
    }

    private fun listenBroadcast() {
        reviewingPush = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.getStringExtra("referId")?.let {
                    val position = mentorViewModel.setReviewedStateReference(it)
                    if(position > -1) {
                        mentorTaskDetailAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    private fun registerBroadcast() {
        registerReceiver(reviewingPush, IntentFilter(TASK_REVIEWING_PUSH))
    }

    private val onItemClick: (referenceId: String) -> Unit = {
        val intent = Intent(this, TaskReferenceDetailActivity::class.java)
        intent.putExtra("referenceId", it)
        startActivity(intent)
    }
}