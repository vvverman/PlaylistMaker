package com.example.playlistmaker.ui.search.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.utils.DateFormat
import com.example.playlistmaker.ui.util.load


class TrackViewHolder(
    private val binding: TrackBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        with(binding) {
            artistName.text = track.artistName
            compositionName.text = track.trackName
            duration.text = DateFormat.formatMillisToString(track.trackTimeMillis)
            coverImageURL.load(track.artworkUrl100)
        }
    }
}