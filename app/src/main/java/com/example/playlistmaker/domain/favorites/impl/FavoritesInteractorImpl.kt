package com.example.playlistmaker.domain.favorites.impl

import com.example.playlistmaker.data.favorites.FavoritesRepo
import com.example.playlistmaker.data.search.HistoryStorageRepo
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepo: FavoritesRepo,
    private val historyStorageRepo: HistoryStorageRepo
): FavoritesInteractor {
    override suspend fun addToFavorites(track: Track) {
        favoritesRepo.addTrackToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoritesRepo.deleteTrackFromFavorites(track)
    }

    override suspend fun getFavorites(): Flow<List<Track>> {
        return favoritesRepo.getFavorites()
    }

    override fun saveTrackForPlaying(track: Track) = historyStorageRepo.savePlayingTrack(track)
}