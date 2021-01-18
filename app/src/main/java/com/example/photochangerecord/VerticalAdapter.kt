package com.example.photochangerecord

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo
import com.takusemba.multisnaprecyclerview.MultiSnapHelper
import com.takusemba.multisnaprecyclerview.SnapGravity


class VerticalAdapter(
    private val context: Context,
    private val folderNameList: ArrayList<String>
) : RecyclerView.Adapter<VerticalAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "VerticalAdapter"
    }

    interface ItemClick
    {
        fun onClick(view: View, position: Int, folderName: String)
//        fun addBtnOnClick(view: View, position: Int, folderName: String)
    }
    var itemClick: ItemClick? = null



    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_vertical, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(itemClick != null)
        {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position, folderNameList[position])
            }
        }

//        if(itemClick != null)
//        {
//            holder?.listAddPhotoBtn?.setOnClickListener { v ->
//                itemClick?.addBtnOnClick(v, position, folderNameList[position])
//            }
//        }

        holder.folderTitle.text = folderNameList[position]


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

    override fun getItemCount(): Int {
        return folderNameList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
//        val recyclerViewHorizontal= itemView.findViewById(R.id.recycler_view_horizontal) as RecyclerView
        val folderTitle: TextView = itemView.findViewById(R.id.folder_title) as TextView
        val folderLayout : RelativeLayout = itemView.findViewById(R.id.folder_layout) as RelativeLayout
//        val listAddPhotoBtn: ImageView = itemView.findViewById(R.id.list_add_photo_btn) as ImageView


    }
}