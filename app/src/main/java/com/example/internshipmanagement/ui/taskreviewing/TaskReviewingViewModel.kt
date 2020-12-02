package com.example.internshipmanagement.ui.taskreviewing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.DetailTaskReference
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaskReviewingViewModel(private val mentorRepository: MentorRepository) : BaseViewModel() {


    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private val _detailReference = MutableLiveData<DetailTaskReference>()
    val detailReference: LiveData<DetailTaskReference>
        get() = _detailReference


    fun getDetailReference(referenceId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getDetailReference(referenceId).body()
            if(res != null) {
                _detailReference.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun updateSpecificReferenceOfTask(mark: String, comment: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val referId = detailReference.value?.referenceId.toString()
            val res = mentorRepository.updateSpecificReferenceOfTask(referId, mark, comment).body()
            super.setIsLoadingValue(false)
            if(res != null) {
                super.setMessageResponseValue("Reviewed successfully")
                delay(500)
                _isSuccessful.value = true
            }
        }
    }
}