package com.example.photochangerecord

import androidx.recyclerview.widget.DiffUtil

object MyListDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return false
    }

}