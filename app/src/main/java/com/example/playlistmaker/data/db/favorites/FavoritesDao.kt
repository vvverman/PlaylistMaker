package com.example.playlistmaker.data.db.favorites

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

    @Query("SELECT * FROM favorites ORDER BY created_at DESC")
    fun getTracks(): Flow<List<FavoritesEntity>>

    @Query("SELECT id FROM favorites")
    fun getTracksIds(): Flow<List<Long>>
}