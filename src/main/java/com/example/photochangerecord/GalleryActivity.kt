package com.example.photochangerecord

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photochangerecord.databinding.ActivityGalleryBinding
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo
import splitties.activities.start


class GalleryActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "GalleryActivity"
    }

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.elevation = 0.0f
        actionBar!!.title = ""


        mContext = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        val intent = intent
        var receivedFolderInfo: Folder = intent.getParcelableExtra("folderInfo")

        Log.d(TAG, "onCreate: $receivedFolderInfo")

        recyclerview(receivedFolderInfo)

        binding.newPhotoFab.setOnClickListener {
            // TODO 나중에는 찍은 사진을 업데이트해서 보여줘야..
            start<CameraActivity>()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun recyclerview(folder: Folder) {

        val adapter = GalleryAdapter(mContext, folder.photos)
        adapter.itemClick = object : GalleryAdapter.ItemClick {
            override fun onClick(view: View, position: Int, photo: Photo) {
                Log.d(TAG, "onClick: $position clicked")

                // GalleryActivity에서 업데이트 될 수도 있으니까
                val intent = Intent(mContext, DetailActivity::class.java)
                intent.putExtra("photoInfo", photo)
                startActivity(intent)


            }
        }

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