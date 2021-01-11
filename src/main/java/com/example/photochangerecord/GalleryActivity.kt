package com.example.photochangerecord

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photochangerecord.databinding.ActivityGalleryBinding
import com.example.photochangerecord.viewmodel.Photo


class GalleryActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "GalleryActivity"
    }

    private lateinit var binding: ActivityGalleryBinding
    var photoList = ArrayList<Photo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)


        recyclerview()
    }

    private fun recyclerview(){
        initializeData()

        val adapter = GalleryAdapter(photoList!!)
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

    private fun initializeData(){
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photoList.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
    }
}