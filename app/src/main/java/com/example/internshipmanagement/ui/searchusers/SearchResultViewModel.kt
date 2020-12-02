package com.example.internshipmanagement.ui.searchusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.data.repository.UserRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SearchResultViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val _searchResult = MutableLiveData<MutableList<UserProfile>>()
    val searchResult: LiveData<MutableList<UserProfile>>
        get() = _searchResult


    suspend fun searchAllUsers(key: String) {
        viewModelScope.launch {
            val res = userRepository.searchAllUsers(key).body()
            if(res != null) {
                _searchResult.value = res
            }
        }
    }
}