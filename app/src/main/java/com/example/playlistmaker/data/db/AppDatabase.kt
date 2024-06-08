package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(version = 1, entities = [FavoritesEntity::class])

abstract class AppDatabase : RoomDatabase(){

    abstract fun favoritesDao(): FavoritesDao

    companion object {
        const val NAME = "playlist_maker_database"
    }

}