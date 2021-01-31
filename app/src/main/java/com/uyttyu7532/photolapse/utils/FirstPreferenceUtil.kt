package com.uyttyu7532.photolapse.utils

import android.content.Context
import android.content.SharedPreferences

class FirstPreferenceUtil(context: Context) {
    private val firstPrefs: SharedPreferences =
        context.getSharedPreferences("appFirst", Context.MODE_PRIVATE)

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return firstPrefs.getBoolean(key, defValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        firstPrefs.edit().putBoolean(key, value).apply()
    }
}

