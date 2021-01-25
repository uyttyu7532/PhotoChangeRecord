package com.example.photochangerecord.ui.camera

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photochangerecord.utils.MyApplication

//enum class ActionType {
//    PLUS,
//    MINUS
//}

class CameraBackGroundViewModel : ViewModel() {

    // 뮤터블 라이브 데이터 - 수정 가능
    // 라이브 데이터 - 수정 불가능

    // 내부에서 설정하는 자료형은 뮤터블로
    private val _imageAlpha = MutableLiveData<Float>()

    // 변경되지 않는 값을 가져올 때 언더스코어 없는 이름 사용
    // private (x)
    // 하지만 라이브 데이터에 직접 접근하지 않고 뷰모델을 통해 가져옴
    val imageAlpha: LiveData<Float>
        get() = _imageAlpha

    // 초기값 설정
    init {
        Log.d(TAG, "CameraBackGroundViewModel 생성자 호출 ")
        _imageAlpha.value = MyApplication.prefsAlpha.getFloat("backGroundAlpha", 0.5f)
    }


    fun updateValue(position: Float) {
        _imageAlpha.postValue(position)
//        Log.d(TAG, "updateValue: $position")
        MyApplication.prefsAlpha.setFloat("backGroundAlpha", position)
    }

    companion object {
        private const val TAG = "CameraBackGroundViewModel"
    }


}