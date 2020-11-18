package com.example.internshipmanagement.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MenteesTask
import com.example.internshipmanagement.ui.adapter.MenteesTaskAdapter
import com.example.internshipmanagement.ui.base.BaseFragment
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.*
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.ibClearAllSearch
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.rvYourTask
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.tvYourTaskTitle
import kotlinx.android.synthetic.main.fragment_mentee_dash_board.tvDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.hours

class MenteeDashBoardFragment : BaseFragment() {

    private val userViewModel by viewModel<UserViewModel>()
    private val menteeViewModel by viewModel<MenteeViewModel>()

    private lateinit var menteesTaskAdapter: MenteesTaskAdapter

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
            menteeViewModel.filterTasks(it.toString())
        }
        ibClearAllSearch.setOnClickListener { etSearchDashBoard.setText("") }
    }

    override fun setObserverFragment() {
        menteeViewModel.getMenteesTasksValue().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateTaskContainerUI(it)
        })
        menteeViewModel.getFilteredTasksValue().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateTaskContainerUI(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sayGreeting()
        userViewModel.registerFCM()
        menteeViewModel.getMenteesTask()
    }

    private fun sayGreeting() {
        val time = System.currentTimeMillis()
        tvDate.text = FunctionHelper.getDateFromTimeMilliSecond(time.toString())
        tvYourTaskTitle.text = getString(R.string.tv_all_task_dash_board, 0)

        Log.d("###", "Time: ${TimeUnit.MILLISECONDS.toHours(time)}")
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

    private fun updateTaskContainerUI(tasks: MutableList<MenteesTask>) {
        if(!this::menteesTaskAdapter.isInitialized) {
            menteesTaskAdapter = MenteesTaskAdapter()
            tvYourTaskTitle.text = getString(R.string.tv_all_task_dash_board, tasks.size)
            tvNoTaskDashBoard.visibility = View.GONE
        }

        menteesTaskAdapter.submitList(tasks)
        rvYourTask.adapter = menteesTaskAdapter
    }

}