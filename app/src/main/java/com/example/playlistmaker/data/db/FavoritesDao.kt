package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: FavoritesEntity)

    @Delete
    suspend fun deleteTrack(track: FavoritesEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY created_at DESC")
    suspend fun getTracks(): Flow<List<FavoritesEntity>>

    @Query("SELECT id FROM favorite_tracks")
    fun getTracksIds(): List<Long>
}