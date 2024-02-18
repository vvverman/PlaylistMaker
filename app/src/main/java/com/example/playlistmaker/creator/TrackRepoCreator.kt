package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.creator.SharedPreferencesCreator.createSharedPreferences
import com.example.playlistmaker.data.network.impl.RetrofitNetworkClient
import com.example.playlistmaker.data.search.HistoryStorageRepo
import com.example.playlistmaker.data.search.impl.HistoryStorageRepoImpl
import com.example.playlistmaker.data.utils.SharedPreferenceConverterImpl
import com.example.playlistmaker.domain.utils.SharedPreferenceConverter

object TrackRepoCreator {
    private var trackRepository: HistoryStorageRepoImpl? = null
    private var retrofitClient: RetrofitNetworkClient? = null
    private var sharedPreferencesConverter: SharedPreferenceConverter? = null

    fun createTracksRepository(context: Context): HistoryStorageRepo {

        return trackRepository ?: HistoryStorageRepoImpl(
            networkClient = retrofitClient ?: RetrofitNetworkClient().apply {
                retrofitClient = this
            },
            sharedPreferences = createSharedPreferences(context),
            sharedPreferencesConverter = createSharedPreferenceConverter()
        ).apply {
            trackRepository = this
        }
    }

    private fun createSharedPreferenceConverter(): SharedPreferenceConverter {
        return sharedPreferencesConverter ?: SharedPreferenceConverterImpl().apply {
            sharedPreferencesConverter = this
        }
    }
}