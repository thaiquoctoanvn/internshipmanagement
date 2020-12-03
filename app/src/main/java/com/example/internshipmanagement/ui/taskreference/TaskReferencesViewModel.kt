package com.example.internshipmanagement.ui.taskreference

import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.data.repository.MentorRepository
import com.example.internshipmanagement.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class TaskReferencesViewModel(
    private val mentorRepository: MentorRepository,
    private val sharedPref: SharedPreferences
): BaseViewModel() {

    private val _myMenteesForTaskRefer = MutableLiveData<MutableList<MyMentee>>()
    val myMenteesForTaskRefer: LiveData<MutableList<MyMentee>>
        get() = _myMenteesForTaskRefer

    private val _filteredReferences = MutableLiveData<MutableList<MyMentee>>()
    val filteredReferences: LiveData<MutableList<MyMentee>>
        get() = _filteredReferences

    private val _pickedReferences = MutableLiveData<MutableList<MyMentee>>()
    val pickedReferences: LiveData<MutableList<MyMentee>>
        get() = _pickedReferences

    // Vi trí item được chọn
    private val _modifiedItemPosition = MutableLiveData<Int>()
    val modifiedItemPosition: LiveData<Int>
        get() = _modifiedItemPosition

    private val _modifiedFilteredItemPosition = MutableLiveData<Int>()
    val modifiedFilteredItemPosition: LiveData<Int>
        get() = _modifiedFilteredItemPosition

    // Vị trí item khi được thêm vào list chọn
    private val _currentPickedItemPosition = MutableLiveData<Int>()
    val currentPickedItemPosition: LiveData<Int>
        get() = _currentPickedItemPosition

    // Vị trí item bị xóa khỏi list chọn
    private val _currentRemovedItemPosition = MutableLiveData<Int>()
    val currentRemovedItemPosition: LiveData<Int>
        get() = _currentRemovedItemPosition


    fun getMyMenteesForTaskReference(existingList: MutableList<MyMentee>) {
        viewModelScope.launch {
            super.setIsLoadingValue(true)
            val res = mentorRepository.getMyMenteesForTaskReference(sharedPref.getString("userId", "")!!).body()
            if(res != null) {
                existingList.forEach { existingItem ->
                    res.find { existingItem.menteeId == it.menteeId }?.isReferred = existingItem.isReferred
                }
                _myMenteesForTaskRefer.value = res
            }
            super.setIsLoadingValue(false)
        }
    }

    fun filterMentees(keyWords: String) {
        viewModelScope.launch {
            if(TextUtils.isEmpty(keyWords)) {
                _filteredReferences.value = _myMenteesForTaskRefer.value
            } else {
                val temp = _myMenteesForTaskRefer.value?.filter {
                    it.menteeName.contains(keyWords) || it.menteeNickName.contains(keyWords)
                }?.toMutableList()
                _filteredReferences.value = temp
            }
        }
    }

    fun setPickedReferencesValue(pickedReferences: MutableList<MyMentee>) {
        _pickedReferences.value = pickedReferences
    }
    /*
    Chưa handle được :((

    fun setReferState(id: String, isFiltering: Boolean) {
        val selection = myMenteesForTaskRefer.value?.find { it.menteeId == id }
        selection?.let {
            if(it.isReferred == "false") {
                it.isReferred = "true"
                addToPickedList(it)
            } else {
                it.isReferred = "false"
                removeFromPickedList(it)
            }
            println(selection)
            if(isFiltering) {
                _modifiedFilteredItemPosition.value = getModifiedFilteredItemPosition(it)
            } else {
                _modifiedItemPosition.value = getModifiedItemPosition(it)
            }
        }
    }

    // Thêm vào list chọn và trả về vị trí để notify
    private fun addToPickedList(item: MyMentee) {
        _pickedReferences.value?.add(item)
        _currentPickedItemPosition.value = _pickedReferences.value?.size?.minus(1)
        Log.d("###", "pickedListSize: ${pickedReferences.value?.size}")
    }

    private fun removeFromPickedList(selection: MyMentee) {
        val position = pickedReferences.value!!.indexOf(selection)
        _pickedReferences.value!!.remove(selection)
        _currentRemovedItemPosition.value = position
        Log.d("###", "pickedListSizeAfterRemove: ${pickedReferences.value?.size}")
    }

    private fun getModifiedFilteredItemPosition(selection: MyMentee): Int {
        return filteredReferences.value!!.indexOf(selection)
    }

    private fun getModifiedItemPosition(selection: MyMentee): Int {
        return myMenteesForTaskRefer.value!!.indexOf(selection)
    }
    */
}