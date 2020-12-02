package com.example.internshipmanagement.ui.addtask

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaskAddingViewModel(
    private val mentorRepository: MentorRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    fun addNewTask(deadline: String, taskBody: String, referencesList: MutableList<MyMentee>) {
        viewModelScope.launch {
            val ownerId = sharedPref.getString("userId", "")
            if(!ownerId.isNullOrEmpty()) {
                val menteeIds = mutableListOf<String>()
                val gcmIds = mutableListOf<String>()
                referencesList.forEach {
                    menteeIds.add(it.menteeId)
                    gcmIds.add(it.gcmId)
                }
                // Các điều kiện đã thỏa
                super.setIsLoadingValue(true)
                val res = mentorRepository.addNewTask(ownerId, deadline, taskBody, menteeIds, gcmIds).body()
                super.setIsLoadingValue(false)
                delay(1000)
                _isSuccessful.value = res != null
            }

        }
    }
}