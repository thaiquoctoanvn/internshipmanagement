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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MentorViewModel(
    private val mentorRepository: MentorRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    // refactor
    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    // refactor
    private val _myMentees = MutableLiveData<MutableList<MyMentee>>()
    val myMentees: LiveData<MutableList<MyMentee>>
        get() = _myMentees

    // refactor
    private val _selectedReferences = MutableLiveData<MutableList<MyMentee>>()
    val selectedReferences: LiveData<MutableList<MyMentee>>
        get() = _selectedReferences

    // refactor
    private val _filteredReferences = MutableLiveData<MutableList<MyMentee>>()
    val filteredReferences: LiveData<MutableList<MyMentee>>
        get() = _filteredReferences

    // refactor
    private val _mentorsTasks = MutableLiveData<MutableList<MentorsTask>>()
    val mentorsTasks: LiveData<MutableList<MentorsTask>>
        get() = _mentorsTasks

    // refactor
    private val _taskReferences = MutableLiveData<MutableList<TaskReference>>()
    val taskReferences: LiveData<MutableList<TaskReference>>
        get() = _taskReferences

    // refactor
    private val _allMentees = MutableLiveData<MutableList<MyMentee>>()
    val allMentees: LiveData<MutableList<MyMentee>>
        get() = _allMentees

    // refactor
    private val _menteesEvaluations = MutableLiveData<MutableList<MenteesEvaluation>>()
    val menteesEvaluations: LiveData<MutableList<MenteesEvaluation>>
        get() = _menteesEvaluations

    // refactor
    private val _isMyMentee = MutableLiveData<Boolean>()
    val isMyMentee: LiveData<Boolean>
        get() = _isMyMentee

    // refactor
    private val _filteredTasks = MutableLiveData<MutableList<MentorsTask>>()
    val filteredTasks: LiveData<MutableList<MentorsTask>>
        get() = _filteredTasks

    // refactor
    private val _criteria = MutableLiveData<MutableList<Criterion>>()
    val criteria: LiveData<MutableList<Criterion>>
        get() = _criteria

    // refactor
    private val _detailReference = MutableLiveData<DetailTaskReference>()
    val detailReference: LiveData<DetailTaskReference>
        get() = _detailReference

    // refactor
    private val _myNewMenteePosition = MutableLiveData<Int>()
    val myNewMenteePosition: LiveData<Int>
        get() = _myNewMenteePosition


    fun getSharedPref() = sharedPref

    // refactor
    fun getMyMenteesForTaskReference(existingList: MutableList<MyMentee>) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getMyMenteesForTaskReference(sharedPref.getString("userId", "")!!).body()
            if(res != null) {
                existingList.forEach { existingItem ->
                    res.find { existingItem.menteeId == it.menteeId }?.isReferred = existingItem.isReferred
                }
                _myMentees.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    // refactor
    fun filterMentees(keyWords: String) {
        viewModelScope.launch {
            if(TextUtils.isEmpty(keyWords)) {
                _filteredReferences.value = _myMentees.value
            } else {
                val temp = _myMentees.value?.filter {
                    it.menteeName.contains(keyWords) || it.menteeNickName.contains(keyWords)
                }?.toMutableList()
                _filteredReferences.value = temp
            }
        }
    }

    // refactor
    fun filterTasks(keyWords: String) {
        viewModelScope.launch {
            if(TextUtils.isEmpty(keyWords)) {
                _filteredTasks.value = mentorsTasks.value
            } else {
                val temp = mentorsTasks.value?.filter {
                    it.content.contains(keyWords) || it.deadline.contains(keyWords)
                }?.toMutableList()
                _filteredTasks.value = temp
            }
        }
    }

    // refactor
    fun addNewTask(deadline: String, taskBody: String, referencesList: MutableList<MyMentee>) {
        viewModelScope.launch {
            val ownerId = sharedPref.getString("userId", "")
            if(!ownerId.isNullOrEmpty()) {
                val menteeIds = mutableListOf<String>()
                val gcmIds = mutableListOf<String>()
                referencesList.forEach {
                    menteeIds.add(it.menteeId)
                    gcmIds.add(it.gcmId)
                }
                // Các điều kiện đã thỏa
                super.setIsLoadingValue(true)
                val res = mentorRepository.addNewTask(ownerId, deadline, taskBody, menteeIds, gcmIds).body()
                super.setIsLoadingValue(false)
                delay(1000)
                _isSuccessful.value = res != null
            }

        }
    }

    // refactor
    fun getMentorsTasks() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val mentorId = sharedPref.getString("userId", "")
            if(!mentorId.isNullOrEmpty()) {
                val res = mentorRepository.getMentorsTasks(mentorId).body()
                if(res != null) {
                    res.sortByDescending { it.deadline.toLong() }
                    _mentorsTasks.value = res
                }
            }
            super.setIsLoadingValue(false)
        }
    }

    // refactor
    fun getTaskReferences(taskId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getTaskReferences(taskId).body()
            if(res != null) {
                _taskReferences.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    // refactor
    fun getAllMentees() {
        viewModelScope.launch {
            val mentorId = sharedPref.getString("userId", "")
            val res = mentorRepository.getAllMentees(mentorId.toString()).body()
            if(res != null) {
                _allMentees.value = res
            }
        }
    }

    // refactor
    fun getMyMentees() {
        viewModelScope.launch {
            val res = mentorRepository.getMyMentee(sharedPref.getString("userId", "")!!).body()
            if(res != null) {
                _myMentees.value = res
            }
        }
    }

    // refactor
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

    // refactor
    fun isMyMentee(userId: String, myId: String) {
        viewModelScope.launch {
            val res = mentorRepository.isMyMentee(userId, myId).body()
            if(res != null) {
                _isMyMentee.value = res == "true"
            }
        }
    }

    // refactor
    fun generateCriteria() {
        viewModelScope.launch {
            _criteria.value = FunctionHelper.provideCriteria()
        }
    }

    // refactor
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

    // refactor
    fun hasMarked(): Boolean {
        criteria.value?.forEach {
            if(it.mark.toInt() <= 0) {
                return false
            }
        }
        return true
    }

    // refactor
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

    // refactor
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

    // refactor
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

    // refactor
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

    // refactor
    fun updateCurrentInteractedItem(id: String): Int {
        var position = -1
        mentorsTasks.value?.let { tasks ->
            val targetItem = tasks.find { it.taskId == id }
            targetItem?.let {
                position = tasks.indexOf(it)
                Collections.swap(tasks, position, 0)
            }
        }
        return position
    }

    // refactor
    fun setReviewedStateReference(id: String): Int {
        taskReferences.value?.forEachIndexed { index, taskReference ->
            if(id == taskReference.id) {
                taskReference.isReviewed = "1"
                return index
            }
        }
        return -1
    }

    // refactor
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