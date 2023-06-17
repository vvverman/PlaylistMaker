package com.example.playlistmaker

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.URI

class ItemsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val compositionNameView: TextView = itemView.findViewById(R.id.compositionName)
    private val artistNameView: TextView = itemView.findViewById(R.id.artistName)
    private val durationView: TextView = itemView.findViewById(R.id.duration)
    private val coverImageURLView: ImageView = itemView.findViewById(R.id.coverImageURL)

    fun bind(item: Item) {
        compositionNameView.text = item.compositionName
        artistNameView.text = item.artistName
        durationView.text = item.duration
        coverImageURLView.setImageURI(Uri.parse("item.coverImageURL"))
    }
}



