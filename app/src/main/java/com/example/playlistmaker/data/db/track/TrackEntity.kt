package com.example.playlistmaker.data.db.track

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.domain.model.Track

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo("track_name")
    val trackName: String,
    @ColumnInfo("artist_name")
    val artistName: String,
    @ColumnInfo("track_time_millis")
    val trackTimeMillis: Long,
    @ColumnInfo("artwork_url")
    val artworkUrl100: String,
    @ColumnInfo("artwork_url_60")
    val artworkUrl60: String,
    @ColumnInfo("primary_genre_url")
    val primaryGenreName: String?,
    @ColumnInfo("collection_name")
    val collectionName: String,
    @ColumnInfo("country")
    val country: String,
    @ColumnInfo("release_date")
    val releaseDate: String,
    @ColumnInfo("preview_url")
    val previewUrl: String,
    @ColumnInfo("created_at")
    val createdAt: Long
) {
    companion object {

        fun mapFromDomain(track: Track): TrackEntity = TrackEntity(
            id = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            artworkUrl60 = track.artworkUrl60,
            primaryGenreName = track.genre,
            collectionName = track.albumName?: "",
            country = track.country,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl,
            createdAt = System.currentTimeMillis()
        )
    }
    fun mapToDomain(): Track = Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        artworkUrl60 = artworkUrl60,
        genre = primaryGenreName,
        albumName = collectionName,
        country = country,
        releaseDate = releaseDate,
        previewUrl = previewUrl
    )
}