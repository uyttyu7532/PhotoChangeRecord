package com.example.photochangerecord

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.photochangerecord.viewmodel.Photo

object MyDiffCallback : DiffUtil.ItemCallback<Photo>() {
    const val TAG = "MyDiffCallback"
    override fun areItemsTheSame(
        oldItem: Photo,
        newItem: Photo
    ): Boolean {
        Log.d(TAG, "areItemsTheSame: $oldItem $newItem ${oldItem.absolute_file_path == newItem.absolute_file_path}")
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