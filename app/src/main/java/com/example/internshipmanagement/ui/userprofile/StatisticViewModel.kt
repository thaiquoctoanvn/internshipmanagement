package com.example.internshipmanagement.ui.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.CriterionPoint
import com.example.internshipmanagement.data.entity.TaskPoint
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class StatisticViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    private val _criteriaPoints = MutableLiveData<MutableList<CriterionPoint>>()
    val criteriaPoints: LiveData<MutableList<CriterionPoint>>
        get() = _criteriaPoints

    private val _taskPoints = MutableLiveData<MutableList<TaskPoint>>()
    val taskPoints: LiveData<MutableList<TaskPoint>>
        get() = _taskPoints



    fun setUserIdValue(id: String) {
        _userId.value = id
    }

    fun getCriteriaPoints(id: String) {
        viewModelScope.launch {
            val res = userRepository.getCriteriaPoints(id).body()
            if(res != null) {
                _criteriaPoints.value = res
            }
        }
    }

    fun getTaskPoints(id: String) {
        viewModelScope.launch {
            val res = userRepository.getTaskPoints(id).body()
            if(res != null) {
                _taskPoints.value = res
            }
        }
    }
}