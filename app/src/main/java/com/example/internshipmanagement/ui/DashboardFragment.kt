package com.example.internshipmanagement.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MentorsTask
import com.example.internshipmanagement.ui.adapter.MentorsTaskAdapter
import com.example.internshipmanagement.ui.base.BaseFragment
import com.example.internshipmanagement.util.FunctionHelper
import com.example.internshipmanagement.util.SERVER_URL
import com.example.internshipmanagement.util.TASK_ADDING_PUSH
import com.example.internshipmanagement.util.TASK_REVIEWING_PUSH
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

    private lateinit var taskAddingPush: BroadcastReceiver
    private lateinit var taskReviewingPush: BroadcastReceiver

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
        slDashBoard.setOnRefreshListener { refreshTaskData() }
    }

    override fun setObserverFragment() {
        mentorViewModel.mentorsTasks.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
        mentorViewModel.filteredTasks.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenBroadcast()
        registerBroadcast()
        sayGreeting()
        userViewModel.registerFCM()
        mentorViewModel.getMentorsTasks()
    }

    private fun sayGreeting() {
        val time = System.currentTimeMillis()
        tvDate.text = FunctionHelper.getDateFromTimeMilliSecond(time.toString())
        tvYourTaskTitle.text = getString(R.string.tv_all_task_dash_board, 0)

        val date = Date(time)
        val calendar = Calendar.getInstance()
        calendar.time = date
        Log.d("###", "Time: ${TimeUnit.MILLISECONDS.toHours(time).toInt()}")
        when(calendar[Calendar.HOUR_OF_DAY]) {
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
        Glide.with(this)
            .load("$SERVER_URL${userViewModel.getUserAvatarUrl()}")
            .placeholder(R.drawable.default_avatar)
            .circleCrop()
            .into(ivMiniMentorAvatar)
    }

    private fun switchToAddNewTaskActivity() {
        startActivity(Intent(activity, AddNewTaskActivity::class.java))
    }

    private fun updateUI(tasks: MutableList<MentorsTask>) {
        slDashBoard.isRefreshing = false
        if(!this::mentorsTaskAdapter.isInitialized) {
            mentorsTaskAdapter = MentorsTaskAdapter(onItemTaskClick)
            tvYourTaskTitle.text = getString(R.string.tv_all_task_dash_board, tasks.size)
            tvNoTaskDashBoard.visibility = View.GONE
        }

        mentorsTaskAdapter.submitList(tasks)
        rvYourTask.adapter = mentorsTaskAdapter

    }

    private fun refreshTaskData() {
        Log.d("###", "swiping")
        slDashBoard.setColorSchemeResources(
            R.color.behavior_color,
            R.color.knowledge_color,
            R.color.proactive_color
        )
        mentorViewModel.getMentorsTasks()
    }

    private fun listenBroadcast() {
        taskAddingPush = object : BroadcastReceiver() {
            override fun onReceive(contect: Context?, intent: Intent?) {
                mentorViewModel.getMentorsTasks()
            }
        }
        taskReviewingPush = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null) {
                    val taskId = intent.getStringExtra("taskId")
                    val position = mentorViewModel.updateCurrentInteractedItem(taskId.toString())
                    if(position > -1) {
                        mentorsTaskAdapter.notifyItemMoved(position, 0)
                    }
                }
            }
        }
    }

    private fun registerBroadcast() {
        requireActivity().registerReceiver(taskAddingPush, IntentFilter(TASK_ADDING_PUSH))
        requireActivity().registerReceiver(taskReviewingPush, IntentFilter(TASK_REVIEWING_PUSH))
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