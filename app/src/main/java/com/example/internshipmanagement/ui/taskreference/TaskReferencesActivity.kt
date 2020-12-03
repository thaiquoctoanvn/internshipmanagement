package com.example.internshipmanagement.ui.taskreference

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.REFERENCES_PUSH
import kotlinx.android.synthetic.main.activity_task_references.*
import kotlinx.android.synthetic.main.activity_task_references.ibClearAllSearch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class TaskReferencesActivity : BaseActivity() {

    private lateinit var taskReferencesAdapter: TaskReferencesAdapter
    private lateinit var pickedMenteeAdapter: PickedMenteeAdapter

    private val taskReferencesViewModel by viewModel<TaskReferencesViewModel>()

    // List chứa bản xem trước các mentee được chọn cho task
    private val pickedMentees = mutableListOf<MyMentee>()
    private val myMentees = mutableListOf<MyMentee>()
    private var isSearching = false

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_task_references
    }

    override fun setViewOnEventListener() {
        ibYourMentessBack.setOnClickListener { returnValueToAddNewTaskAct() }
        etSearchReferences.doAfterTextChanged {
            if(TextUtils.isEmpty(it)) {
                ibClearAllSearch.visibility = View.GONE
                isSearching = false
            } else {
                ibClearAllSearch.visibility = View.VISIBLE
                isSearching = true
            }
            taskReferencesViewModel.filterMentees(it.toString())
        }
        ibClearAllSearch.setOnClickListener { etSearchReferences.setText("") }
    }

    override fun setObserver() {
        taskReferencesViewModel.apply {
            pickedReferences.observe(this@TaskReferencesActivity, Observer {
                updatePickedMenteesWindow(it)
            })
            myMenteesForTaskRefer.observe(this@TaskReferencesActivity, Observer {
                updateUI(it)
//                myMentees.addAll(it)
            })
            filteredReferences.observe(this@TaskReferencesActivity, Observer {
                updateUI(it)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setBaseObserver(taskReferencesViewModel)
//        setObserver()
        getMyMentees()
    }

    // Hiển thị danh sách tất cả mentee mình quản lí
    private fun updateUI(mentees: MutableList<MyMentee>) {
        if(!this::taskReferencesAdapter.isInitialized) {
            taskReferencesAdapter = TaskReferencesAdapter(onItemClick)
        }
        taskReferencesAdapter.submitList(mentees)
        rvTaskReference.adapter = taskReferencesAdapter
    }

    // Hiển thị bản xem trước các mentee được chọn cho task
    private fun updatePickedMenteesWindow(pickedMentees: MutableList<MyMentee>) {
        if(!this::pickedMenteeAdapter.isInitialized) {
            pickedMenteeAdapter = PickedMenteeAdapter(onItemRemove)
        }
        pickedMenteeAdapter.submitList(pickedMentees)
        rvPickedMentee.adapter = pickedMenteeAdapter
    }

    // Request danh sách mentee
    private fun getMyMentees() {
//        val existingList = mutableListOf<MyMentee>()
        intent.getParcelableArrayListExtra<MyMentee>("referencesList")?.let {
            pickedMentees.addAll(it)
        }
        taskReferencesViewModel.let {
            it.getMyMenteesForTaskReference(pickedMentees)
//            it.setPickedReferencesValue(existingList.toMutableList())
        }
//        taskReferencesViewModel.getMyMenteesForTaskReference(existingList)
        updatePickedMenteesWindow(pickedMentees)
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

    private fun returnValueToAddNewTaskAct() {
        val referencesPush = Intent(REFERENCES_PUSH)
//        val pickedItems = taskReferencesViewModel.pickedReferences.value
        referencesPush.putParcelableArrayListExtra("references", pickedMentees as ArrayList<MyMentee>)
        sendBroadcast(referencesPush)
        this.finish()
    }

    private val onItemClick: (id: String) -> Unit = {
        val clickedItem = taskReferencesViewModel.myMenteesForTaskRefer.value!!.find { item ->
            it == item.menteeId
        }
        if (clickedItem != null) {
            if(clickedItem.isReferred == "false") {
                clickedItem.isReferred = "true"
                addPickedMentee(clickedItem)
            } else {
                clickedItem.isReferred = "false"
                removePickedMentee(clickedItem)
            }
        }
        if(isSearching) {
            taskReferencesAdapter.notifyItemChanged(taskReferencesViewModel.filteredReferences.value!!.indexOf(clickedItem))
        } else {
            taskReferencesAdapter.notifyItemChanged(taskReferencesViewModel.myMenteesForTaskRefer.value!!.indexOf(clickedItem))
        }

    }

    // Sự kiện click nút remove trên bản xem trước mentee được chọn
    private val onItemRemove: (position: Int, id: String) -> Unit = {position: Int, id: String ->
        removePickedMentee(pickedMentees[position])
        val target = taskReferencesViewModel.myMenteesForTaskRefer.value?.find { it.menteeId == id }.also {
            it?.isReferred = "false"
        }
        if(isSearching) {
            taskReferencesAdapter.notifyItemChanged(taskReferencesViewModel.filteredReferences.value!!.indexOf(target))
        } else {
            taskReferencesAdapter.notifyItemChanged(taskReferencesViewModel.myMenteesForTaskRefer.value!!.indexOf(target))
        }
    }
}