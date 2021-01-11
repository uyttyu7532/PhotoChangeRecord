package com.example.photochangerecord


import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photochangerecord.databinding.ActivityListBinding
import com.example.photochangerecord.viewmodel.CameraBackGroundViewModel
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo
import com.takusemba.multisnaprecyclerview.MultiSnapHelper
import com.takusemba.multisnaprecyclerview.SnapGravity
import kotlinx.android.synthetic.main.activity_list.*


class ListActivity : AppCompatActivity() {
    private var mContext: Context ?= null
    private val folderList: ArrayList<Folder> = ArrayList()

    companion object{
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

        recyclerview()


    }

    private fun recyclerview(){
        initializeData()

        val adapter = VerticalAdapter(mContext!!, folderList)
        val recyclerView = binding.recyclerViewVertical
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter

        val multiSnapHelper = MultiSnapHelper(SnapGravity.START, 1, 100f)
        multiSnapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0){
                    binding.newFolderFab.hide()
                } else{
                    binding.newFolderFab.show()
                }
                super.onScrolled(recyclerView, dx, dy)

            }
        })
    }


    private fun initializeData() {
        val photo1: ArrayList<Photo> = ArrayList()
        photo1.add(Photo(R.drawable.photo, "어벤져스"))
        photo1.add(Photo(R.drawable.photo, "미션임파서블"))
        photo1.add(Photo(R.drawable.photo, "아저씨"))
        folderList.add(Folder("Skin Log", photo1))
        val photo2: ArrayList<Photo> = ArrayList()
        photo2.add(Photo(R.drawable.photo, "범죄도시"))
        photo2.add(Photo(R.drawable.photo, "공공의적"))
        photo2.add(Photo(R.drawable.photo, "맨인블랙"))
        folderList.add(Folder("Diet", photo2))
        val photo3: ArrayList<Photo> = ArrayList()
        photo3.add(Photo(R.drawable.photo, "고질라"))
        photo3.add(Photo(R.drawable.photo, "캡틴마블"))
        photo3.add(Photo(R.drawable.photo, "아이언맨"))
        photo3.add(Photo(R.drawable.photo, "고질라"))
        photo3.add(Photo(R.drawable.photo, "캡틴마블"))
        photo3.add(Photo(R.drawable.photo, "아이언맨"))
        photo3.add(Photo(R.drawable.photo, "고질라"))
        photo3.add(Photo(R.drawable.photo, "캡틴마블"))
        photo3.add(Photo(R.drawable.photo, "아이언맨"))
        folderList.add(Folder("Diary", photo3))
        val photo4: ArrayList<Photo> = ArrayList()
        photo4.add(Photo(R.drawable.photo, "어벤져스"))
        photo4.add(Photo(R.drawable.photo, "미션임파서블"))
        photo4.add(Photo(R.drawable.photo, "아저씨"))
        folderList.add(Folder("Skin Log", photo1))
        val photo5: ArrayList<Photo> = ArrayList()
        photo5.add(Photo(R.drawable.photo, "범죄도시"))
        photo5.add(Photo(R.drawable.photo, "공공의적"))
        photo5.add(Photo(R.drawable.photo, "맨인블랙"))
        folderList.add(Folder("Diet", photo2))
        val photo6: ArrayList<Photo> = ArrayList()
        photo6.add(Photo(R.drawable.photo, "고질라"))
        photo6.add(Photo(R.drawable.photo, "캡틴마블"))
        photo6.add(Photo(R.drawable.photo, "아이언맨"))
        folderList.add(Folder("Diary", photo3))
    }

}