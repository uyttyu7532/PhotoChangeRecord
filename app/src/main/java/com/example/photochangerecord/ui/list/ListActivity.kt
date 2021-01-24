package com.example.photochangerecord.ui.list

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photochangerecord.ui.gallery.GalleryActivity
import com.example.photochangerecord.R
import com.example.photochangerecord.databinding.ActivityListBinding
import com.example.photochangerecord.databinding.AddFolderDialogBinding
import com.example.photochangerecord.model.FolderName
import com.example.photochangerecord.ui.adapter.ListVerticalAdapter
import splitties.toast.toast
import java.io.File


class ListActivity : AppCompatActivity() {
    private var mContext: Context? = null

    companion object {
        private const val TAG = "ListActivity"
    }

    private lateinit var binding: ActivityListBinding
    private lateinit var viewModel: FolderNameListViewModel
    private lateinit var adapter : ListVerticalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        recyclerview()

        viewModel = ViewModelProvider(this).get(FolderNameListViewModel::class.java)
        viewModel.folderList?.observe(this, {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.newFolderFab.setOnClickListener {
            showMakeFolderDialog()
        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        getFolderName()
        super.onResume()
    }

    private fun getFolderName() {

        var directory = File(
            getExternalFilesDir(
                Environment.DIRECTORY_DCIM
            ).toString()
        )
        var files = directory.listFiles().sortedArray()
        val filesNameList = ArrayList<FolderName>()
        for (f in files) {
            filesNameList.add(FolderName(f.name))
        }
        viewModel.updateValue(filesNameList)

    }


    private fun makeNewFolder(folderName: String): Boolean {

        var isSuccess: Boolean

        val dir = File(
            getExternalFilesDir(
                Environment.DIRECTORY_DCIM
            ).toString() + "/$folderName"
        )

        Log.d(TAG, "makeNewFolder: ${dir.absolutePath}")


        if (!dir!!.exists()) {
            dir.mkdirs()
            isSuccess = true
        } else {
            toast("Existed Folder Name")
            isSuccess = false
        }

        return isSuccess
    }


    private fun showMakeFolderDialog() {
        val binding: AddFolderDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.add_folder_dialog,
            null,
            false
        )

        val dialog = Dialog(this)

        binding.dialogAgreeBtn.setOnClickListener {

            if (makeNewFolder(binding.dialogEt.text.toString())) {
                getFolderName()
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

    private fun recyclerview() {
        val recyclerView = binding.recyclerViewVertical
        adapter = ListVerticalAdapter()

        adapter.itemClick = object : ListVerticalAdapter.ItemClick {
            override fun onClick(view: View, position: Int, folderName: FolderName) {
                Log.d(TAG, "onClick: $position clicked")

                val intent = Intent(mContext, GalleryActivity::class.java)
                intent.putExtra("folderName", folderName.folderName)
                startActivity(intent)
            }

//            override fun addBtnOnClick(view: View, position: Int, folderName: String) {
//                val intent = Intent(mContext, LaunchActivity::class.java)
//                intent.putExtra("folderName", folderName)
//                // intent.putExtra("backgroundPhoto", backgroundPhoto)
//                startActivity(intent)
//            }
        }

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = manager
//        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter
//        adapter.submitList(folderNameList)

//        val multiSnapHelper = MultiSnapHelper(SnapGravity.START, 1, 100f)
//        multiSnapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.newFolderFab.hide()
                } else {
                    binding.newFolderFab.show()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

}