package com.example.internshipmanagement.ui

import android.content.SharedPreferences
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MenteesTask
import com.example.internshipmanagement.data.repository.MenteeRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MenteeViewModel(
    private val menteeRepository: MenteeRepository,
    private val sharedPref: SharedPreferences
): BaseViewModel() {

    private val menteesTasks = MutableLiveData<MutableList<MenteesTask>>()
    private val filteredTasks = MutableLiveData<MutableList<MenteesTask>>()

    fun getMenteesTasksValue() = menteesTasks
    fun getFilteredTasksValue() = filteredTasks

    fun getMenteesTask() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val menteeId = sharedPref.getString("userId", "")
            if(!menteeId.isNullOrEmpty()) {
                val res = menteeRepository.getMenteesTasks(menteeId).body()
                if(res != null) {
                    menteesTasks.value = res
                }
            }
            super.setIsLoadingValue(false)
        }
    }

    fun filterTasks(keyWords: String) {
        viewModelScope.launch {
            if(TextUtils.isEmpty(keyWords)) {
                filteredTasks.value = menteesTasks.value
            } else {
                val temp = menteesTasks.value?.filter {
                    it.content.contains(keyWords) || it.deadline.contains(keyWords)
                }?.toMutableList()
                filteredTasks.value = temp
            }
        }
    }
}