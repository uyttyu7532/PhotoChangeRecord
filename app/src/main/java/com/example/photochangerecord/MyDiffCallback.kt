package com.example.photochangerecord

import androidx.recyclerview.widget.DiffUtil
import com.example.photochangerecord.viewmodel.Folder

object MyDiffCallback : DiffUtil.ItemCallback<Folder>() {
    override fun areItemsTheSame(
        oldItem: Folder,
        newItem: Folder
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Folder,
        newItem: Folder
    ): Boolean {
        return oldItem == newItem
    }

}