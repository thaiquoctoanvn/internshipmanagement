package com.example.internshipmanagement.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    private val isLoading = MutableLiveData<Boolean>()
    private val eventFailure = MutableLiveData<Throwable>()
    private val messageResponse = MutableLiveData<String>()

    fun setIsLoadingValue(value: Boolean) {
        isLoading.value = value
    }

    fun setEventFailureValue(throwable: Throwable) {
        eventFailure.value = throwable
    }

    fun setMessageResponseValue(message: String) {
        messageResponse.value = message
    }

    fun getIsLoadingValue() = isLoading

    fun getEventFailureValue() = eventFailure

    fun getMessageResponseValue() = messageResponse
}