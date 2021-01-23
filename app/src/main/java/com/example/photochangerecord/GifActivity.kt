package com.example.photochangerecord

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.photochangerecord.databinding.ActivityGifBinding
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.util.*

class GifActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGifBinding
    private lateinit var folderPathList: ArrayList<String>

    companion object {
        private const val TAG = "GifActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_gif)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.elevation = 0.0f
        supportActionBar!!.title = ""

        val gifSlider = binding.gifSlider
        gifSlider.visibility = View.VISIBLE
        gifSlider.steps = 5


        val intentReceived = intent
        folderPathList = intentReceived.getStringArrayListExtra("folderPathList")
        Log.d(TAG, "onCreate: ${folderPathList}")

        changeGif()

        MakeGif.makeGif(binding.imageanim, 0.0f, folderPathList)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_gif, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                return true
            }
            R.id.action_save -> {
                // TODO gif로 저장
            }
            R.id.action_share -> {
                // TODO 공유
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun changeGif() {
        val gifSlider = binding.gifSlider

        var tempValue: Float = 0.0f

        gifSlider.setOnRangeChangedListener(
            object :

                OnRangeChangedListener {

                override fun onRangeChanged(
                    rangeSeekBar: RangeSeekBar,
                    leftValue: Float,
                    rightValue: Float,
                    isFromUser: Boolean
                ) {
                    tempValue = leftValue
                }

                override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

                }

                override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                    Log.d(TAG, "onStopTrackingTouch: $tempValue")
                    MakeGif.makeGif(binding.imageanim, tempValue, folderPathList)
                }

            })
    }


    object MakeGif {
        fun makeGif(view: ImageView, value: Float, folderPathList: ArrayList<String>) {
            var animation = AnimationDrawable()

            var velocity = 1000
            if (value != 0.0f) {
                velocity = (1000 - value * 10).toInt()
            }
            Log.d(TAG, "makeGif: $value $velocity")

            for (j in folderPathList.indices) {
                animation.addFrame(Drawable.createFromPath(folderPathList[j])!!, velocity)
            }

            animation.isOneShot = false
            view.load(animation) {
                crossfade(true)
                crossfade(200)
                placeholder(R.drawable.loading)
            }
        }
    }

}

