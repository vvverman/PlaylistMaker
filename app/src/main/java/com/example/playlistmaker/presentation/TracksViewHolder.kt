package com.example.playlistmaker.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.Track
import com.example.playlistmaker.R

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val compositionNameView: TextView = itemView.findViewById(R.id.compositionName)
    private val artistNameView: TextView = itemView.findViewById(R.id.artistName)
    private val durationView: TextView = itemView.findViewById(R.id.duration)
    private val coverImageURLView: ImageView = itemView.findViewById(R.id.coverImageURL)

    fun bind(track: Track) {
        compositionNameView.text = track.compositionName
        artistNameView.text = track.artistName

        val minutes = track.durationInMillis / 1000 / 60
        val seconds = (track.durationInMillis / 1000) % 60

        val durationText = String.format("%02d:%02d", minutes, seconds)
        durationView.text = durationText

        Glide
            .with(itemView)
            .load(track.coverImageURL)
            .transform(
                MultiTransformation(
                    RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.roundedCornersValue)),
                    CenterCrop()
                ))
            .placeholder(R.drawable.placeholder_track)
            .into(coverImageURLView)
    }
}