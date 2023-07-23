package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("trackName") val compositionName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val durationInMillis: Long,
    @SerializedName("artworkUrl100") val coverImageURL: String
)



var items: ArrayList<Item> = arrayListOf()



