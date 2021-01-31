package com.uyttyu7532.photolapse.ui.list

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.uyttyu7532.photolapse.R
import com.uyttyu7532.photolapse.ui.gallery.GalleryActivity
import com.uyttyu7532.photolapse.databinding.ActivityListBinding
import com.uyttyu7532.photolapse.databinding.AddFolderDialogBinding
import com.uyttyu7532.photolapse.model.FolderName
import com.uyttyu7532.photolapse.ui.adapter.ListVerticalAdapter
import com.uyttyu7532.photolapse.utils.MyApplication.Companion.prefsFirst
import splitties.toast.toast
import java.io.File
import java.io.FileOutputStream


class ListActivity : AppCompatActivity() {
    private var mContext: Context? = null

    companion object {
        private const val TAG = "ListActivity"
    }

    private lateinit var binding: ActivityListBinding
    private lateinit var viewModel: FolderNameListViewModel
    private lateinit var adapter: ListVerticalAdapter

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

        setSampleFolder()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                toast("Delete Success")
            }
        }

    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        getFolderName()
        super.onResume()
    }

    private fun setSampleFolder() {
        if (prefsFirst.getBoolean("appFirst", true)) {
            makeNewFolder("Sample")
            makeNewFolder("Your First Folder")

            var bitmapList: ArrayList<Bitmap> = ArrayList()
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample1))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample2))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample3))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample4))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample5))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample6))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample7))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample8))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample9))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample10))
            bitmapList.add(BitmapFactory.decodeResource(this.resources, R.drawable.sample11))

            var fileNameList: ArrayList<String> = ArrayList()
            fileNameList.add("20210128_235210.jpg")
            fileNameList.add("20210128_235240.jpg")
            fileNameList.add("20210128_235310.jpg")
            fileNameList.add("20210128_235390.jpg")
            fileNameList.add("20210128_235417.jpg")
            fileNameList.add("20210128_235430.jpg")
            fileNameList.add("20210128_235522.jpg")
            fileNameList.add("20210128_235622.jpg")
            fileNameList.add("20210128_235700.jpg")
            fileNameList.add("20210128_235741.jpg")
            fileNameList.add("20210128_235952.jpg")

            persistSampleImage(bitmapList, fileNameList)

            prefsFirst.setBoolean("appFirst", false)
        }
    }

    private fun persistSampleImage(bitmapList: ArrayList<Bitmap>, fileNameList: ArrayList<String>) {
        try {

            val dir = File(
                getExternalFilesDir(
                    Environment.DIRECTORY_DCIM
                ).toString() + "/Sample"
            )

            if (!dir!!.exists()) {
                dir.mkdirs()
            }


            for (i in bitmapList.indices) {
                Log.d(TAG, "${bitmapList[i]},${fileNameList[i]}")

                val newFile = File(dir, "${fileNameList[i]}")

                val out = FileOutputStream(newFile)
                bitmapList[i].compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
            }

        } catch (e: Exception) {
            Log.d(TAG, "persistSampleImage: $e")
        }
    }

    private fun getFolderName() {

        var directory = File(
            getExternalFilesDir(
                Environment.DIRECTORY_DCIM
            ).toString()
        )
        var files = directory.listFiles().sortedArray()
        val filesNameList = ArrayList<FolderName>()
        Log.d(TAG, "getFolderName: after $filesNameList")
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