package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.search.model.TracksResult
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Интерфейс для работы с API поиска треков.
 */
interface TracksApi {

    /**
     * Поиск треков по заданному тексту.
     *
     * @param text Текст для поиска треков.
     * @return Результат поиска треков.
     */
    @GET("search?entity=song")
    suspend fun searchTracks(@Query("term") text: String): TracksResult
}