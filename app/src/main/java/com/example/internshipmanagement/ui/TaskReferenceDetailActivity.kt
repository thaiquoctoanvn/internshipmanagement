package com.example.internshipmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskReferenceDetailActivity : BaseActivity() {

    private val mentorViewModel by viewModel<MentorViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_task_reference_detail
    }

    override fun setViewOnEventListener() {

    }

    override fun setObserver() {
        mentorViewModel.getDetailReferenceValue().observe(this, Observer {
            
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadDetailReference()
    }

    private fun loadDetailReference() {
        val intentData = intent
        if(intentData != null) {
            val referenceId = intent.getStringExtra("referenceId").toString()
            mentorViewModel.getDetailReference(referenceId)
        }
    }
}