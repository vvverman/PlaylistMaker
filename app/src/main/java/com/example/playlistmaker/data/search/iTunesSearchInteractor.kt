package com.example.playlistmaker.data.search


import com.example.playlistmaker.data.search.model.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


// Определение интерфейса для iTunes Search API
interface ITunesSearchRepository {
    @GET("/search")
    fun searchTracks(@Query("term") term: String, @Query("media") media: String): Call<TracksResponse>
}


