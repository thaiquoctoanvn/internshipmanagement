package com.example.internshipmanagement.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity

class MySubmissionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_my_submission
    }

    override fun setViewOnEventListener() {
    }

    override fun setObserver() {
    }
}