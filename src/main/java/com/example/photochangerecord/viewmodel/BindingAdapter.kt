package com.example.photochangerecord.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.photochangerecord.CameraActivity
import com.ramotion.fluidslider.FluidSlider
import kotlinx.android.synthetic.main.activity_camera.*

object BindingAdapter {

    // TODO 슬라이더 값이 바뀌면 이미지 뷰의 투명도가 바뀌는 것이기 때문에 양방향 데이터 바인딩이 아니었다.
//    // 단반향 데이터 바인딩에서도 사용
//    // getter (view에 데이터를 set)
//    @BindingAdapter("alpha")
//    fun setAlpha(view: FluidSlider) {
//        val position = view.position
//    }
//
//
//    @BindingAdapter("alphaAttrChanged") // ~AttrChanged
//    fun setAlphaInverseBindingListener(
//        view: FluidSlider,
//        listener: InverseBindingListener
//    ) {
//        // InverseBindingListener의 onChange()가 어디서 호출되는 지 정의
//        view.positionListener?.let {
//            listener.onChange()
//
//        }
//    }
//
//
//    // getter (view에서 데이터를 get)
//    @InverseBindingAdapter(attribute = "alpha", event = "alphaAttrChanged")
//    fun getAlpha(view: FluidSlider): Float {
//        return view.position
//    }

}
