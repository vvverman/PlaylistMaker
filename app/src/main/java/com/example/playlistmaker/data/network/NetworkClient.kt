package com.example.playlistmaker.data.network

interface NetworkClient {

    suspend fun doRequest(dto: Any): Result
}