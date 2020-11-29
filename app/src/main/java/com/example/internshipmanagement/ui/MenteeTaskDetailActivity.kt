package com.example.internshipmanagement.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MenteeTaskDetail
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.FunctionHelper
import com.example.internshipmanagement.util.SUBMISSION_PUSH
import kotlinx.android.synthetic.main.activity_mentee_task_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenteeTaskDetailActivity : BaseActivity() {

    private lateinit var submissionPush: BroadcastReceiver

    private val menteeViewModel by viewModel<MenteeViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_mentee_task_detail
    }

    override fun setViewOnEventListener() {
        ibMenteeTaskDetailBack.setOnClickListener { this.finish() }
        ibDetailSubmission.setOnClickListener {  }
        tvSubmit.setOnClickListener { switchToSubmissionScreen() }
    }

    override fun setObserver() {
        menteeViewModel.menteeTaskDetail.observe(this, Observer {
            updateTaskDetailUI(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listenBroadcast()
        registerBroadcast()
        loadMenteeTaskDetail()
    }

    private fun listenBroadcast() {
        submissionPush = object : BroadcastReceiver() {
            override fun onReceive(content: Context?, intent: Intent?) {
                if(intent != null) {
                    tvSubmit.apply {
                        backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(tvSubmit.context, R.color.mentee_light_color)
                        )
                        isEnabled = false
                        text = getString(R.string.submitted_state)
                    }
                }
            }
        }
    }

    private fun registerBroadcast() {
        registerReceiver(submissionPush, IntentFilter(SUBMISSION_PUSH))
    }

    private fun loadMenteeTaskDetail() {
        val intenData = intent
        if(intenData != null) {
            val taskId = intenData.getStringExtra("taskId")
            if(!taskId.isNullOrEmpty()) {
                menteeViewModel.getMenteeTaskDetail(taskId)
            }
        }
    }

    private fun updateTaskDetailUI(menteeTaskDetail: MenteeTaskDetail) {
        var deadline = "Deadline: ${FunctionHelper.getDateFromTimeMilliSecond(menteeTaskDetail.deadline)}"
        if(menteeTaskDetail.deadline.toLong() < System.currentTimeMillis()) {
            deadline = "$deadline (Expired)"
        }
        tvMenteeTaskDetailDeadline.text = deadline
        tvMenteeTaskDetailName.text = menteeTaskDetail.content

        // Nếu đã nộp bài thì disable button submit
        if(menteeTaskDetail.isSubmitted.toInt() == 1) {
            tvSubmit.apply {
                backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(tvSubmit.context, R.color.mentee_light_color)
                )
                isEnabled = false
                text = getString(R.string.submitted_state)
            }
        }
        val mark = "Mark: ${menteeTaskDetail.mark}"
        tvMenteeTaskDetailMark.text = mark
        if(!TextUtils.isEmpty(menteeTaskDetail.comment)) {
            tvYourMentorCommentContent.text = menteeTaskDetail.comment
        }
    }

    private fun switchToSubmissionScreen() {
        val intent = Intent(this, SubmissionActivity::class.java)
        intent.putExtra("referId", menteeViewModel.menteeTaskDetail.value?.id)
        startActivity(intent)
    }
}