package com.uyttyu7532.photolapse.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.uyttyu7532.photolapse.R
import com.uyttyu7532.photolapse.model.Folder
import java.io.File

class ListHorizontalAdapter(
    private val context: Context,
    private val folder: Folder
) : androidx.recyclerview.widget.RecyclerView.Adapter<ListHorizontalAdapter.ViewHolder>() {

    interface ItemClick
    {
        fun onClick(view: View, position: Int, folder: Folder)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_list_horizontal, viewGroup, false)
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
        holder.title.text = photo.absolute_file_path

        holder.image.load(File(photo.absolute_file_path))
    }

    override fun getItemCount(): Int {
        return folder.photos.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(
        itemView) {
        val title: TextView = itemView.findViewById(R.id.title) as TextView
        val image: ImageView = itemView.findViewById(R.id.image) as ImageView
    }
}