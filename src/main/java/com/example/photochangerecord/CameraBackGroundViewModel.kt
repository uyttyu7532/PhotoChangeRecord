package com.example.photochangerecord

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class ActionType {
    PLUS,
    MINUS
}

class CameraBackGroundViewModel : ViewModel() {

    // 뮤터블 라이브 데이터 - 수정 가능
    // 라이브 데이터 - 수정 불가능

    // 내부에서 설정하는 자료형은 뮤터블로
    private val _currentValue = MutableLiveData<Int>()

    // 변경되지 않는 값을 가져올 때 언더스코어 없는 이름 사용
    // private (x)
    // 하지만 라이브 데이터에 직접 접근하지 않고 뷰모델을 통해 가져옴
    val currentValue: LiveData<Int>
        get() = _currentValue

    // 초기값 설정
    init {
        Log.d(TAG, "CameraBackGroundViewModel 생성자 호출 ")
        _currentValue.value = 0
    }

    fun updateValue(actionType: ActionType) {
        when (actionType) {
            ActionType.PLUS -> _currentValue.value = _currentValue.value?.plus(1)
            ActionType.MINUS -> _currentValue.value = _currentValue.value?.minus(1)
        }
    }

    companion object {
        private const val TAG = "로그"
    }
}