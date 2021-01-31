package com.uyttyu7532.photolapse.ui.camera

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.uyttyu7532.photolapse.R
import com.uyttyu7532.photolapse.databinding.ActivityCameraResultBinding
import java.io.File
import java.util.*


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
//        var folderName = intent.getStringExtra("folderName")
        var thumbnailBitmap = intent.getParcelableExtra<Bitmap>("thumbnailBitmap")
        val cachePath = intent.getStringExtra("cachePath")

//        var storage: File = cacheDir

        Log.d(TAG, "onCreate: $cachePath")

//        //storage 에 파일 인스턴스를 생성합니다.
//        var tempFile = File(storage, cachePath)
//        Log.d(TAG, "onCreate: tempFile $tempFile")

//        binding.resultImage.load(thumbnailBitmap)
        binding.resultImage.load(File(cachePath))

        intent.putExtra("cachePath",cachePath)

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