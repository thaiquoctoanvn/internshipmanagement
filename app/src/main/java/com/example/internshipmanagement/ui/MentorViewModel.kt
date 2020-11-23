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

class MentorViewModel(
    private val mentorRepository: MentorRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private val _myMentees = MutableLiveData<MutableList<MyMentee>>()
    val myMentees: LiveData<MutableList<MyMentee>>
        get() = _myMentees

    private val _selectedReferences = MutableLiveData<MutableList<MyMentee>>()
    val selectedReferences: LiveData<MutableList<MyMentee>>
        get() = _selectedReferences

    private val _filteredReferences = MutableLiveData<MutableList<MyMentee>>()
    val filteredReferences: LiveData<MutableList<MyMentee>>
        get() = _filteredReferences

    private val _mentorsTasks = MutableLiveData<MutableList<MentorsTask>>()
    val mentorsTasks: LiveData<MutableList<MentorsTask>>
        get() = _mentorsTasks

    private val _taskReferences = MutableLiveData<MutableList<TaskReference>>()
    val taskReferences: LiveData<MutableList<TaskReference>>
        get() = _taskReferences

    private val _allMentees = MutableLiveData<MutableList<MyMentee>>()
    val allMentees: LiveData<MutableList<MyMentee>>
        get() = _allMentees





    private val _menteesEvaluations = MutableLiveData<MutableList<MenteesEvaluation>>()
    val menteesEvaluations: LiveData<MutableList<MenteesEvaluation>>
    get() = _menteesEvaluations

    private val _isMyMentee = MutableLiveData<Boolean>()
    val isMyMentee: LiveData<Boolean>
    get() = _isMyMentee

    private val _filteredTasks = MutableLiveData<MutableList<MentorsTask>>()
    val filteredTasks: LiveData<MutableList<MentorsTask>>
    get() = _filteredTasks

    private val _criteria = MutableLiveData<MutableList<Criterion>>()
    val criteria: LiveData<MutableList<Criterion>>
    get() = _criteria

    private val _detailReference = MutableLiveData<DetailTaskReference>()
    val detailReference: LiveData<DetailTaskReference>
    get() = _detailReference

//    fun getIsSucceedValue() = isSucceed
//    fun getMyMenteesValue() = myMentees
//    fun getFilterListValue() = filteredReferences
//    fun getMentorsTasksValue() = mentorsTasks
//    fun getTaskReferencesValue() = taskReferences

//    fun getAllMenteesValue() = allMentees
//    fun getMenteesEvaluationsValue() = menteesEvaluations
//    fun getIsMyMenteeValue() = isMyMentee
//    fun getFilteredTasksValue() = filteredTasks
//    fun getCriteriaValue() = criteria
//    fun getDetailReferenceValue() = detailReference

    fun getSharedPref() = sharedPref

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

//    fun observerSelectedItem(id: String) {
//        val selection = _myMentees.value?.find {
//            it.menteeId == id
//        }
//        if(selection != null) {
//            if(selection.isReferred == "false") {
//                selection.isReferred = "true"
//
//            }
//        }
//    }

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
                _isSuccessful.value = res != null
                super.setIsLoadingValue(false)
            }

        }
    }

    fun getMentorsTasks() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val mentorId = sharedPref.getString("userId", "")
            if(!mentorId.isNullOrEmpty()) {
                val res = mentorRepository.getMentorsTasks(mentorId).body()
                if(res != null) {
                    _mentorsTasks.value = res
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
                _taskReferences.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun getAllMentees() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getAllMentees().body()
            if(res != null) {
                _allMentees.value = res
            }
        }
    }

    fun getMyMentees() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getMyMentee(sharedPref.getString("userId", "")!!).body()
            if(res != null) {
                _myMentees.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

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

    fun generateCriteria() {
        viewModelScope.launch {
            _criteria.value = FunctionHelper.provideCriteria()
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
            _isSuccessful.value = res != null
        }
    }

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
            if(res != null) {
                super.setMessageResponseValue("Reviewed successfully")
                delay(2000)
                _isSuccessful.value = true
            }
            super.setIsLoadingValue(false)
        }
    }
}