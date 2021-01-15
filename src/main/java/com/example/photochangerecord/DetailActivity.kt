package com.example.photochangerecord

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.photochangerecord.databinding.ActivityDetailBinding
import com.example.photochangerecord.viewmodel.Photo


class DetailActivity : AppCompatActivity() {


    lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.elevation = 0.0f
        actionBar!!.title = ""

        val intent = intent
        // GalleryActivity에서 업데이트 될 수도 있으니까 전역으로 저장?
        var receivedPhotoInfo: Photo = intent.getParcelableExtra("photoInfo") // TODO 나중에 지우기
        var folderName = intent.getStringExtra("folderName")
        var photoPosition = intent.getIntExtra("photoPosition",0)

        // TODO folder이름과 position으로 이미지 표시해야함!
        Glide.with(this).load(receivedPhotoInfo.resourceID).into(binding.detailImageView)
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
}