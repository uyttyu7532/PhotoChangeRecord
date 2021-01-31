package com.uyttyu7532.photolapse.utils

import android.app.Application

class MyApplication : Application() {
    companion object {
        lateinit var prefsAlpha: AlphaPreferenceUtil
        lateinit var prefsSortBy: SortByPreferenceUtil
        lateinit var prefsFirst: FirstPreferenceUtil


    }

    override fun onCreate() {
        prefsAlpha = AlphaPreferenceUtil(applicationContext)
        prefsSortBy = SortByPreferenceUtil(applicationContext)
        prefsFirst = FirstPreferenceUtil(applicationContext)
        super.onCreate()
    }
}
