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
import com.example.photochangerecord.model.Photo
import java.io.File
import java.util.*


class GalleryAdapter(private val folderName: String) :
    ListAdapter<Photo, GalleryAdapter.GalleryViewHolder>(
        MyDiffCallback
    ) {

    var itemClick: ItemClick? = null

    interface ItemClick {
        fun onClick(view: View, position: Int, folderName: String, photos: ArrayList<Photo>)
    }


    inner class GalleryViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.galleryImageTitle.text =
                photo.absolute_file_path.substringAfterLast("/").substringBeforeLast(".jpg")
            binding.galleryImageView.load(File(photo.absolute_file_path)) {
                crossfade(true)
                crossfade(200)
                placeholder(R.drawable.loading)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemGalleryBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_gallery, parent, false)
        return GalleryViewHolder(binding)
    }

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
    }


    override fun getItemCount(): Int {
        return currentList.size
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

}




