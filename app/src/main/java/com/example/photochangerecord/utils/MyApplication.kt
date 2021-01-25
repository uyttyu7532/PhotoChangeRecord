package com.example.photochangerecord.utils

import android.app.Application

class MyApplication : Application() {
    companion object {
        lateinit var prefsAlpha: AlphaPreferenceUtil
        lateinit var prefsSortBy: SortByPreferenceUtil


    }

    override fun onCreate() {
        prefsAlpha = AlphaPreferenceUtil(applicationContext)
        prefsSortBy = SortByPreferenceUtil(applicationContext)
        super.onCreate()
    }
}
