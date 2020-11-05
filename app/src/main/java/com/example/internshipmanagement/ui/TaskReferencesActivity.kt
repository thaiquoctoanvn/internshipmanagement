package com.example.internshipmanagement.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.ui.adapter.PickedMenteeAdapter
import com.example.internshipmanagement.ui.adapter.TaskReferencesAdapter
import com.example.internshipmanagement.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_task_references.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskReferencesActivity : BaseActivity() {

    private val mentorViewModel by viewModel<MentorViewModel>()

    private lateinit var taskReferencesAdapter: TaskReferencesAdapter
    private lateinit var pickedMenteeAdapter: PickedMenteeAdapter

    // List chứa bản xem trước các mentee được chọn cho task
    private val pickedMentees = mutableListOf<MyMentee>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_task_references
    }

    override fun setViewOnEventListener() {
        ibYourMentessBack.setOnClickListener { finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setBaseObserver(mentorViewModel)
        setObserver()
        getMyMentees()
    }

    // Hiển thị danh sách tất cả mentee mình quản lí
    private fun updateUI(mentees: MutableList<MyMentee>) {
        if(!this::taskReferencesAdapter.isInitialized) {
            taskReferencesAdapter = TaskReferencesAdapter(onItemClick)
        }
        taskReferencesAdapter.submitList(mentees)
        rvTaskReference.adapter = taskReferencesAdapter

        updatePickedMenteesWindow(pickedMentees)
    }

    // Hiển thị bản xem trước các mentee được chọn cho task
    private fun updatePickedMenteesWindow(pickedMentees: MutableList<MyMentee>) {
        if(!this::pickedMenteeAdapter.isInitialized) {
            pickedMenteeAdapter = PickedMenteeAdapter(onItemRemove)
        }
        pickedMenteeAdapter.submitList(pickedMentees)
        rvPickedMentee.adapter = pickedMenteeAdapter
    }

    private fun setObserver() {
        mentorViewModel.getMyMenteesValue().observe(this, Observer {
            updateUI(it)
        })
    }

    // Request danh sách mentee
    private fun getMyMentees() {
        mentorViewModel.getMyMenteesForTaskReference()
    }

    // Thêm mentee vào list chọn cho task và cập nhật ui
    private fun addPickedMentee(pickedItem: MyMentee) {
        pickedMentees.add(pickedItem)
        pickedMenteeAdapter.notifyItemInserted(pickedMentees.size - 1)
    }

    // Xóa mentee ra khỏi list được chọn và cập nhật ui
    private fun removePickedMentee(pickedItem: MyMentee) {
        val target = pickedMentees.find { it.menteeId == pickedItem.menteeId }
        val targetPos = pickedMentees.indexOf(target)
        pickedMentees.remove(target)
        pickedMenteeAdapter.notifyItemRemoved(targetPos)
    }

    private val onItemClick: (position: Int) -> Unit = {
        val clickedItem = mentorViewModel.getMyMenteesValue().value!![it]
        if(clickedItem.isReferred == "false") {
            mentorViewModel.getMyMenteesValue().value!![it].isReferred = "true"
            addPickedMentee(clickedItem)
        } else {
            mentorViewModel.getMyMenteesValue().value!![it].isReferred = "false"
            removePickedMentee(clickedItem)
        }
        taskReferencesAdapter.notifyItemChanged(it)
    }

    // Sự kiện click nút remove trên bản xem trước mentee được chọn
    private val onItemRemove: (position: Int, id: String) -> Unit = {position: Int, id: String ->
        removePickedMentee(pickedMentees[position])
        val target = mentorViewModel.getMyMenteesValue().value?.find { it.menteeId == id }.also {
            it?.isReferred = "false"
        }
        taskReferencesAdapter.notifyItemChanged(mentorViewModel.getMyMenteesValue().value!!.indexOf(target))
    }
}