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

class GalleryAdapter(
    private val context: Context,
    private val folder: Folder
) : androidx.recyclerview.widget.RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {


    interface ItemClick
    {
        // TODO photo는 나중에 안 보내도 될듯 (photo -> folderName과 position)
        fun onClick(view: View, position: Int, folderName: String, photo: Photo)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_gallery, viewGroup, false)
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

        // TODO (데이터 바인딩..)
//        holder.image.setImageResource(photo.resourceID)
        Glide.with(context).load(photo.resourceID).into(holder.image)

    }

    override fun getItemCount(): Int {
        return folder.photos.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(
        itemView) {
        val title: TextView = itemView.findViewById(R.id.gallery_image_title) as TextView
        val image: ImageView = itemView.findViewById(R.id.gallery_image_view) as ImageView

    }
}