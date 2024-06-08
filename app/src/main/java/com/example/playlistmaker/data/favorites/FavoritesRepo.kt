package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepo {
    suspend  fun addTrackToFavorites(track: Track)
    suspend  fun deleteTrackFromFavorites(track: Track)
    suspend  fun getFavorites(): Flow<List<Track>>
}