package com.example.playlistmaker.data.network.impl

import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.Result
import com.example.playlistmaker.data.network.TracksApi
import com.example.playlistmaker.data.search.model.TracksSearchRequest

class RetrofitNetworkClient (private val tracksApi: TracksApi): NetworkClient {

    override fun doRequest(dto: Any): Result {
        return if (dto is TracksSearchRequest) {
            try {
                val response = tracksApi.searchTracks(dto.text).execute()
                response.body()?.apply {
                    resultCode = response.code()
                } ?: Result(resultCode = response.code())
            } catch (e: Throwable) {
                Result(resultCode = -1)
            }
        } else {
            Result(resultCode = 400)
        }
    }
}


