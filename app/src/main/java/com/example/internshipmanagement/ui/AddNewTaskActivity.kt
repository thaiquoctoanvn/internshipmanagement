package com.example.internshipmanagement.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.widget.CalendarView
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.ui.adapter.AddNewTaskAdapter
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.REFERENCES_PUSH
import kotlinx.android.synthetic.main.activity_add_new_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.PhantomReference
import java.util.*

class AddNewTaskActivity : BaseActivity() {

    private lateinit var referencesPushBroadcast: BroadcastReceiver
    private lateinit var addNewTaskAdapter: AddNewTaskAdapter

    private var referencesList = mutableListOf<MyMentee>()

    private val mentorViewModel by viewModel<MentorViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_add_new_task
    }

    override fun setViewOnEventListener() {
        ibAddReference.setOnClickListener { switchToTaskReferenceActivity() }
        ibAddNewTaskBack.setOnClickListener { finish() }
        tvSaveNewTask.setOnClickListener { addTask() }

        val calendar = Calendar.getInstance()
        newTaskDatePicker.init(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH],
            DatePicker.OnDateChangedListener { datePicker, year, month, dayOfMonth ->
                val deadlineText = "Deadline: $dayOfMonth/$month/$year"
                tvTaskDeadline.text = deadlineText
            })
    }

    override fun setObserver() {
        mentorViewModel.isSuccessful.observe(this, androidx.lifecycle.Observer {
            completeTaskAdding(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setBaseObserver(mentorViewModel)
        listenBroadcast()
        registerBroadcast()
    }

    private fun switchToTaskReferenceActivity() {
        val intent = Intent(this, TaskReferencesActivity::class.java)
        if(referencesList.size > 0) {
            intent.putParcelableArrayListExtra("referencesList", referencesList as ArrayList<MyMentee>)
        }
        startActivity(intent)
    }

    private fun listenBroadcast() {
        referencesPushBroadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    referencesList =
                        it.getParcelableArrayListExtra<MyMentee>("references")!!.toMutableList()
                    updateUI(referencesList)
                }
            }
        }
    }

    private fun registerBroadcast() {
        registerReceiver(referencesPushBroadcast, IntentFilter(REFERENCES_PUSH))
    }

    private fun updateUI(references: MutableList<MyMentee>) {
        if (!this::addNewTaskAdapter.isInitialized) {
            addNewTaskAdapter = AddNewTaskAdapter()
        }
        addNewTaskAdapter.submitList(references)
        rvReference.adapter = addNewTaskAdapter
    }

    private fun getDeadlineTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(
            newTaskDatePicker.year,
            newTaskDatePicker.month,
            newTaskDatePicker.dayOfMonth,
            0,
            0,
            0
        )
        return calendar.timeInMillis
    }

    private fun addTask() {
        val deadline = getDeadlineTime()
        val taskBody = etTaskDecryption.text.toString().trim()

        if(System.currentTimeMillis() > deadline) {
            super.showSnackBar("Deadline time is invalid")
            return
        }
        if(TextUtils.isEmpty(taskBody)) {
            super.showSnackBar("Task decryption must be not empty")
            return
        }
        if(referencesList.size == 0) {
            super.showSnackBar("Please choose group of mentees for this task")
            return
        }

        mentorViewModel.addNewTask(deadline.toString(), taskBody, referencesList)
    }

    private fun completeTaskAdding(isSucceed: Boolean) {
        if(isSucceed) {
            this.finish()
        }
    }
}