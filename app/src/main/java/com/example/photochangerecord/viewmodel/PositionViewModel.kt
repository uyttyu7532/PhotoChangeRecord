package com.example.photochangerecord.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PositionViewModel: ViewModel(){

    //MutableLiveData란 변경할 수 있는 LiveData 형입니다.
    //일반적인 LiveData형은 변경할 수 없고 오로지 데이터의 변경값만을 소비하는데 반해
    //MutableLiveData는 데이터를 UI Thread와 Background Thread에서 선택적으로 바꿀 수 있습니다.
    private val _currentDetailPosition = MutableLiveData<Int>()

    //  _post로 선언된 MutableLiveData를 post를 통해 발행합니다.
    // 이렇듯 ViewModel에서만 _post를 변경할 수 있기때문에 보안에 더 좋습니다.
    val currentDetailPosition : LiveData<Int>
        get() = _currentDetailPosition

    fun updatePosition(position: Int) {
        _currentDetailPosition.postValue(position)
    }

}