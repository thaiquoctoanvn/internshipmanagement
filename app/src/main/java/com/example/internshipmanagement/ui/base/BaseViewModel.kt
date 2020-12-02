package com.example.internshipmanagement.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
    get() = _isLoading

    private val _eventFailure = MutableLiveData<Throwable>()
    val eventFailure: LiveData<Throwable>
    get() = _eventFailure

    private val _messageResponse = MutableLiveData<String>()
    val messageResponse: LiveData<String>
    get() = _messageResponse

    fun setIsLoadingValue(value: Boolean) {
        _isLoading.value = value
    }

    fun setEventFailureValue(throwable: Throwable) {
        _eventFailure.value = throwable
    }

    fun setMessageResponseValue(message: String) {
        _messageResponse.value = message
    }


//    fun getIsLoadingValue() = isLoading
//
//    fun getEventFailureValue() = eventFailure
//
//    fun getMessageResponseValue() = messageResponse
}