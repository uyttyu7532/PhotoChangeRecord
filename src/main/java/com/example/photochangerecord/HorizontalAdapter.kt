package com.example.photochangerecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.photochangerecord.viewmodel.Photo

class HorizontalAdapter(
    private val photos: ArrayList<Photo>
) : androidx.recyclerview.widget.RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_horizontal, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photos[position]
        holder.title.text = photo.date

        // TODO Glide로 해야되나  (데이터 바인딩..)
        holder.image.setImageResource(photo.resourceID)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(
        itemView) {
        val title: TextView = itemView.findViewById(R.id.title) as TextView
        val image: ImageView = itemView.findViewById(R.id.image) as ImageView
    }
}