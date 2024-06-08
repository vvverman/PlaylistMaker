package com.example.playlistmaker.data.search.model

import com.example.playlistmaker.domain.models.Track
import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackId") val id: Long,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long,
    @SerializedName("country") val country: String,
    @SerializedName("releaseDate") val releaseDate: String,
    @SerializedName("artworkUrl100") val artworkUrl100: String,
    @SerializedName("primaryGenreName") val primaryGenreName: String,
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("previewUrl") val previewUrl:String,
    @SerializedName("itemId") val itemId: Long
){

    fun mapToDomain(): Track = Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        genre = primaryGenreName,
        albumName = collectionName,
        country = country,
        releaseDate = releaseDate,
        previewUrl = previewUrl,
        itemId = itemId
    )
}
