package com.example.photochangerecord.utils

import android.content.Context
import android.content.SharedPreferences

class SortByPreferenceUtil(context: Context) {
    private val sortByPrefs: SharedPreferences =
        context.getSharedPreferences("sortBy", Context.MODE_PRIVATE)

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sortByPrefs.getBoolean(key, defValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        sortByPrefs.edit().putBoolean(key, value).apply()
    }
}

