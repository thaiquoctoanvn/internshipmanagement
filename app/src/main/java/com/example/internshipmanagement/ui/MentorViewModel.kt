package com.example.internshipmanagement.ui

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.example.internshipmanagement.util.ERROR_MESSAGE
import kotlinx.coroutines.launch

class MentorViewModel(
    private val mentorRepository: MentorRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {
    private val myMentees = MutableLiveData<MutableList<MyMentee>>()
    private val pickedMentees = MutableLiveData<MutableList<MyMentee>>()

    fun getMyMenteesValue() = myMentees

    fun getPickedMenteesValue() = pickedMentees

    fun getMyMenteesForTaskReference() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            myMentees.value = mentorRepository.getMyMenteesForTaskReference(sharedPref.getString("userId", "")!!)
            super.setIsLoadingValue(false)
        }
    }

    fun addPickedMentee(myMentee: MyMentee) {
        viewModelScope.launch {
            val tempList = pickedMentees.value

        }
    }
}