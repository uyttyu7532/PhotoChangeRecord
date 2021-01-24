package com.example.photochangerecord

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photochangerecord.databinding.ItemListVerticalBinding
import com.example.photochangerecord.viewmodel.FolderName


class ListVerticalAdapter :
    ListAdapter<FolderName, ListVerticalAdapter.ListViewHolder>(MyListDiffCallback) {

    companion object {
        private const val TAG = "VerticalAdapter"
    }

    interface ItemClick {
        fun onClick(view: View, position: Int, folderName: FolderName)
//        fun addBtnOnClick(view: View, position: Int, folderName: String)
    }

    var itemClick: ItemClick? = null


    inner class ListViewHolder(private val binding: ItemListVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // 그냥 View하고 데이터 연결하는 거 생각하면 됩니다
        fun bind(folderName: FolderName) {
            binding.folderTitle.text = folderName.folderName
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemListVerticalBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_list_vertical, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(currentList[position])

        if (itemClick != null) {
            holder?.itemView?.setOnClickListener { v ->
                Log.d(TAG, "onBindViewHolder: $position")
                itemClick?.onClick(v, position, currentList[position])
            }
        }

//        if(itemClick != null)
//        {
//            holder?.listAddPhotoBtn?.setOnClickListener { v ->
//                itemClick?.addBtnOnClick(v, position, folderNameList[position])
//            }
//        }

        // ListActivity(메인)에 일단 사진을 제외함. 리사이클러뷰 in 리사이클러뷰
//        val adapter = HorizontalAdapter(context, folderList[position])
//
//        adapter.itemClick = object : HorizontalAdapter.ItemClick {
//            override fun onClick(view: View, position: Int, folder:Folder) {
//                Log.d(TAG, "onClick: $position clicked")
//
//                // GalleryActivity에서 업데이트 될 수도 있으니까
//                val intent = Intent(context, DetailActivity::class.java)
//                intent.putExtra("folder", folder)
//                intent.putExtra("position", position)
//                context.startActivity(intent)
//            }
//        }
//
//        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        holder.recyclerViewHorizontal.layoutManager = manager
//        holder.recyclerViewHorizontal.adapter = adapter
//
//        val multiSnapHelper = MultiSnapHelper(SnapGravity.START, 1, 100f)
//        multiSnapHelper.attachToRecyclerView(holder.recyclerViewHorizontal)
    }
}