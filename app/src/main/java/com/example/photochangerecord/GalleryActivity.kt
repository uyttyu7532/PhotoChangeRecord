package com.example.photochangerecord

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
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photochangerecord.databinding.ActivityGalleryBinding
import com.example.photochangerecord.databinding.DeleteFolderDialogBinding
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo
import splitties.toast.toast
import java.io.File


class GalleryActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "GalleryActivity"
    }

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var mContext: Context
    private lateinit var folderName: String
    private var photos:ArrayList<Photo> = ArrayList()

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




        binding.newPhotoFab.setOnClickListener {
            val intent = Intent(mContext, LaunchActivity::class.java)
            intent.putExtra("folderName", folderName)
            intent.putExtra("backgroundPhoto", photos[photos.size-1])
            startActivity(intent)
        }

    }

    override fun onResume() {
        recyclerview(getFolder(folderName))
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
                    if(it){
                        toast("Delete Success")
                        finish()
                    }
                    else{
                        toast("Delete Failed")
                    }
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getFolder(folderName: String): Folder {

        var directory = File(
            getExternalFilesDir(
                Environment.DIRECTORY_DCIM
            ).toString() + "/$folderName"
        )
        var files = directory.listFiles()

//        var photos: ArrayList<Photo> = ArrayList() // 파일 경로

        for (f in files) {
            photos.add(Photo(f.absolutePath))
        }
        return Folder(folderName, photos)
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
                deleteFolderList[j].delete()
            }
            if (deleteFolderList.isEmpty() && deleteFolder.isDirectory) {
                deleteFolder.delete()
            }
        }
        return true
    }

    private fun showDeleteFolderDialog(callback: (Boolean) -> Unit) {
        val binding: DeleteFolderDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.delete_folder_dialog,
            null,
            false
        )

        val dialog = Dialog(this)

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


    private fun deletePhotor() {
        // TODO
    }

    private fun recyclerview(folder: Folder) {

        val adapter = GalleryAdapter(mContext, folder)
//        adapter.setHasStableIds(true)
        adapter.itemClick = object : GalleryAdapter.ItemClick {
            override fun onClick(view: View, position: Int, folder: Folder) {
                Log.d(TAG, "onClick: $position clicked")

                val intent = Intent(mContext, DetailActivity::class.java)
                intent.putExtra("folder", folder)
                intent.putExtra("position", position)
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