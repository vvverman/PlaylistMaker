package com.example.playlistmaker

import com.google.gson.annotations.SerializedName


data class TracksResponse(
    @SerializedName("results") val tracks: List<Track>
)

