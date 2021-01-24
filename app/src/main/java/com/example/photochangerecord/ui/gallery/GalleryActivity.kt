package com.example.photochangerecord.ui.gallery

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photochangerecord.ui.detail.DetailActivity
import com.example.photochangerecord.R
import com.example.photochangerecord.databinding.ActivityGalleryBinding
import com.example.photochangerecord.databinding.AddFolderDialogBinding
import com.example.photochangerecord.databinding.DeleteFolderDialogBinding
import com.example.photochangerecord.ui.adapter.GalleryAdapter
import com.example.photochangerecord.ui.gif.GifActivity
import com.example.photochangerecord.model.Folder
import com.example.photochangerecord.model.Photo
import com.example.photochangerecord.ui.camera.CameraPermissionActivity
import splitties.toast.toast
import java.io.File


class GalleryActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "GalleryActivity"
    }

    var isAllImageToGif = true // 전체사진을 gif로 만들것인지, 다중선택할 것인지

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var mContext: Context
    private lateinit var folderName: String
    private lateinit var viewModel: PhotosViewModel
    private lateinit var adapter : GalleryAdapter

    private var photos: ArrayList<Photo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        mContext = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        val intent = intent
        folderName = intent.getStringExtra("folderName")

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.elevation = 0.0f
        supportActionBar!!.title = folderName

        recyclerview()

        viewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)
        viewModel.photos?.observe(this, {
            it?.let {
                adapter.submitList(it)
            }
        })

//        // 폴더에 저장된 사진이 없다면 바로 카메라 액티비티로 이동
//        if (photos.size == 0) {
////            val intent = Intent(mContext, LaunchActivity::class.java)
////            intent.putExtra("folderName", folderName)
////            startActivity(intent)
//            binding.noImageFrameLayout.visibility = VISIBLE
//        }else{
//            binding.noImageFrameLayout.visibility = GONE
//        }
//        recyclerview()

        binding.newPhotoFab.setOnClickListener {
            val intent = Intent(mContext, CameraPermissionActivity::class.java)
            intent.putExtra("folderName", folderName)

            if (photos.size != 0) {
                intent.putExtra("backgroundPhoto", photos[photos.size - 1])
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume: started")
        getFolder(folderName)
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_gallery, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                return true
            }
            R.id.action_delete_folder -> {
                showDeleteFolderDialog(callback = {
                    if (it) {
                        toast("Delete Success")
                        finish()
                    } else {
                        toast("Delete Failed")
                    }
                })
            }
            R.id.action_rename_folder -> {
                showRenameFolderDialog()
            }
            R.id.action_generate_gif -> {
                if (photos.size != 0) {
                    getImagePathToGif(isAllImageToGif)
                } else {
                    toast("2 or more photos are required")
                }


            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getImagePathToGif(isAllImageToGif: Boolean) {
        val folderPathList = ArrayList<String>()

        // 전체
        if (isAllImageToGif) {
            val folder = File(
                getExternalFilesDir(
                    Environment.DIRECTORY_DCIM
                ).toString() + "/$folderName"
            )

            val folderList = folder.listFiles()

            for (j in folderList.indices) {
                folderPathList.add(folderList[j].absolutePath)
            }

        } else {
            // TODO 다중 선택한 파일의 절대경로를 folderPathList에 담기
        }

        val intent = Intent(this, GifActivity::class.java)
        intent.putExtra("folderPathList", folderPathList)
        startActivity(intent)


    }


    private fun getFolder(folderName: String){

        var directory = File(
            getExternalFilesDir(
                Environment.DIRECTORY_DCIM
            ).toString() + "/$folderName"
        )
        var files = directory.listFiles()

        photos = ArrayList() // 파일 경로

        for (f in files.sortedArray()) {
            photos.add(Photo(f.absolutePath))
        }

        // 폴더에 저장된 사진이 없다면 no image 표시
        if (photos.size == 0) {
            binding.noImageFrameLayout.visibility = VISIBLE
        } else {
            binding.noImageFrameLayout.visibility = GONE
        }

        Log.d(TAG, "getFolder: ${Folder(folderName, photos)}")

        viewModel.updateValue(photos)
    }

    private fun renameFolder(oldFolderName: String, newFolderName: String): Boolean {
        val folder = File(
            getExternalFilesDir(
                Environment.DIRECTORY_DCIM
            ).toString() + "/$oldFolderName"
        )

        folder.renameTo(
            File(
                getExternalFilesDir(
                    Environment.DIRECTORY_DCIM
                ).toString() + "/$newFolderName"
            )
        )

        folderName = newFolderName
        toast("Rename Success")

        finish()

        return true
    }


    private fun deleteFolder(folderName: String): Boolean {
        val deleteFolder = File(
            getExternalFilesDir(
                Environment.DIRECTORY_DCIM
            ).toString() + "/$folderName"
        )

        if (deleteFolder.exists()) {
            val deleteFolderList = deleteFolder.listFiles()
            for (j in deleteFolderList.indices) {
                Log.d(TAG, "deleteFolder: $j 파일삭제 ")
                deleteFolderList[j].delete()
            }
            deleteFolder.delete()
        }
        return true
    }


    private fun showRenameFolderDialog() {
        val binding: AddFolderDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.add_folder_dialog,
            null,
            false
        )

        binding.dialogEt.setText(folderName)

        binding.dialogAgreeBtn.text = "RENAME"

        val dialog = Dialog(this)

        binding.dialogAgreeBtn.setOnClickListener {
            var newFolderName = binding.dialogEt.text.toString()
            if (renameFolder(folderName, newFolderName)) {
//                recyclerview()
                supportActionBar!!.title = newFolderName
                dialog.dismiss()
            } else {
                toast("Check Your Folder Name!")
            }
        }

        binding.dialogDisagreeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showDeleteFolderDialog(callback: (Boolean) -> Unit) {
        val binding: DeleteFolderDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.delete_folder_dialog,
            null,
            false
        )

        val dialog = Dialog(this)
        binding.deleteFileName.text = folderName

        binding.dialogAgreeBtn.setOnClickListener {

            if (deleteFolder(folderName)) {
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


    private fun deletePhoto() {
        // TODO 다중선택?
    }

    private fun recyclerview() {
        val recyclerView = binding.recyclerViewGallery
        adapter = GalleryAdapter(folderName)

        adapter.itemClick = object : GalleryAdapter.ItemClick {
            override fun onClick(view: View, position: Int, folderName: String, photos: ArrayList<Photo>) {
                Log.d(TAG, "onClick: $position clicked")

                val intent = Intent(mContext, DetailActivity::class.java)
                intent.putExtra("folderName", folderName)
                intent.putExtra("photos", photos)
                intent.putExtra("position", position)
                startActivity(intent)
            }
        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
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

