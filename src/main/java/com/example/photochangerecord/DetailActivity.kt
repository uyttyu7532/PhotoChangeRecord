package com.example.photochangerecord

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.photochangerecord.R
import com.example.photochangerecord.databinding.ActivityDetailBinding
import com.example.photochangerecord.databinding.ActivityListBinding
import com.example.photochangerecord.viewmodel.Folder
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
        var receivedPhotoInfo: Photo = intent.getParcelableExtra("photoInfo")

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