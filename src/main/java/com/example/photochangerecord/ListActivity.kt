package com.example.photochangerecord


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photochangerecord.databinding.ActivityListBinding
import com.example.photochangerecord.databinding.AddFolderDialogBinding
import com.example.photochangerecord.viewmodel.CameraBackGroundViewModel
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo
import com.takusemba.multisnaprecyclerview.MultiSnapHelper
import com.takusemba.multisnaprecyclerview.SnapGravity
import kotlinx.android.synthetic.main.add_folder_dialog.*
import splitties.toast.toast


class ListActivity : AppCompatActivity() {
    private var mContext: Context? = null
    private val folderList: ArrayList<Folder> = ArrayList()

    companion object {
        private const val TAG = "ListActivity"
    }

    private lateinit var binding: ActivityListBinding
    private lateinit var viewModel: CameraBackGroundViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        // 뷰모델 인스턴스를 가져온다.
        viewModel = ViewModelProvider(this).get(CameraBackGroundViewModel::class.java)
        // 원래 this로 액티비티를 연결했지만 뷰모델을 여기서 연결한다!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.hide()

        recyclerview()

        binding.newFolderFab.setOnClickListener {
            // 폴더 생성 다이얼로그
            showMakeFolderDialog()

        }

    }

    private fun showMakeFolderDialog(){
        val binding: AddFolderDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.add_folder_dialog,
            null,
            false
        )

//        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = inflater.inflate(R.layout.add_folder_dialog, null)

//        val alertDialog = AlertDialog.Builder(this).create()

        val dialog = Dialog(this)
        dialog.setContentView(binding.root)

//        alertDialog.setView(view)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun recyclerview() {
        initializeData()


        val adapter = VerticalAdapter(mContext!!, folderList)
        adapter.itemClick = object : VerticalAdapter.ItemClick {
            override fun onClick(view: View, position: Int, folder: Folder) {
                Log.d(TAG, "onClick: $position clicked")

                // TODO 생각해보니까 여기서 사진 데이터를 모두 넘기기보다는 폴더명을 넘겨야할 듯?
                // GalleryActivity에서 업데이트 될 수도 있으니까
                val intent = Intent(mContext, GalleryActivity::class.java)
                intent.putExtra("folderInfo", folder)
                startActivity(intent)
            }

            override fun addBtnOnClick(view: View, position: Int, folder: Folder) {
                toast(folder.toString())
                // 카메라로 이동 해당 폴더에 저장
            }
        }

        val recyclerView = binding.recyclerViewVertical
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter

        val multiSnapHelper = MultiSnapHelper(SnapGravity.START, 1, 100f)
        multiSnapHelper.attachToRecyclerView(recyclerView)

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


    private fun initializeData() {
        val photo1: ArrayList<Photo> = ArrayList()
        photo1.add(Photo(R.drawable.photo, "2021-01-11 17:00:53"))
        photo1.add(Photo(R.drawable.photo, "2021-01-12 17:00:53"))
        photo1.add(Photo(R.drawable.photo, "2021-01-13 17:00:53"))
        photo1.add(Photo(R.drawable.photo, "2021-01-14 17:00:53"))
        folderList.add(Folder("Skin Log", photo1))
        val photo2: ArrayList<Photo> = ArrayList()
        photo2.add(Photo(R.drawable.star, "2021-01-15 17:00:53"))
        photo2.add(Photo(R.drawable.star, "2021-01-16 17:00:53"))
        photo2.add(Photo(R.drawable.star, "2021-01-17 17:00:53"))
        photo2.add(Photo(R.drawable.star, "2021-01-18 17:00:53"))
        folderList.add(Folder("Diet", photo2))
        val photo3: ArrayList<Photo> = ArrayList()
        photo3.add(Photo(R.drawable.hourglass, "2021-01-19 17:00:53"))
        photo3.add(Photo(R.drawable.hourglass, "2021-01-20 17:00:53"))
        photo3.add(Photo(R.drawable.photo, "2021-01-21 17:00:53"))
        photo3.add(Photo(R.drawable.hourglass, "2021-01-22 17:00:53"))
        photo3.add(Photo(R.drawable.photo, "2021-01-23 17:00:53"))
        photo3.add(Photo(R.drawable.hourglass, "2021-01-24 17:00:53"))
        photo3.add(Photo(R.drawable.photo, "2021-01-25 17:00:53"))
        photo3.add(Photo(R.drawable.hourglass, "2021-01-26 17:00:53"))
        folderList.add(Folder("Diary", photo3))
        val photo4: ArrayList<Photo> = ArrayList()
        photo4.add(Photo(R.drawable.bell, "2021-01-11 17:00:53"))
        photo4.add(Photo(R.drawable.star, "2021-02-11 17:00:53"))
        photo4.add(Photo(R.drawable.hourglass, "2021-03-11 17:00:53"))
        photo4.add(Photo(R.drawable.photo, "2021-04-11 17:00:53"))
        folderList.add(Folder("Skin Log", photo1))
        val photo5: ArrayList<Photo> = ArrayList()
        photo5.add(Photo(R.drawable.bell, "2021-05-11 17:00:53"))
        photo5.add(Photo(R.drawable.bell, "2021-06-11 17:00:53"))
        photo5.add(Photo(R.drawable.bell, "2021-07-11 17:00:53"))
        photo5.add(Photo(R.drawable.bell, "2021-08-11 17:00:53"))
        folderList.add(Folder("Diet", photo2))
        val photo6: ArrayList<Photo> = ArrayList()
        photo6.add(Photo(R.drawable.photo, "2021-09-11 17:00:53"))
        photo6.add(Photo(R.drawable.photo, "2021-10-11 17:00:53"))
        photo6.add(Photo(R.drawable.photo, "2021-11-11 17:00:53"))
        photo6.add(Photo(R.drawable.photo, "2021-12-11 17:00:53"))
        folderList.add(Folder("Diary", photo3))
    }

}