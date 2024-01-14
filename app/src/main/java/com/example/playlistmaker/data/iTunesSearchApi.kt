package com.example.playlistmaker.data


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


// Определение интерфейса для iTunes Search API
interface ITunesSearchApi {
    @GET("/search")
    fun searchTracks(@Query("term") term: String, @Query("media") media: String): Call<TracksResponse>
}


