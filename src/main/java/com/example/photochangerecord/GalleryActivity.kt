package com.example.photochangerecord

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photochangerecord.databinding.ActivityGalleryBinding
import com.example.photochangerecord.viewmodel.Folder
import splitties.activities.start


class GalleryActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "GalleryActivity"
    }

    private lateinit var binding: ActivityGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        val intent = intent
        var receivedFolderInfo: Folder = intent.getParcelableExtra("folderInfo")

        Log.d(TAG, "onCreate: $receivedFolderInfo")

        recyclerview(receivedFolderInfo)

        binding.newPhotoFab.setOnClickListener{
            // TODO 나중에는 찍은 사진을 업데이트해서 보여줘야..
            start<CameraActivity>()
        }
    }

    private fun recyclerview(folder : Folder){

        val adapter = GalleryAdapter(folder.photos)
        val recyclerView = binding.recyclerViewGallery
        val manager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter

        val nsv: NestedScrollView = binding.nestedScrollViewGallery
        nsv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                binding.newPhotoFab.hide()
            } else {
                binding.newPhotoFab.show()
            }
        })

    }

}