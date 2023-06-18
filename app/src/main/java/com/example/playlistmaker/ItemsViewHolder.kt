package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class ItemsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val compositionNameView: TextView = itemView.findViewById(R.id.compositionName)
    private val artistNameView: TextView = itemView.findViewById(R.id.artistName)
    private val durationView: TextView = itemView.findViewById(R.id.duration)
    private val coverImageURLView: ImageView = itemView.findViewById(R.id.coverImageURL)

    fun bind(item: Item) {
        compositionNameView.text = item.compositionName
        artistNameView.text = item.artistName
        durationView.text = item.duration

        Glide
            .with(itemView)
            .load(item.coverImageURL)
            .transform(
                MultiTransformation(
                    RoundedCorners(2),
                    CenterCrop()
                ))
            .placeholder(R.drawable.placeholder_track)

            .into(coverImageURLView)
    }
}



