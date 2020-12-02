package com.example.internshipmanagement.ui.people.mentee

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MyMentor
import com.example.internshipmanagement.data.repository.MenteeRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MenteeSearcViewModel(
    private val menteeRepository: MenteeRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _myMentor = MutableLiveData<MutableList<MyMentor>>()
    val myMentor: LiveData<MutableList<MyMentor>>
        get() = _myMentor

    fun getMyMentor() {
        viewModelScope.launch {
            val menteeId = sharedPref.getString("userId", "")
            if(!menteeId.isNullOrEmpty()) {
                val res = menteeRepository.getMyMentor(menteeId).body()
                if(res != null) {
                    _myMentor.value = res
                }
            }
        }
    }
}