package com.example.internshipmanagement.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MentorsTask
import com.example.internshipmanagement.ui.adapter.MentorsTaskAdapter
import com.example.internshipmanagement.ui.base.BaseFragment
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.etSearchDashBoard
import kotlinx.android.synthetic.main.fragment_dashboard.ibClearAllSearch
import kotlinx.android.synthetic.main.fragment_dashboard.rvYourTask
import kotlinx.android.synthetic.main.fragment_dashboard.tvDate
import kotlinx.android.synthetic.main.fragment_dashboard.tvGreeting
import kotlinx.android.synthetic.main.fragment_dashboard.tvNoTaskDashBoard
import kotlinx.android.synthetic.main.fragment_dashboard.tvYourTaskTitle
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.hours


/*
* Màn hình dash board của mentor
* */
class DashboardFragment : BaseFragment() {

    private val userViewModel by viewModel<UserViewModel>()
    private val mentorViewModel by viewModel<MentorViewModel>()

    private lateinit var mentorsTaskAdapter: MentorsTaskAdapter

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun setViewOnEventListener() {
        ibAddTask.setOnClickListener { switchToAddNewTaskActivity() }
        etSearchDashBoard.doAfterTextChanged {
            if(TextUtils.isEmpty(it)) {
                ibClearAllSearch.visibility = View.GONE
            } else {
                ibClearAllSearch.visibility = View.VISIBLE
            }
            mentorViewModel.filterTasks(it.toString())
        }
        ibClearAllSearch.setOnClickListener { etSearchDashBoard.setText("") }
    }

    override fun setObserverFragment() {
        mentorViewModel.getMentorsTasksValue().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
        mentorViewModel.getFilteredTasksValue().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sayGreeting()
        userViewModel.registerFCM()
        mentorViewModel.getMentorsTasks()
    }

    private fun sayGreeting() {
        val time = System.currentTimeMillis()
        tvDate.text = FunctionHelper.getDateFromTimeMilliSecond(time.toString())
        tvYourTaskTitle.text = getString(R.string.tv_all_task_dash_board, 0)

        Log.d("###", "Time: ${TimeUnit.MILLISECONDS.toHours(time).toInt()}")
        when(TimeUnit.MILLISECONDS.toHours(time).toInt()) {
            in 4..10 -> {
                tvGreeting.text = getString(R.string.good_morning)
            }
            in 11..17 -> {
                tvGreeting.text = getString(R.string.good_afternoon)
            }
            else -> {
                tvGreeting.text = getString(R.string.good_evening)
            }
        }
    }

    private fun switchToAddNewTaskActivity() {
        startActivity(Intent(activity, AddNewTaskActivity::class.java))
    }

    private fun updateUI(tasks: MutableList<MentorsTask>) {
        if(!this::mentorsTaskAdapter.isInitialized) {
            mentorsTaskAdapter = MentorsTaskAdapter(onItemTaskClick)
            tvYourTaskTitle.text = getString(R.string.tv_all_task_dash_board, tasks.size)
            tvNoTaskDashBoard.visibility = View.GONE
        }

        mentorsTaskAdapter.submitList(tasks)
        rvYourTask.adapter = mentorsTaskAdapter

    }

    private val onItemTaskClick: (mentorsTask: MentorsTask) -> Unit = {
        val intent = Intent(activity, MentorTaskDetailActivity::class.java)
        intent.putExtra("taskId", it.taskId)
        intent.putExtra("taskDeadline", it.deadline)
        intent.putExtra("taskState", it.isClosed)
        intent.putExtra("taskContent", it.content)
        startActivity(intent)
    }
}