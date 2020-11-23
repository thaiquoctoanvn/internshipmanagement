package com.example.internshipmanagement.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MenteeTaskDetail
import com.example.internshipmanagement.data.entity.MenteesTask
import com.example.internshipmanagement.data.repository.MenteeRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.example.internshipmanagement.util.SUCCEED_MESSAGE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MenteeViewModel(
    private val menteeRepository: MenteeRepository,
    private val sharedPref: SharedPreferences
): BaseViewModel() {

    private val _menteesTasks = MutableLiveData<MutableList<MenteesTask>>()
    val menteesTasks: LiveData<MutableList<MenteesTask>>
        get() = _menteesTasks

    private val _filteredTasks = MutableLiveData<MutableList<MenteesTask>>()
    val filteredTasks: LiveData<MutableList<MenteesTask>>
        get() = _filteredTasks

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

    private val _menteeTaskDetail = MutableLiveData<MenteeTaskDetail>()
    val menteeTaskDetail: LiveData<MenteeTaskDetail>
        get() = _menteeTaskDetail


    fun getMenteesTask() {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val menteeId = sharedPref.getString("userId", "")
            if(!menteeId.isNullOrEmpty()) {
                val res = menteeRepository.getMenteesTasks(menteeId).body()
                if(res != null) {
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

    fun getMenteeTaskDetail(referId: String) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = menteeRepository.getMenteeTaskDetail(referId).body()
            if(res != null) {
                _menteeTaskDetail.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun uploadMaterialSFromUri(
        context: Context,
        referId: String,
        urls: MutableList<String>,
        taskNote: String
    ) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val userId = sharedPref.getString("userId", "")
            if(userId !== null) {
                val res = menteeRepository.
                uploadTaskMaterialsFromUri(context, urls, userId, referId, taskNote).body()
                if(res != null) {
                    super.setIsLoadingValue(false)
                    super.setMessageResponseValue("Submit work successfully")
                    delay(2000)
                    _isSuccessful.value = res == SUCCEED_MESSAGE
                }
            } else {
                super.setIsLoadingValue(false)
                _isSuccessful.value = false
            }
        }
    }

    fun uploadMaterialsFromBitmap(
        context: Context,
        bitmap: Bitmap,
        taskNote: String
    ) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val userName = sharedPref.getString("userName", "")
            val referId = _menteeTaskDetail.value!!.id
            if(userName !== null) {
                val res = menteeRepository.
                uploadTaskMaterialsFromBitmap(context, bitmap, userName, referId, taskNote).body()
                if(res != null) {
                    super.setIsLoadingValue(false)
                    super.setMessageResponseValue("Submit work successfully")
                    delay(2000)
                    _isSuccessful.value = res == SUCCEED_MESSAGE
                }
            } else {
                _isSuccessful.value = false
            }
        }
    }
}