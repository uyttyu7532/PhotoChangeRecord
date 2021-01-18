package com.example.photochangerecord

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
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
import splitties.toast.toast
import kotlin.math.floor


class DetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DetailActivity"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailSlider: FluidSlider
    private lateinit var receiveFolder: Folder
    private var receivedPosition = 0
    private var folderSize = 0
    private var positionFloat = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)


        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.elevation = 0.0f
        supportActionBar!!.title = ""

        detailSlider = binding.detailSlider


        val intent = intent
        // GalleryActivity에서 업데이트 될 수도 있으니까 전역으로 저장?
        receiveFolder = intent.getParcelableExtra("folder")
        receivedPosition = intent.getIntExtra("position", 0)

        folderSize = receiveFolder.photos.size
        positionFloat = ((receivedPosition).toFloat() / (folderSize - 1).toFloat())

        detailSlider.position = positionFloat
        detailSlider.bubbleText = (receivedPosition + 1).toString()
        detailSlider.startText = ""
        detailSlider.endText = ""


        Glide.with(this).load(receiveFolder.photos[receivedPosition].absolute_file_path)
            .into(binding.detailImageView)

        setSlider()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                return true
            }
            R.id.action_detail_camera -> {
                val intent = Intent(this, LaunchActivity::class.java)
                intent.putExtra("folderName", receiveFolder.title)
                intent.putExtra(
                    "backgroundPhoto",
                    receiveFolder.photos[receivedPosition]
                )
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setSlider() {
        val positionViewModel = PositionViewModel()

        detailSlider.positionListener = {
            var currentPosition = floor(it * (folderSize - 1)).toInt()

            // 라이브 데이터 변경
            positionViewModel.updatePosition(currentPosition)
        }

//        // end시점으로 가장 가까운 위치로 이동 (근데 생각보다 애니메이션이 별로임)
//        detailSlider.endTrackingListener = {
//            var closePosition = positionViewModel.currentDetailPosition.value
//            detailSlider.position =  closePosition!!.toFloat() / (folderSize - 1).toFloat()
//            detailSlider.bubbleText = (closePosition + 1).toString()
//        }


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


}