package com.example.playlistmaker

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

abstract class ItemsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val compositionNameView: TextView = itemView.findViewById(R.id.compositionName)
    private val artistNameView: TextView = itemView.findViewById(R.id.artistName)
    private val durationView: TextView = itemView.findViewById(R.id.duration)
    abstract val coverImageURLView: ImageView

    fun bind(item: Item) {
        compositionNameView.text = item.compositionName
        artistNameView.text = item.artistName
        durationView.text = item.duration
//        coverImageURLView.setImageURI(Uri.parse("item.coverImageURL"))

        Glide
            .with(itemView)
            .load(item.coverImageURL)
            .placeholder(R.drawable.poster)
            .transform(CenterCrop())
            .into(coverImageURLView)
    }
}



