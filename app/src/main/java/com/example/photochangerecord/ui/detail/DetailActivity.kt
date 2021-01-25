package com.example.photochangerecord.ui.detail

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.photochangerecord.R
import com.example.photochangerecord.databinding.ActivityDetailBinding
import com.example.photochangerecord.databinding.DeleteFolderDialogBinding
import com.example.photochangerecord.model.Photo
import com.example.photochangerecord.ui.camera.CameraPermissionActivity
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import splitties.toast.toast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


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

        binding.detailImageView.load(File(photos[currentPosition].absolute_file_path))

        setSlider()

        Log.d(TAG, "onCreate: $currentPosition ${photos} ")

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

        Log.d(TAG, "onResume: 외부 저장소 ${isExternalStorageWritable()} ${isExternalStorageReadable()}")

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
//            R.id.action_detail_save -> {
//                val bitmap = binding.detailImageView.drawable.toBitmap()
//                persistImage(bitmap)
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deletePhoto(filePath: String): Boolean {
        val f = File(filePath)
        return f.delete()
    }

    private fun persistImage(bitmap: Bitmap) {
        try {

            val dir = File(
                this.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES
                ), "PhotoChangeRecord"
            )

            if (!dir!!.exists()) {
                Log.d(TAG, "persistImage: 폴더생성")
                dir.mkdirs()
            }
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
            var time = dateFormat.format(Date())
            val newFile = File(dir, "${time}.jpg")

            val out = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()


            toast("Save Success")
            Log.d(TAG, "persistImage: $dir")



        } catch (e: Exception) {
            Log.d(TAG, "persistImage: $e")
            toast("Save Failed")
        }


    }



    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
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