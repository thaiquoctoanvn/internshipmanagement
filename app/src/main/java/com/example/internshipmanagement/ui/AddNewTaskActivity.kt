package com.example.internshipmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_new_task.*

class AddNewTaskActivity : BaseActivity() {

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_add_new_task
    }

    override fun setViewOnEventListener() {
        ibAddReference.setOnClickListener { switchToTaskReferenceActivity() }
        ibAddNewTaskBack.setOnClickListener { finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun switchToTaskReferenceActivity() {
        startActivity(Intent(this, TaskReferencesActivity::class.java))
    }
}