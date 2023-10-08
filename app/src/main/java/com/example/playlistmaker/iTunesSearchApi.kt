package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


// Определение интерфейса для iTunes Search API
interface ITunesSearchApi {
    @GET("/search")
    fun searchTracks(@Query("term") term: String, @Query("media") media: String): Call<TracksResponse>
}

// Метод для создания плейлиста на основе полученных треков
fun createPlaylist(tracks: List<Track>) {
    println("Your Playlist:")
    for (item in tracks) {
        println("${item.compositionName} - ${item.artistName} (${item.durationInMillis})")
        println("Cover Image URL: ${item.coverImageURL}")
        println("----------------------------------")
    }
}

