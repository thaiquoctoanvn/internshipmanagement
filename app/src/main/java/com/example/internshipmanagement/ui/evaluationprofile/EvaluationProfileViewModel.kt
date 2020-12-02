package com.example.internshipmanagement.ui.evaluationprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MenteesEvaluation
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class EvaluationProfileViewModel(
    private val mentorRepository: MentorRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _menteesEvaluations = MutableLiveData<MutableList<MenteesEvaluation>>()
    val menteesEvaluations: LiveData<MutableList<MenteesEvaluation>>
        get() = _menteesEvaluations

    private val _isMyMentee = MutableLiveData<Boolean>()
    val isMyMentee: LiveData<Boolean>
        get() = _isMyMentee

    private val _menteeProfile = MutableLiveData<UserProfile>()
    val menteeProfile: LiveData<UserProfile>
        get() = _menteeProfile

    fun getMenteesEvaluations(userId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getMenteesEvaluations(userId).body()
            if(res != null) {
                _menteesEvaluations.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun isMyMentee(userId: String, myId: String) {
        viewModelScope.launch {
            val res = mentorRepository.isMyMentee(userId, myId).body()
            if(res != null) {
                _isMyMentee.value = res == "true"
            }
        }
    }

    fun getMenteeProfile(userId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = userRepository.getUserProfile(userId).body()
            if(res != null) {
                _menteeProfile.value = res
            }
            super.setIsLoadingValue(false)
        }
    }
}