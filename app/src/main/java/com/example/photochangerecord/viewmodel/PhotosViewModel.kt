package com.example.photochangerecord.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class PhotosViewModel : ViewModel() {
    companion object {
        private const val TAG = "PhotosViewModel"
    }

    private val _photos: MutableLiveData<List<Photo>>? = MutableLiveData()


    val photos: LiveData<List<Photo>>
        get() = _photos!!

    fun updateValue(photos: List<Photo>) {
        Log.d(TAG, "updateValue: 값변경 $photos")
        _photos!!.postValue(photos)
    }
}