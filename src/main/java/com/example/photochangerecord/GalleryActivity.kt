package com.example.photochangerecord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

lateinit var myCameraBackGroundViewModel: CameraBackGroundViewModel

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        myCameraBackGroundViewModel = ViewModelProvider(this).get(CameraBackGroundViewModel::class.java)
    }
}