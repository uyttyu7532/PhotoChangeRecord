package com.example.photochangerecord

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo

class HorizontalAdapter(
    private val context: Context,
    private val folder: Folder
) : androidx.recyclerview.widget.RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    interface ItemClick
    {
        fun onClick(view: View, position: Int, folderName: String, photo: Photo) // TODO photo지우기
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_horizontal, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(itemClick != null)
        {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position, folder.title, folder.photos[position])
            }
        }

        val photo = folder.photos[position]
        holder.title.text = photo.date

        // TODO Glide로 해야되나  (데이터 바인딩..)
        Glide.with(context).load(photo.resourceID).into(holder.image)

    }

    override fun getItemCount(): Int {
        return folder.photos.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(
        itemView) {
        val title: TextView = itemView.findViewById(R.id.title) as TextView
        val image: ImageView = itemView.findViewById(R.id.image) as ImageView
    }
}