package com.example.playlistmaker.domain.model

data class Track(
    val id:Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val genre: String?,
    val albumName: String?,
    val country: String,
    val releaseDate: String,
    val previewUrl: String,
    val isFavorite: Boolean = false
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

        return true
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + (trackName?.hashCode() ?: 0)
        result = 31 * result + (artistName?.hashCode() ?: 0)
        result = 31 * result + (trackTimeMillis?.hashCode() ?: 0)
        result = 31 * result + (artworkUrl100?.hashCode() ?: 0)
        result = 31 * result + (genre?.hashCode() ?: 0)
        result = 31 * result + (albumName?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + (releaseDate?.hashCode() ?: 0)
        result = 31 * result + (previewUrl?.hashCode() ?: 0)
        return result
    }
}
