package com.example.playlistmaker.data.network.impl

import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.Result
import com.example.playlistmaker.data.network.TrackApi
import com.example.playlistmaker.data.search.model.TracksSearchRequest

class RetrofitNetworkClient (private val trackApi: TrackApi): NetworkClient {

    override fun doRequest(dto: Any): Result {
        return if (dto is TracksSearchRequest) {
            try {
                val response = trackApi.searchTracks(dto.text).execute()
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




//package com.example.playlistmaker.data.network.impl
//
//import com.example.playlistmaker.data.network.NetworkClient
//import com.example.playlistmaker.data.network.Result
//import com.example.playlistmaker.data.network.TrackApi
//import com.example.playlistmaker.data.search.model.TracksSearchRequest
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class RetrofitNetworkClient : NetworkClient {
//
//    private val BASE_URL = "https://itunes.apple.com/"
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    private val api: TrackApi = retrofit.create(TrackApi::class.java)
//
//    override fun doRequest(dto: Any): Result {
//        return if (dto is TracksSearchRequest) {
//            try {
//                val response = api.searchTracks(dto.text).execute()
//                response.body()?.apply {
//                    resultCode = response.code()
//                } ?: Result(resultCode = response.code())
//            } catch (e: Throwable) {
//                Result(resultCode = -1)
//            }
//        } else {
//            Result(resultCode = 400)
//        }
//    }
//}
