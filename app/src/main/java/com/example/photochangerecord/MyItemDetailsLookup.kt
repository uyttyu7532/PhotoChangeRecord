package com.example.photochangerecord

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView


// 사용자가 선택한 항목들에 대한 정보를 라이브러리에 제공
// 보여주고 있는 아이템 정보에 대해 ItemDetails<Key>를 ViewHolder가 반환해야 한다.
// motionevent의 좌표값으로 부터 해당 view 얻어온다.
class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as GalleryAdapter.GalleryViewHolder).getItemDetails()
        }
        return null
    }
}


