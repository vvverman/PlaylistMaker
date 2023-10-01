package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("ItemId") val itemId: Long,
    @SerializedName("trackName") val compositionName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val durationInMillis: Long,
    @SerializedName("artworkUrl100") val coverImageURL: String,
    @SerializedName("collectionName") val albumName: String?, // Название альбома (может быть null)
    @SerializedName("releaseDate") val releaseDate: String?, // Год релиза трека (может быть null)
    @SerializedName("primaryGenreName") val genre: String?, // Жанр трека (может быть null)
    @SerializedName("country") val country: String? // Страна исполнителя (может быть null)
)

var tracks: ArrayList<Track> = arrayListOf()



