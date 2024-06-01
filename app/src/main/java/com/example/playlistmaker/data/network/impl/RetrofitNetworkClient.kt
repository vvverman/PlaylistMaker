package com.example.playlistmaker.data.network.impl

import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.Result
import com.example.playlistmaker.data.network.TracksApi
import com.example.playlistmaker.data.search.model.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient (private val tracksApi: TracksApi): NetworkClient {


override suspend fun doRequest(dto: Any): Result {
    return if (dto is TracksSearchRequest) {
        withContext(Dispatchers.IO) {
            try {
                tracksApi.searchTracks(dto.text).apply {
                    resultCode = 200
                }
            } catch (e: Throwable) {
                Result(resultCode = -1)
            }
        }
    } else {
        Result(resultCode = 400)
    }
}
}


