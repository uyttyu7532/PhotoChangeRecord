package com.uyttyu7532.photolapse.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uyttyu7532.photolapse.R
import com.uyttyu7532.photolapse.databinding.ItemListVerticalBinding
import com.uyttyu7532.photolapse.model.FolderName


class ListVerticalAdapter :
    ListAdapter<FolderName, ListVerticalAdapter.ListViewHolder>(MyListDiffCallback) {

    companion object {
        private const val TAG = "VerticalAdapter"
    }

    interface ItemClick {
        fun onClick(view: View, position: Int, folderName: FolderName)
//        fun addBtnOnClick(view: View, position: Int, folderName: String)
    }

    var itemClick: ItemClick? = null

    inner class ListViewHolder(private val binding: ItemListVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(folderName: FolderName) {
            binding.folderTitle.text = folderName.folderName
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemListVerticalBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_list_vertical, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(currentList[holder.adapterPosition])

        if (itemClick != null) {
            holder?.itemView?.setOnClickListener { v ->
                Log.d(TAG, "onBindViewHolder: ${holder.adapterPosition}")
                itemClick?.onClick(v, holder.adapterPosition, currentList[holder.adapterPosition])
            }

        }
    }


    object MyListDiffCallback : DiffUtil.ItemCallback<FolderName>() {
        const val TAG = "MyListDiffCallback"
        override fun areItemsTheSame(
            oldItem: FolderName,
            newItem: FolderName
        ): Boolean {
            Log.d(
                TAG,
                "areItemsTheSame: $oldItem $newItem ${oldItem.folderName == newItem.folderName}"
            )
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
}