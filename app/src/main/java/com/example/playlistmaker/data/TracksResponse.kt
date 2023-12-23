package com.example.playlistmaker.data

import com.example.playlistmaker.domain.Track
import com.google.gson.annotations.SerializedName


data class TracksResponse(
    @SerializedName("results") val tracks: List<Track>
)
