package com.example.playlistmaker.domain.models

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val genre: String,
    val albumName: String,
    val country: String,
    val releaseDate: String,
    val previewUrl: String,
    val itemId: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Track

        if (trackName != other.trackName) return false
        if (artistName != other.artistName) return false
        if (trackTimeMillis != other.trackTimeMillis) return false
        if (artworkUrl100 != other.artworkUrl100) return false
        if (genre != other.genre) return false
        if (albumName != other.albumName) return false
        if (country != other.country) return false
        if (releaseDate != other.releaseDate) return false
        if (previewUrl != other.previewUrl) return false
        if (itemId != other.itemId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = trackName.hashCode()
        result = 31 * result + artistName.hashCode()
        result = 31 * result + trackTimeMillis.hashCode()
        result = 31 * result + artworkUrl100.hashCode()
        result = 31 * result + genre.hashCode()
        result = 31 * result + albumName.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + previewUrl.hashCode()
        result = 31 * result + itemId.hashCode()
        return result
    }
}
