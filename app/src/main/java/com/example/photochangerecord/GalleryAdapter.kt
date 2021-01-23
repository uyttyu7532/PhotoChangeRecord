package com.example.photochangerecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photochangerecord.viewmodel.Folder
import com.example.photochangerecord.viewmodel.Photo
import java.io.File

class GalleryAdapter(
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    var folder: Folder ?= null
    var tracker: SelectionTracker<Long>? = null
    var itemClick: ItemClick? = null

    interface ItemClick {
        fun onClick(view: View, position: Int, folder: Folder)
    }




    init {
        // true: long 타입의 유일한 id는 각각 하나의 아이템에만 매칭된다는 뜻
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // 적절한 id값 리턴한다.
    }




    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        private val title: TextView = itemView.findViewById(R.id.gallery_image_title) as TextView
        private val image: ImageView = itemView.findViewById(R.id.gallery_image_view) as ImageView

        fun bind(photo: Photo, isActivated: Boolean = false) {
            title.text =
                photo.absolute_file_path.substringAfterLast("/").substringBeforeLast(".jpg")
            image.load(File(photo.absolute_file_path)) {
                crossfade(true)
                crossfade(200)
                placeholder(R.drawable.loading)
            }
            itemView.isActivated = isActivated
        }


        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
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
                itemClick?.onClick(v, position, folder!!)
            }
        }

        val photo = folder!!.photos[position]

        tracker?.let {
            holder.bind(photo, it.isSelected(position.toLong()))
        }


    }

    override fun getItemCount(): Int {
        return folder!!.photos.size
    }

}


