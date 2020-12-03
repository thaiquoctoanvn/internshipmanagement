package com.example.internshipmanagement.ui.dashboard.mentee

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MenteesTask
import com.example.internshipmanagement.ui.taskdetail.mentee.MenteeTaskDetailActivity
import com.example.internshipmanagement.ui.notification.NotificationActivity
import com.example.internshipmanagement.ui.base.BaseFragment
import com.example.internshipmanagement.util.FCM_PUSH
import com.example.internshipmanagement.util.FunctionHelper
import com.example.internshipmanagement.util.SUBMISSION_PUSH
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.*
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.ibClearAllSearch
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.rvYourTask
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.tvYourTaskTitle
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.tvDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class MenteeDashBoardFragment : BaseFragment() {

    private lateinit var menteesTaskAdapter: MenteesTaskAdapter
    private lateinit var submissionPush: BroadcastReceiver
    private lateinit var fcmPush: BroadcastReceiver

//    private val userViewModel by viewModel<UserViewModel>()
//    private val menteeViewModel by viewModel<MenteeViewModel>()
    private val menteeDashBoardViewModel by viewModel<MenteeDashBoardViewModel>()

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_mentee_dash_board
    }

    override fun setViewOnEventListener() {
        etSearchDashBoard.doAfterTextChanged {
            if(TextUtils.isEmpty(it)) {
                ibClearAllSearch.visibility = View.GONE
            } else {
                ibClearAllSearch.visibility = View.VISIBLE
            }
            menteeDashBoardViewModel.filterTasks(it.toString())
        }
        ibClearAllSearch.setOnClickListener { etSearchDashBoard.setText("") }
        ibNotificationMenteeDashBoard.setOnClickListener {
            tvNotificationBadge.visibility = View.GONE
            startActivity(Intent(requireActivity(), NotificationActivity::class.java))
        }
        slMenteeDashBoard.setOnRefreshListener { refreshMenteeTaskData() }
    }

    override fun setObserverFragment() {
        menteeDashBoardViewModel.menteesTasks.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateTaskContainerUI(it)
        })
        menteeDashBoardViewModel.filteredTasks.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateTaskContainerUI(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sayGreeting()
        listenBroadcast()
        registerBroadcast()
//        userViewModel.registerFCM()
        menteeDashBoardViewModel.getMenteesTask()
    }

    private fun registerBroadcast() {
        requireActivity().apply {
            registerReceiver(submissionPush, IntentFilter(SUBMISSION_PUSH))
            registerReceiver(fcmPush, IntentFilter(FCM_PUSH))
        }
    }

    private fun listenBroadcast() {
        submissionPush = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null) {
                    val referId = intent.getStringExtra("referId")
                    val position = menteeDashBoardViewModel.updateCurrentInteractedItem(referId.toString())
                    if(position > -1) {
                        menteesTaskAdapter.apply {
                            // Di chuyển task vừa tương tác lên đầu
                            notifyItemChanged(position)
                            notifyItemMoved(position, 0)
                            // Cập nhật giao diện task
                        }
                    }
                }
            }
        }
        fcmPush = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null) {
                    try {
                        tvNotificationBadge.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        Log.d("###", "Set badge exception: ${e.message}")
                    }
                }
            }
        }
    }

    private fun sayGreeting() {
        val time = System.currentTimeMillis()
        tvDate.text = FunctionHelper.getDateFromTimeMilliSecond(time.toString())
        tvYourTaskTitle.text = getString(R.string.tv_all_task_dash_board, 0)

        val date = Date(time)
        val calendar = Calendar.getInstance()
        calendar.time = date
        Log.d("###", "Time: ${TimeUnit.MILLISECONDS.toHours(time)}")
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
    }

    private fun updateTaskContainerUI(tasks: MutableList<MenteesTask>) {
        slMenteeDashBoard.isRefreshing = false
        if(!this::menteesTaskAdapter.isInitialized) {
            menteesTaskAdapter = MenteesTaskAdapter(onItemClick)
            tvYourTaskTitle.text = getString(R.string.tv_all_task_dash_board, tasks.size)
            tvNoTaskDashBoard.visibility = View.GONE
        }

        menteesTaskAdapter.submitList(tasks)
        rvYourTask.adapter = menteesTaskAdapter
    }

    private fun refreshMenteeTaskData() {
        slMenteeDashBoard.setColorSchemeResources(
            R.color.behavior_color,
            R.color.knowledge_color,
            R.color.proactive_color
        )
        menteeDashBoardViewModel.getMenteesTask()
    }

    private val onItemClick: (taskId: String) -> Unit = {
        val intent = Intent(requireActivity(), MenteeTaskDetailActivity::class.java)
        intent.putExtra("taskId", it)
        startActivity(intent)
    }
}