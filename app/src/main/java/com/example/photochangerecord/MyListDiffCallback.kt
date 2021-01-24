package com.example.photochangerecord

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.photochangerecord.viewmodel.FolderName

object MyListDiffCallback : DiffUtil.ItemCallback<FolderName>() {
    const val TAG = "MyListDiffCallback"
    override fun areItemsTheSame(
        oldItem: FolderName,
        newItem: FolderName
    ): Boolean {
        Log.d(TAG, "areItemsTheSame: $oldItem $newItem ${oldItem.folderName == newItem.folderName}")
        return oldItem.folderName == newItem.folderName
    }

    override fun areContentsTheSame(
        oldItem: FolderName,
        newItem: FolderName
    ): Boolean {
        Log.d(TAG, "areContentsTheSame:$oldItem $newItem ${oldItem == newItem}")
        return oldItem == newItem
    }

}