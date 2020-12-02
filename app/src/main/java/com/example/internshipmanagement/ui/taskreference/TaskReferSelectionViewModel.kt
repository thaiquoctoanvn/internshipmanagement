package com.example.internshipmanagement.ui.taskreference

import android.content.SharedPreferences
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class TaskReferSelectionViewModel(
    private val mentorRepository: MentorRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _myMenteesForTaskRefer = MutableLiveData<MutableList<MyMentee>>()
    val myMenteesForTaskRefer: LiveData<MutableList<MyMentee>>
        get() = _myMenteesForTaskRefer

    private val _filteredReferences = MutableLiveData<MutableList<MyMentee>>()
    val filteredReferences: LiveData<MutableList<MyMentee>>
        get() = _filteredReferences


    fun getMyMenteesForTaskReference(existingList: MutableList<MyMentee>) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getMyMenteesForTaskReference(sharedPref.getString("userId", "")!!).body()
            if(res != null) {
                existingList.forEach { existingItem ->
                    res.find { existingItem.menteeId == it.menteeId }?.isReferred = existingItem.isReferred
                }
                _myMenteesForTaskRefer.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun filterMentees(keyWords: String) {
        viewModelScope.launch {
            if(TextUtils.isEmpty(keyWords)) {
                _filteredReferences.value = _myMenteesForTaskRefer.value
            } else {
                val temp = _myMenteesForTaskRefer.value?.filter {
                    it.menteeName.contains(keyWords) || it.menteeNickName.contains(keyWords)
                }?.toMutableList()
                _filteredReferences.value = temp
            }
        }
    }
}