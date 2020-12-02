package com.example.internshipmanagement.ui.taskdetail.mentee

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MenteeTaskDetail
import com.example.internshipmanagement.data.repository.MenteeRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MenteeTaskDetailViewModel(
    private val menteeRepository: MenteeRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _menteeTaskDetail = MutableLiveData<MenteeTaskDetail>()
    val menteeTaskDetail: LiveData<MenteeTaskDetail>
        get() = _menteeTaskDetail

    fun getMenteeTaskDetail(taskId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val menteeId = sharedPref.getString("userId", "").toString()
            val res = menteeRepository.getMenteeTaskDetail(taskId, menteeId).body()
            if (res != null) {
                _menteeTaskDetail.value = res
            }
            super.setIsLoadingValue(false)
        }
    }
}