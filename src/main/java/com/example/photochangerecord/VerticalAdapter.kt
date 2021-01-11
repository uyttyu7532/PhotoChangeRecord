package com.example.photochangerecord

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photochangerecord.viewmodel.Folder
import com.takusemba.multisnaprecyclerview.MultiSnapHelper
import com.takusemba.multisnaprecyclerview.SnapGravity


class VerticalAdapter(
    private val context: Context,
    private val folderList: ArrayList<Folder>
) : androidx.recyclerview.widget.RecyclerView.Adapter<VerticalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_vertical, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.folderTitle.text = folderList[position].title

        val adapter = HorizontalAdapter(folderList[position].photos)
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerViewHorizontal.layoutManager = manager
        holder.recyclerViewHorizontal.adapter = adapter

        val multiSnapHelper = MultiSnapHelper(SnapGravity.START, 1, 100f)
        multiSnapHelper.attachToRecyclerView(holder.recyclerViewHorizontal)

    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        val recyclerViewHorizontal= itemView.findViewById(R.id.recycler_view_horizontal) as RecyclerView
        val folderTitle: TextView = itemView.findViewById(R.id.folder_title) as TextView


    }
}