package com.example.photochangerecord

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo
import com.takusemba.multisnaprecyclerview.MultiSnapHelper
import com.takusemba.multisnaprecyclerview.SnapGravity


class VerticalAdapter(
    private val context: Context,
    private val folderList: ArrayList<Folder>
) : RecyclerView.Adapter<VerticalAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "VerticalAdapter"
    }

    interface ItemClick
    {
        fun onClick(view: View, position: Int, folder: Folder)
        fun addBtnOnClick(view: View, position: Int, folder: Folder)
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
                itemClick?.onClick(v, position, folderList[position])
            }
        }

        if(itemClick != null)
        {
            holder?.listAddPhotoBtn?.setOnClickListener { v ->
                itemClick?.addBtnOnClick(v, position, folderList[position])
            }
        }

        holder.folderTitle.text = folderList[position].title

        val adapter = HorizontalAdapter(context, folderList[position])

        adapter.itemClick = object : HorizontalAdapter.ItemClick {
            override fun onClick(view: View, position: Int, folderName:String, photo: Photo) {
                Log.d(TAG, "onClick: $position clicked")

                // TODO 생각해보니까 여기서 사진 데이터를 모두 넘기기보다는 폴더명을 넘겨야할 듯?
                // GalleryActivity에서 업데이트 될 수도 있으니까
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("photoInfo", photo) // TODO 나중에 지우기
                intent.putExtra("folderName", folderName)
                intent.putExtra("photoPosition", position)
                context.startActivity(intent)
            }
        }

        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerViewHorizontal.layoutManager = manager
        holder.recyclerViewHorizontal.adapter = adapter

        val multiSnapHelper = MultiSnapHelper(SnapGravity.START, 1, 100f)
        multiSnapHelper.attachToRecyclerView(holder.recyclerViewHorizontal)

    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        val recyclerViewHorizontal= itemView.findViewById(R.id.recycler_view_horizontal) as RecyclerView
        val folderTitle: TextView = itemView.findViewById(R.id.folder_title) as TextView
        val listAddPhotoBtn: ImageView = itemView.findViewById(R.id.list_add_photo_btn) as ImageView


    }
}