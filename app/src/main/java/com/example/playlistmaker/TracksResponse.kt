package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

//data class TracksResponse(
//    @SerializedName("trackName") val trackName: String,
//    @SerializedName("artistName") val artistName: String,
//    @SerializedName("trackTimeMillis") val trackTimeMillis: Int,
//    @SerializedName("artworkUrl100") val artworkUrl100: String
//)

data class TracksResponse(
    @SerializedName("results") val items: List<Item>
)

