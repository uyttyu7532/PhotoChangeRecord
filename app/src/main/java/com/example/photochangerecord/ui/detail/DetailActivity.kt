package com.example.photochangerecord.ui.detail

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.photochangerecord.R
import com.example.photochangerecord.databinding.ActivityDetailBinding
import com.example.photochangerecord.databinding.DeleteFolderDialogBinding
import com.example.photochangerecord.ui.camera.CameraPermissionActivity
import com.example.photochangerecord.model.Photo
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import splitties.toast.toast
import java.io.File


class DetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DetailActivity"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailSlider: RangeSeekBar
    private lateinit var photos: ArrayList<Photo>
    private var currentPosition = 0
    private var folderSize = 0
    private var folderName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val intent = intent
        // GalleryActivity에서 업데이트 될 수도 있으니까 전역으로 저장?
        photos = intent.getParcelableArrayListExtra("photos")
        folderName = intent.getStringExtra("folderName")
        currentPosition = intent.getIntExtra("position", 0)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.elevation = 0.0f
        supportActionBar!!.title =
            photos[currentPosition].absolute_file_path.substringAfterLast("/")
                .substringBeforeLast(".jpg")

        detailSlider = binding.detailSlider




        folderSize = photos.size
//        positionFloat = ((receivedPosition).toFloat() / (folderSize - 1).toFloat())

//        detailSlider.position = positionFloat
//        detailSlider.bubbleText = (receivedPosition + 1).toString()
//        detailSlider.startText = ""
//        detailSlider.endText = ""


//        Glide.with(this).load(receiveFolder.photos[receivedPosition].absolute_file_path)
//            .into(binding.detailImageView)

        binding.detailImageView.load(File(photos[currentPosition].absolute_file_path))

        setSlider()

    }

    override fun onResume() {

        if (photos.size == 1) {
            detailSlider.visibility = INVISIBLE
        } else {
            detailSlider.visibility = VISIBLE
            detailSlider.setIndicatorTextDecimalFormat("0")
            detailSlider.steps = folderSize - 1
            detailSlider.setRange(0.0f, (folderSize - 1).toFloat())
            detailSlider.setProgress((currentPosition).toFloat())
        }

        super.onResume()
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
                val intent = Intent(this, CameraPermissionActivity::class.java)
                intent.putExtra("folderName", folderName)
                intent.putExtra("backgroundPhoto", photos[currentPosition])
                startActivity(intent)
                finish()
            }
            R.id.action_delete_detail -> {
                showDeletePhotoDialog(callback = {
                    when (it) {
                        true -> {
                            toast("Delete Success")
                            finish()
                        }
                        false -> toast("Delete Failed")
                    }
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deletePhoto(filePath: String): Boolean {
        val f = File(filePath)
        return f.delete()
    }


    private fun showDeletePhotoDialog(callback: (Boolean) -> Unit) {
        val binding: DeleteFolderDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.delete_folder_dialog,
            null,
            false
        )

        val dialog = Dialog(this)

        binding.deleteFolderDialogTextView.text =
            "Are you sure you want to permanently delete this photo? "
        binding.deleteFileName.text =
            photos[currentPosition].absolute_file_path.substringAfterLast("/")
                .substringBeforeLast(".jpg")

        binding.dialogAgreeBtn.setOnClickListener {

            if (deletePhoto(photos[currentPosition].absolute_file_path)) {
                callback(true)
                dialog.dismiss()
            } else {
                callback(false)
            }

        }

        binding.dialogDisagreeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun setSlider() {
        val positionViewModel = PositionViewModel()

//        detailSlider.positionListener = {
//            var currentPosition = floor(it * (folderSize - 1)).toInt()
//
//            // 라이브 데이터 변경
//            positionViewModel.updatePosition(currentPosition)
//        }

        detailSlider.setOnRangeChangedListener(object :
            OnRangeChangedListener {
            override fun onRangeChanged(
                rangeSeekBar: RangeSeekBar,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                currentPosition = leftValue.toInt()

                // 라이브 데이터 변경
                positionViewModel.updatePosition(currentPosition)
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

        })


//        // end시점으로 가장 가까운 위치로 이동 (근데 생각보다 애니메이션이 별로임)
//        detailSlider.endTrackingListener = {
//            var closePosition = positionViewModel.currentDetailPosition.value
//            detailSlider.position =  closePosition!!.toFloat() / (folderSize - 1).toFloat()
//            detailSlider.bubbleText = (closePosition + 1).toString()
//        }


        //라이브 데이터 변경되면 실행
        positionViewModel.currentDetailPosition.observe(this,
            {
//                detailSlider.bubbleText = (it + 1).toString()
//                binding.detailImageView.load(File(receiveFolder.photos[it].absolute_file_path))
//                detailSlider.bubbleText = (it + 1).toString()


                binding.detailImageView.load(File(photos[it].absolute_file_path))
                supportActionBar!!.title =
                    photos[it].absolute_file_path.substringAfterLast("/")
                        .substringBeforeLast(".jpg")

            })
    }


}