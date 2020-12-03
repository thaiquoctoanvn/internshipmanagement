package com.example.internshipmanagement.ui.tasksubmission

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.repository.MenteeRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import com.example.internshipmanagement.util.DURATION
import com.example.internshipmanagement.util.SUCCEED_MESSAGE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaskSubmissionViewModel(
    private val menteeRepository: MenteeRepository,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean>
        get() = _isSuccessful

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
        referId: String,
        bitmap: Bitmap,
        taskNote: String
    ) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val userName = sharedPref.getString("userName", "")
            if(userName !== null) {
                val res = menteeRepository.
                uploadTaskMaterialsFromBitmap(context, bitmap, userName, referId, taskNote).body()
                if(res != null) {
                    super.setIsLoadingValue(false)
                    super.setMessageResponseValue("Submit work successfully")
                    delay(500)
                    _isSuccessful.value = res == SUCCEED_MESSAGE
                }
            } else {
                _isSuccessful.value = false
            }
        }
    }

}