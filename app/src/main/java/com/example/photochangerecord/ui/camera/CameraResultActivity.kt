package com.example.photochangerecord.ui.camera

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.photochangerecord.R
import com.example.photochangerecord.databinding.ActivityCameraResultBinding


class CameraResultActivity : AppCompatActivity() {


    companion object {
        private const val TAG = "CameraResultActivity"
    }

    private lateinit var binding: ActivityCameraResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_result)

        Log.d(TAG, "onCreate: 진입")
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_result)

        val intent = intent
        var folderName = intent.getStringExtra("folderName")
        var thumbnailBitmap = intent.getParcelableExtra<Bitmap>("thumbnailBitmap")

        binding.resultImage.load(thumbnailBitmap)
        binding.okButton.setOnClickListener {
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED, intent)
            finish()
        }

    }

}