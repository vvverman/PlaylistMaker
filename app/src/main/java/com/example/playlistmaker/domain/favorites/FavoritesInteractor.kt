package com.example.playlistmaker.domain.favorites

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    suspend fun getFavorites(): Flow<List<Track>>
    fun saveTrackForPlaying(track: Track)
}