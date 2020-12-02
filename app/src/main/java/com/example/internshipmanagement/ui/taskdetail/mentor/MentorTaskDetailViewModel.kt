package com.example.internshipmanagement.ui.taskdetail.mentor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.TaskReference
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MentorTaskDetailViewModel(
    private val mentorRepository: MentorRepository
) : BaseViewModel() {

    private val _taskReferences = MutableLiveData<MutableList<TaskReference>>()
    val taskReferences: LiveData<MutableList<TaskReference>>
        get() = _taskReferences


    fun getTaskReferences(taskId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getTaskReferences(taskId).body()
            if(res != null) {
                _taskReferences.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun setReviewedStateReference(id: String): Int {
        taskReferences.value?.forEachIndexed { index, taskReference ->
            if(id == taskReference.id) {
                taskReference.isReviewed = "1"
                return index
            }
        }
        return -1
    }
}