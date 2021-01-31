package com.uyttyu7532.photolapse.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.uyttyu7532.photolapse.R
import com.uyttyu7532.photolapse.ui.list.ListActivity


class SplashActivity : Activity(){

    val TIME_OUT : Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            kotlin.run {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, TIME_OUT)
    }
}