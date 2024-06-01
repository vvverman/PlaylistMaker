package com.example.playlistmaker.data.network

import kotlinx.coroutines.Deferred

/**
 * Интерфейс для работы с сетью.
 */
interface NetworkClient {

    /**
     * Выполняет запрос к сети.
     *
     * @param dto Данные, которые нужно передать в запрос.
     * @return Результат запроса.
     */
    suspend fun doRequest(dto: Any): Result
}