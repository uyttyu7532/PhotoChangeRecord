package com.example.photochangerecord.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FolderNameListViewModel : ViewModel() {
    companion object {
        private const val TAG = "FolderNameListViewModel"
    }

    private val _folderNameList: MutableLiveData<List<FolderName>>? = MutableLiveData()


    val folderList: LiveData<List<FolderName>>
        get() = _folderNameList!!

    fun updateValue(folderNameList: List<FolderName>) {
        Log.d(TAG, "updateValue: 값변경 $folderNameList")
        _folderNameList!!.postValue(folderNameList)
    }
}