package com.example.photochangerecord.ui.camera


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.photochangerecord.R
import com.example.photochangerecord.model.Photo
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class CameraPermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_permission)

        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                // 권한 허용시 실행
                override fun onPermissionGranted() {
                    val intentReceived = intent
                    var folderName: String? = intentReceived.getStringExtra("folderName")
                    var backgroundPhoto: Photo? = intentReceived.getParcelableExtra("backgroundPhoto")

                    val intent = Intent(this@CameraPermissionActivity, CameraActivity::class.java)
                    intent.putExtra("folderName", folderName)
                    intent.putExtra("backgroundPhoto", backgroundPhoto)
                    startActivity(intent)
                    finish()
                }

                // 권한 거부시 실행
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    for(i in deniedPermissions!!)
                        Log.i("permissionDenied", i)
                    finish()
                }

            })
            .setDeniedMessage("You need to grant permission to run the app.")
            .setPermissions(Manifest.permission.CAMERA)
            .check()
    }
}