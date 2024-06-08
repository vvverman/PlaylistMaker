package com.example.playlistmaker.data.favorites.impl

import com.example.playlistmaker.data.db.FavoritesDao
import com.example.playlistmaker.data.db.FavoritesEntity
import com.example.playlistmaker.data.favorites.FavoritesRepo
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepoImpl(
    private val favoritesDao: FavoritesDao
): FavoritesRepo {
    override suspend fun addTrackToFavorites(track: Track) {
        favoritesDao.addTrack(FavoritesEntity.mapFromDomain(track, System.currentTimeMillis()))
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        favoritesDao.deleteTrack(FavoritesEntity.mapFromDomain(track))
    }

    override suspend fun getFavorites(): Flow<List<Track>> {
        return favoritesDao.getTracks().map {
            it.map { entity -> entity.mapToDomain() }
        }
    }
}