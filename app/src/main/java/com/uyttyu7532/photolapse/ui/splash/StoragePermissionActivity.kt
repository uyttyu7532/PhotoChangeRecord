package com.uyttyu7532.photolapse.ui.splash


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.uyttyu7532.photolapse.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class StoragePermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_permission)

        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                // 권한 허용시 실행
                override fun onPermissionGranted() {

                    val intent = Intent(this@StoragePermissionActivity, com.uyttyu7532.photolapse.ui.list.ListActivity::class.java)
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
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }
}