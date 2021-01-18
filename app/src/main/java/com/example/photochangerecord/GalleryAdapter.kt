package com.example.photochangerecord

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo

class GalleryAdapter(
    private val context: Context,
    private val folder: Folder
) : androidx.recyclerview.widget.RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {


    interface ItemClick
    {
        // TODO photo는 나중에 안 보내도 될듯 (photo -> folderName과 position)
        fun onClick(view: View, position: Int, folder: Folder)
    }
    var itemClick: ItemClick? = null


    // 굳이 지금 필요한 건 아니고.. recyclerview 깜박임 해결
//    override fun getItemId(position: Int): Long {
//        return folder.photos[position].absolute_file_path.toLong()
////        return super.getItemId(position)
//    }



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
                itemClick?.onClick(v, position, folder)
            }
        }

        val photo = folder.photos[position]


//        holder.title.text = photo.absolute_file_path

        // TODO (데이터 바인딩..)
//        holder.image.setImageResource(photo.resourceID)

        Glide.with(context).load(photo.absolute_file_path).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(
            holder.image
        )

    }

    override fun getItemCount(): Int {
        return folder.photos.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(
        itemView
    ) {
//        val title: TextView = itemView.findViewById(R.id.gallery_image_title) as TextView
        val image: ImageView = itemView.findViewById(R.id.gallery_image_view) as ImageView

    }
}