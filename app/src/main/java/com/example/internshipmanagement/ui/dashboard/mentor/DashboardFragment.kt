package com.example.internshipmanagement.ui.dashboard.mentor

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
import com.example.internshipmanagement.ui.main.MainViewModel
import com.example.internshipmanagement.ui.addtask.AddNewTaskActivity
import com.example.internshipmanagement.ui.taskdetail.mentor.MentorTaskDetailActivity
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
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit


/*
* Màn hình dash board của mentor
* */
class DashboardFragment : BaseFragment() {

    private lateinit var taskAddingPush: BroadcastReceiver
    private lateinit var taskReviewingPush: BroadcastReceiver

//    private val userViewModel by viewModel<UserViewModel>()
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val mentorDashBoardViewModel by viewModel<MentorDashBoardViewModel>()
//    private val mentorViewModel by viewModel<MentorViewModel>()

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
            mentorDashBoardViewModel.filterTasks(it.toString())
        }
        ibClearAllSearch.setOnClickListener { etSearchDashBoard.setText("") }
        slDashBoard.setOnRefreshListener { refreshTaskData() }
    }

    override fun setObserverFragment() {
        mentorDashBoardViewModel.mentorsTasks.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
        mentorDashBoardViewModel.filteredTasks.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenBroadcast()
        registerBroadcast()
        sayGreeting()
//        userViewModel.registerFCM()
        mentorDashBoardViewModel.getMentorsTasks()
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
                tvGreeting.text = getString(R.string.good_morning_mentor)
            }
            in 11..17 -> {
                tvGreeting.text = getString(R.string.good_afternoon_mentor)
            }
            else -> {
                tvGreeting.text = getString(R.string.good_evening_mentor)
            }
        }
        Glide.with(this)
            .load("$SERVER_URL${mainViewModel.getMyAvatarUrl()}")
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
        mentorDashBoardViewModel.getMentorsTasks()
    }

    private fun listenBroadcast() {
        taskAddingPush = object : BroadcastReceiver() {
            override fun onReceive(contect: Context?, intent: Intent?) {
                mentorDashBoardViewModel.getMentorsTasks()
            }
        }
        taskReviewingPush = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null) {
                    val taskId = intent.getStringExtra("taskId")
                    val position = mentorDashBoardViewModel.updateCurrentInteractedItem(taskId.toString())
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