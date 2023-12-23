package com.example.playlistmaker.domain

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
    @SerializedName("country") val country: String?, // Страна исполнителя (может быть null)
    @SerializedName("previewUrl") val previewUrl: String? // Ссылка на отрывок трека (может быть null)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Track

        if (itemId != other.itemId) return false
        if (compositionName != other.compositionName) return false
        if (artistName != other.artistName) return false
        if (durationInMillis != other.durationInMillis) return false
        if (coverImageURL != other.coverImageURL) return false
        if (albumName != other.albumName) return false
        if (releaseDate != other.releaseDate) return false
        if (genre != other.genre) return false
        if (country != other.country) return false
        if (previewUrl != other.previewUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + compositionName.hashCode()
        result = 31 * result + artistName.hashCode()
        result = 31 * result + durationInMillis.hashCode()
        result = 31 * result + coverImageURL.hashCode()
        result = 31 * result + (albumName?.hashCode() ?: 0)
        result = 31 * result + (releaseDate?.hashCode() ?: 0)
        result = 31 * result + (genre?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + (previewUrl?.hashCode() ?: 0)
        return result

    }
}


