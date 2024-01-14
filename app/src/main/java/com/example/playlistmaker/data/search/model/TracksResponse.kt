package com.example.playlistmaker.data.search.model

import com.example.playlistmaker.domain.player.model.Track
import com.google.gson.annotations.SerializedName


data class TracksResponse(
    @SerializedName("results") val tracks: List<Track>
)
