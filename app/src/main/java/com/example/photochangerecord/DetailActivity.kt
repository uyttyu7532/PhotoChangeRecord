package com.example.photochangerecord

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.photochangerecord.databinding.ActivityDetailBinding
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.PositionViewModel
import com.ramotion.fluidslider.FluidSlider
import kotlin.math.floor


class DetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DetailActivity"
    }

    lateinit var binding: ActivityDetailBinding
    lateinit var detailSlider: FluidSlider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)


        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.elevation = 0.0f
        actionBar!!.title = ""

        detailSlider = binding.detailSlider


        val intent = intent
        // GalleryActivity에서 업데이트 될 수도 있으니까 전역으로 저장?
        var receiveFolder: Folder = intent.getParcelableExtra("folder")
        var receivedPosition: Int = intent.getIntExtra("position", 0)

        var folderSize = receiveFolder.photos.size
        var positionFloat: Float = ((receivedPosition).toFloat() / (folderSize - 1).toFloat())

        detailSlider.position = positionFloat
        detailSlider.bubbleText = (receivedPosition + 1).toString()
        detailSlider.startText = ""
        detailSlider.endText = ""


        Glide.with(this).load(receiveFolder.photos[receivedPosition].absolute_file_path)
            .into(binding.detailImageView)


        val positionViewModel = PositionViewModel()

        detailSlider.positionListener = {
            var currentPosition = floor(it * (folderSize - 1)).toInt()

            // 라이브 데이터 변경
            positionViewModel.updatePosition(currentPosition)
        }

        detailSlider.endTrackingListener = {
            // TODO end 시점에서 가장 가까운 위치에 놓고 싶음
            Log.d(TAG, "onCreate: end = ${detailSlider.position}")
        }



        //라이브 데이터 변경되면 실행
        positionViewModel.currentDetailPosition.observe(this,
            {
                Glide.with(this).load(receiveFolder.photos[it].absolute_file_path)
                    .into(binding.detailImageView)
                detailSlider.bubbleText = (it + 1).toString()

                Glide.with(this).load(receiveFolder.photos[it].absolute_file_path)
                    .diskCacheStrategy(
                        DiskCacheStrategy.AUTOMATIC
                    ).dontAnimate()
                    .into(binding.detailImageView)

                detailSlider.bubbleText = (it + 1).toString()

            })
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