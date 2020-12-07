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

    private var isSearching = false

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_task_references
    }

    override fun setViewOnEventListener() {
        ibYourMentessBack.setOnClickListener { returnValueToAddNewTaskAct() }
        etSearchReferences.doAfterTextChanged {
            if(TextUtils.isEmpty(it)) {
                ibClearAllSearch.visibility = View.GONE
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
            // Khi list item được chọn thay đổi
            pickedReferences.observe(this@TaskReferencesActivity, Observer {
                Log.d("###", "Picked list has been changed")
                updatePickedMenteesWindow(it)
            })

            // Khi list chính thay đổi
            myMenteesForTaskRefer.observe(this@TaskReferencesActivity, Observer {
                Log.d("###", "Main list has been changed")
                updateUI(it)
            })

            // Khi list kết quả tìm kiếm thay đổi
            filteredReferences.observe(this@TaskReferencesActivity, Observer {
                Log.d("###", "Filtered list has been changed")
                updateUI(it)
            })

            // Khi item trong list chính thay đổi thuộc tính
            modifiedFilteredItemPosition.observe(this@TaskReferencesActivity, Observer {
                Log.d("###", "Filtered item has been changed")
                taskReferencesAdapter.notifyItemChanged(it)
            })

            // Khi item trong list kết quả tìm kiếm thay đổi thuộc tính
            modifiedItemPosition.observe(this@TaskReferencesActivity, Observer {
                Log.d("###", "Item has been changed")
                taskReferencesAdapter.notifyItemChanged(it)
            })

            // Khi thêm 1 item vào list chọn
            currentPickedItemPosition.observe(this@TaskReferencesActivity, Observer {
                Log.d("###", "Current added item at: $it")
                pickedMenteeAdapter.notifyItemInserted(it)
            })

            // Khi xóa 1 item khỏi list chọn
            currentRemovedItemPosition.observe(this@TaskReferencesActivity, Observer {
                Log.d("###", "Current removed item at: $it")
                pickedMenteeAdapter.notifyItemRemoved(it)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setBaseObserver(taskReferencesViewModel)
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
        val lastPickedList = mutableListOf<MyMentee>()
        intent.getParcelableArrayListExtra<MyMentee>("referencesList")?.let { availableItems ->
            lastPickedList.addAll(availableItems)
        }
        taskReferencesViewModel.let {
            // Nếu list đã chọn trước đó, thì đồng bộ vào list request về từ server
            it.getMyMenteesForTaskReference(lastPickedList)
            it.setPickedReferencesValue(lastPickedList)
        }
    }

    private fun returnValueToAddNewTaskAct() {
        val referencesPush = Intent(REFERENCES_PUSH)
        referencesPush.putParcelableArrayListExtra(
            "references",
            taskReferencesViewModel.pickedReferences.value as ArrayList<MyMentee>
        )
        sendBroadcast(referencesPush)
        this.finish()
    }

    private val onItemClick: (id: String) -> Unit = {
        taskReferencesViewModel.setReferState(it, isSearching)
    }

    // Sự kiện click nút remove trên bản xem trước mentee được chọn
    private val onItemRemove: (position: Int, id: String) -> Unit = {position: Int, id: String ->
        taskReferencesViewModel.setReferState(id, isSearching)
    }
}