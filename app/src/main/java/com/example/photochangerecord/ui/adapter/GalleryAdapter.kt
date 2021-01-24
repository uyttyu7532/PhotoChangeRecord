package com.example.photochangerecord.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photochangerecord.R
import com.example.photochangerecord.databinding.ItemGalleryBinding
import com.example.photochangerecord.model.Folder
import com.example.photochangerecord.model.Photo
import java.io.File
import java.util.*


class GalleryAdapter(val folderName: String) : ListAdapter<Photo, GalleryAdapter.GalleryViewHolder>(
    MyDiffCallback
) {

    var folder: Folder? = null

    //    var tracker: SelectionTracker<Long>? = null
    var itemClick: ItemClick? = null

    interface ItemClick {
        fun onClick(view: View, position: Int, folderName: String, photos: ArrayList<Photo>)
    }


//    init {
//        // true: long 타입의 유일한 id는 각각 하나의 아이템에만 매칭된다는 뜻
//        setHasStableIds(true)
//    }
//
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong() // 적절한 id값 리턴한다.
//    }

    override fun getItemCount(): Int {
        return currentList.size
    }


//    inner class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(
//        itemView
//    ) {
//        private val title: TextView = itemView.findViewById(R.id.gallery_image_title) as TextView
//        private val image: ImageView = itemView.findViewById(R.id.gallery_image_view) as ImageView
//
//        fun bind(photo: Photo, isActivated: Boolean = false) {
//            title.text =
//                photo.absolute_file_path.substringAfterLast("/").substringBeforeLast(".jpg")
//            image.load(File(photo.absolute_file_path)) {
//                crossfade(true)
//                crossfade(200)
//                placeholder(R.drawable.loading)
//            }
//            itemView.isActivated = isActivated
//        }


//        override fun onCreateViewHolder(
//            viewGroup: ViewGroup,
//            viewType: Int
//        ): GalleryViewHolder {
//            val inflater = LayoutInflater.from(viewGroup.context)
//            val view = inflater.inflate(R.layout.item_gallery, viewGroup, false)
//            return GalleryViewHolder(view)
//        }


//        override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
//            if (itemClick != null) {
//                holder?.itemView?.setOnClickListener { v ->
//                    itemClick?.onClick(v, position, folder!!)
//                }
//            }
//
//            val photo = folder!!.photos[position]
//
//            tracker?.let {
//                holder.bind(photo, it.isSelected(position.toLong()))
//            }
//        }


    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {

        if (itemClick != null) {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(
                    v,
                    position,
                    folderName,
                    currentList.toList() as ArrayList<Photo>
                )
            }
        }

        holder.bind(currentList[position])

//        tracker?.let {
//            holder.bind(photo, it.isSelected(position.toLong()))
//        }
    }

    inner class GalleryViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // 그냥 View하고 데이터 연결하는 거 생각하면 됩니다
        fun bind(photo: Photo) {
            binding.galleryImageTitle.text =
                photo.absolute_file_path.substringAfterLast("/").substringBeforeLast(".jpg")
            binding.galleryImageView.load(File(photo.absolute_file_path)) {
                crossfade(true)
                crossfade(200)
                placeholder(R.drawable.loading)
            }
//            itemView.isActivated = isActivated
        }

//        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
//            object : ItemDetailsLookup.ItemDetails<Long>() {
//                override fun getPosition(): Int = adapterPosition
//                override fun getSelectionKey(): Long? = itemId
//            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemGalleryBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_gallery, parent, false)
        return GalleryViewHolder(binding)
    }

}

object MyDiffCallback : DiffUtil.ItemCallback<Photo>() {
    const val TAG = "MyDiffCallback"
    override fun areItemsTheSame(
        oldItem: Photo,
        newItem: Photo
    ): Boolean {
        Log.d(
            TAG,
            "areItemsTheSame: $oldItem $newItem ${oldItem.absolute_file_path == newItem.absolute_file_path}"
        )
        return oldItem.absolute_file_path == newItem.absolute_file_path
    }

    override fun areContentsTheSame(
        oldItem: Photo,
        newItem: Photo
    ): Boolean {
        Log.d(TAG, "areContentsTheSame:$oldItem $newItem ${oldItem == newItem}")
        return oldItem == newItem
    }
}





