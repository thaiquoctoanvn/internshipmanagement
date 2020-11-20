package com.example.internshipmanagement.ui

import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.*
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.example.internshipmanagement.util.ADD_TASK_SUCCESSFULLY
import com.example.internshipmanagement.util.ERROR_MESSAGE
import com.example.internshipmanagement.util.FunctionHelper
import com.example.internshipmanagement.util.SUCCEED_MESSAGE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MentorViewModel(
    private val mentorRepository: MentorRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val isSucceed = MutableLiveData<Boolean>()
    private val myMentees = MutableLiveData<MutableList<MyMentee>>()
    private val filteredReferences = MutableLiveData<MutableList<MyMentee>>()
    private val mentorsTasks = MutableLiveData<MutableList<MentorsTask>>()
    private val taskReferences = MutableLiveData<MutableList<TaskReference>>()
    private val allMentees = MutableLiveData<MutableList<MyMentee>>()
    private val menteesEvaluations = MutableLiveData<MutableList<MenteesEvaluation>>()
    private val isMyMentee = MutableLiveData<Boolean>()
    private val filteredTasks = MutableLiveData<MutableList<MentorsTask>>()
    private val criteria = MutableLiveData<MutableList<Criterion>>()
    private val detailReference = MutableLiveData<DetailTaskReference>()

    fun getIsSucceedValue() = isSucceed
    fun getMyMenteesValue() = myMentees
    fun getFilterListValue() = filteredReferences
    fun getMentorsTasksValue() = mentorsTasks
    fun getTaskReferencesValue() = taskReferences
    fun getSharedPref() = sharedPref
    fun getAllMenteesValue() = allMentees
    fun getMenteesEvaluationsValue() = menteesEvaluations
    fun getIsMyMenteeValue() = isMyMentee
    fun getFilteredTasksValue() = filteredTasks
    fun getCriteriaValue() = criteria
    fun getDetailReferenceValue() = detailReference

    fun getMyMenteesForTaskReference(existingList: MutableList<MyMentee>) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getMyMenteesForTaskReference(sharedPref.getString("userId", "")!!).body()
            if(res != null) {
                existingList.forEach { existingItem ->
                    res.find { existingItem.menteeId == it.menteeId }?.isReferred = existingItem.isReferred
                }
                myMentees.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun filterMentees(srcList: MutableList<MyMentee>, keyWords: String) {
        viewModelScope.launch {
            val temp = srcList.filter {
                it.menteeName.contains(keyWords) || it.menteeNickName.contains(keyWords)
            }?.toMutableList()
            filteredReferences.value = temp
        }
    }

    fun filterTasks(keyWords: String) {
        viewModelScope.launch {
            if(TextUtils.isEmpty(keyWords)) {
                filteredTasks.value = mentorsTasks.value
            } else {
                val temp = mentorsTasks.value?.filter {
                    it.content.contains(keyWords) || it.deadline.contains(keyWords)
                }?.toMutableList()
                filteredTasks.value = temp
            }
        }
    }

    fun addNewTask(ownerId: String, deadline: String, taskBody: String, menteeIds: MutableList<String>, gcmIds: MutableList<String>) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.addNewTask(ownerId, deadline, taskBody, menteeIds, gcmIds).body()
            isSucceed.value = res != null
            super.setIsLoadingValue(false)
        }
    }

    fun getMentorsTasks() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val mentorId = sharedPref.getString("userId", "")
            if(!mentorId.isNullOrEmpty()) {
                val res = mentorRepository.getMentorsTasks(mentorId).body()
                if(res != null) {
                    mentorsTasks.value = res
                }
            }
            super.setIsLoadingValue(false)
        }
    }

    fun getTaskReferences(taskId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getTaskReferences(taskId).body()
            if(res != null) {
                taskReferences.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun getAllMentees() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getAllMentees().body()
            if(res != null) {
                allMentees.value = res
            }
        }
    }

    fun getMyMentees() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getMyMentee(sharedPref.getString("userId", "")!!).body()
            if(res != null) {
                myMentees.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun getMenteesEvaluations(userId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getMenteesEvaluations(userId).body()
            if(res != null) {
                menteesEvaluations.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun isMyMentee(userId: String, myId: String) {
        viewModelScope.launch {
            val res = mentorRepository.isMyMentee(userId, myId).body()
            if(res != null) {
                isMyMentee.value = res == "true"
            }
        }
    }

    fun generateCriteria() {
        viewModelScope.launch {
            criteria.value = FunctionHelper.provideCriteria()
        }
    }

    fun calculateCriteriaMark(): Float? {
        var average = 0F
        var counter = 0
        criteria.value?.forEach {
            if(it.mark.toInt() < 0 || it.mark.toInt() > 10) {
                return null
            }
            average += it.mark.toInt()
            counter++
        }
        return average / counter
    }

    fun addEvaluation(
        mentorId: String,
        menteeId: String,
        fromDate: String,
        toDate: String,
        evaluation: String,
        mark: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.addEvaluation(mentorId, menteeId, fromDate, toDate, evaluation, mark).body()
            super.setIsLoadingValue(false)
            isSucceed.value = res != null
        }
    }

    fun getDetailReference(referenceId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getDetailReference(referenceId).body()
            if(res != null) {
                detailReference.value = res
            }
            super.setIsLoadingValue(false)
        }
    }
}