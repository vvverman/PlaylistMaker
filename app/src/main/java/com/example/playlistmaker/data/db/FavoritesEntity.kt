package com.example.playlistmaker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.domain.models.Track

@Entity(tableName = "favorites")
data class  FavoritesEntity
    (
    @PrimaryKey
    val id: Long,
    @ColumnInfo("track_name")
    val trackName: String,
    @ColumnInfo("artist_name")
    val artistName: String,
    @ColumnInfo("track_time_millis")
    val trackTimeMillis: Long,
    @ColumnInfo("artwork_url_100")
    val artworkUrl100: String,
    @ColumnInfo("genre")
    val genre: String,
    @ColumnInfo("album_name")
    val albumName: String,
    @ColumnInfo("country")
    val country: String,
    @ColumnInfo("release_date")
    val releaseDate: String,
    @ColumnInfo("preview_url")
    val previewUrl: String,
    @ColumnInfo("created_at")
    val createdAt: Long
){
    companion object {

        fun mapFromDomain(
            track: Track,
            createdAt: Long = 0L
        ): FavoritesEntity = FavoritesEntity(
            id = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            genre = track.genre,
            albumName = track.albumName,
            country = track.country,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl,
            createdAt = createdAt
        )
    }

    fun mapToDomain(): Track = Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        genre = genre,
        albumName = albumName,
        country = country,
        releaseDate = releaseDate,
        previewUrl = previewUrl,
        isFavorite = true
    )
}