package com.example.internshipmanagement.ui.people.mentor

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MentorSearchViewModel (
    private val mentorRepository: MentorRepository,
    private val sharedPref: SharedPreferences
): BaseViewModel() {

    private val _myMentees = MutableLiveData<MutableList<MyMentee>>()
    val myMentees: LiveData<MutableList<MyMentee>>
        get() = _myMentees

    private val _allMentees = MutableLiveData<MutableList<MyMentee>>()
    val allMentees: LiveData<MutableList<MyMentee>>
        get() = _allMentees

    private val _myNewMenteePosition = MutableLiveData<Int>()
    val myNewMenteePosition: LiveData<Int>
        get() = _myNewMenteePosition


    fun getAllMentees() {
        viewModelScope.launch {
            val mentorId = sharedPref.getString("userId", "")
            val res = mentorRepository.getAllMentees(mentorId.toString()).body()
            if(res != null) {
                _allMentees.value = res
            }
        }
    }

    fun getMyMentees() {
        viewModelScope.launch {
            val res = mentorRepository.getMyMentee(sharedPref.getString("userId", "")!!).body()
            if(res != null) {
                _myMentees.value = res
            }
        }
    }

    fun addToMyMentee(menteeId: String) {
        viewModelScope.launch {
            val mentorId = sharedPref.getString("userId", "")
            val res = mentorRepository.addToMyMentee(mentorId.toString(), menteeId).body()
            if(res != null) {
                _myMentees.value?.add(res)
                _myNewMenteePosition.value = _myMentees.value?.size?.minus(1)
            }
        }
    }

    fun setIsMyMenteeState(id: String): Int {
        allMentees.value?.forEachIndexed { index, mentee ->
            if(id == mentee.menteeId) {
                mentee.isMyMentee = "true"
                return index
            }
        }
        return  -1
    }
}