package com.example.photochangerecord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.photochangerecord.databinding.ActivityListBinding
import com.example.photochangerecord.viewmodel.CameraBackGroundViewModel


class ListActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "ListActivity"
    }

    private lateinit var binding:ActivityListBinding
    private lateinit var viewModel: CameraBackGroundViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)

        // 뷰모델 인스턴스를 가져온다.
        viewModel = ViewModelProvider(this).get(CameraBackGroundViewModel::class.java)
        // 원래 this로 액티비티를 연결했지만 뷰모델을 여기서 연결한다!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        binding =  ActivityListBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)


        viewModel.imageAlpha.observe(this, {
            Log.d(TAG, it.toString())
        })

//        plus.setOnClickListener {
//            viewModel.updateValue(ActionType.PLUS)
//        }
//
//        minus.setOnClickListener {
//            viewModel.updateValue(ActionType.MINUS)
//        }
    }

}