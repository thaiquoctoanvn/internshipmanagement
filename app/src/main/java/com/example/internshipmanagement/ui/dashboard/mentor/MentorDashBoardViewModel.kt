package com.example.internshipmanagement.ui.dashboard.mentor

import android.content.SharedPreferences
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MenteesTask
import com.example.internshipmanagement.data.entity.MentorsTask
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class MentorDashBoardViewModel(
    private val mentorRepository: MentorRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _filteredTasks = MutableLiveData<MutableList<MentorsTask>>()
    val filteredTasks: LiveData<MutableList<MentorsTask>>
        get() = _filteredTasks

    private val _mentorsTasks = MutableLiveData<MutableList<MentorsTask>>()
    val mentorsTasks: LiveData<MutableList<MentorsTask>>
        get() = _mentorsTasks

    fun filterTasks(keyWords: String) {
        viewModelScope.launch {
            if(TextUtils.isEmpty(keyWords)) {
                _filteredTasks.value = mentorsTasks.value
            } else {
                val temp = mentorsTasks.value?.filter {
                    it.content.contains(keyWords) || it.deadline.contains(keyWords)
                }?.toMutableList()
                _filteredTasks.value = temp
            }
        }
    }

    fun getMentorsTasks() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val mentorId = sharedPref.getString("userId", "")
            if(!mentorId.isNullOrEmpty()) {
                val res = mentorRepository.getMentorsTasks(mentorId).body()
                if(res != null) {
                    _mentorsTasks.value = sortTaskByDeadline(res)
                }
            }
            super.setIsLoadingValue(false)
        }
    }

    fun updateCurrentInteractedItem(id: String): Int {
        var position = -1
        mentorsTasks.value?.let { tasks ->
            val targetItem = tasks.find { it.taskId == id }
            targetItem?.let {
                position = tasks.indexOf(it)
                Collections.swap(tasks, position, 0)
            }
        }
        return position
    }

    private fun sortTaskByDeadline(srcTasks: MutableList<MentorsTask>): MutableList<MentorsTask> {
        val outOfDateTasks = mutableListOf<MentorsTask>()
        val now = System.currentTimeMillis()
        srcTasks.sortBy { it.deadline.toLong() }
        srcTasks.forEach {
            if(it.deadline.toLong() < now) {
                outOfDateTasks.add(it)
            }
        }
        if(outOfDateTasks.isNotEmpty()) {
            srcTasks.removeAll(outOfDateTasks)
            srcTasks.addAll(outOfDateTasks)
        }
        return srcTasks
    }
}