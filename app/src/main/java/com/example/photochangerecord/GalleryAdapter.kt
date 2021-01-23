package com.example.photochangerecord

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo
import java.io.File

class GalleryAdapter(
    private val context: Context,
    private val folder: Folder
) : androidx.recyclerview.widget.RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    var itemClick: ItemClick? = null

    interface ItemClick {
        fun onClick(view: View, position: Int, folder: Folder)
    }

   inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(
        itemView
    ) {
        private val title: TextView = itemView.findViewById(R.id.gallery_image_title) as TextView
        private val image: ImageView = itemView.findViewById(R.id.gallery_image_view) as ImageView

       fun bind(photo: Photo){
           title.text = photo.absolute_file_path.substringAfterLast("/").substringBeforeLast(".jpg")
           image.load(File(photo.absolute_file_path)) {
               crossfade(true)
               crossfade(200)
               placeholder(R.drawable.loading)
           }
       }

    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_gallery, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemClick != null) {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position, folder)
            }
        }

        val photo = folder.photos[position]

        holder.bind(photo)

    }

    override fun getItemCount(): Int {
        return folder.photos.size
    }
}


// 굳이 지금 필요한 건 아니고.. recyclerview 깜박임 해결
//    override fun getItemId(position: Int): Long {
//        return folder.photos[position].absolute_file_path.toLong()
////        return super.getItemId(position)
//    }