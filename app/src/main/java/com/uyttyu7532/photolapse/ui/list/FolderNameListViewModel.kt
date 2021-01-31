package com.uyttyu7532.photolapse.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uyttyu7532.photolapse.model.FolderName


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