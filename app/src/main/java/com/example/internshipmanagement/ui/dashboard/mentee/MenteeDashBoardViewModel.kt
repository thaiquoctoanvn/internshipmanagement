package com.example.internshipmanagement.ui.dashboard.mentee

import android.content.SharedPreferences
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MenteesTask
import com.example.internshipmanagement.data.repository.MenteeRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class MenteeDashBoardViewModel(
    private val menteeRepository: MenteeRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _menteesTasks = MutableLiveData<MutableList<MenteesTask>>()
    val menteesTasks: LiveData<MutableList<MenteesTask>>
        get() = _menteesTasks

    private val _filteredTasks = MutableLiveData<MutableList<MenteesTask>>()
    val filteredTasks: LiveData<MutableList<MenteesTask>>
        get() = _filteredTasks

    fun getMenteesTask() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val menteeId = sharedPref.getString("userId", "")
            if(!menteeId.isNullOrEmpty()) {
                val res = menteeRepository.getMenteesTasks(menteeId).body()
                if(res != null) {
                    res.sortBy { it.deadline.toLong() >= System.currentTimeMillis() + 86400000 }
                    _menteesTasks.value = res
                }
            }
            super.setIsLoadingValue(false)
        }
    }

    fun filterTasks(keyWords: String) {
        viewModelScope.launch {
            if(TextUtils.isEmpty(keyWords)) {
                _filteredTasks.value = menteesTasks.value
            } else {
                val temp = menteesTasks.value?.filter {
                    it.content.contains(keyWords) || it.deadline.contains(keyWords)
                }?.toMutableList()
                _filteredTasks.value = temp
            }
        }
    }

    fun updateCurrentInteractedItem(id: String): Int {
        var position = -1
        menteesTasks.value?.let { tasks ->
            val targetItem = tasks.find { it.id == id }
            targetItem?.let {
                it.isSubmitted = "1"
                position = tasks.indexOf(it)
                Collections.swap(tasks, position, 0)
            }
        }
        return position
    }
}