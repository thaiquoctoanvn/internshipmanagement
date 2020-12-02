package com.example.internshipmanagement.ui.evaluationcreating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.Criterion
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EvaluationCreatingViewModel(
    private val mentorRepository: MentorRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private val _criteria = MutableLiveData<MutableList<Criterion>>()
    val criteria: LiveData<MutableList<Criterion>>
        get() = _criteria

    private val _mentorProfile = MutableLiveData<UserProfile>()
    val mentorProfile: LiveData<UserProfile>
        get() = _mentorProfile


    fun generateCriteria() {
        viewModelScope.launch {
            _criteria.value = FunctionHelper.provideCriteria()
        }
    }

    fun addEvaluation(
        mentorId: String,
        menteeId: String,
        fromDate: String,
        toDate: String,
        evaluation: String
    ) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.addEvaluation(mentorId, menteeId, fromDate, toDate, evaluation, criteria.value!!).body()
            super.setIsLoadingValue(false)
            if(res != null) {
                super.setMessageResponseValue("Give a evaluation successfully")
                delay(500)
                _isSuccessful.value = true
            } else {
                _isSuccessful.value = false
            }
        }
    }

    fun getMentorProfile(userId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.getUserProfile(userId).body()
            if(res != null) {
                _mentorProfile.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun hasMarked(): Boolean {
        criteria.value?.forEach {
            if(it.mark.toInt() <= 0) {
                return false
            }
        }
        return true
    }
}