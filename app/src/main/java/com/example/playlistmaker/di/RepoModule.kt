package com.example.playlistmaker.di

import com.example.playlistmaker.data.favorites.FavoritesRepo
import com.example.playlistmaker.data.favorites.impl.FavoritesRepoImpl
import com.example.playlistmaker.data.search.impl.HistoryStorageRepoImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepoImpl
import com.example.playlistmaker.data.search.HistoryStorageRepo
import com.example.playlistmaker.data.settings.SettingsRepo
import org.koin.dsl.module

val repoModule = module {

    single<SettingsRepo> {
        SettingsRepoImpl(sharedPreferences = get())
    }

    single<HistoryStorageRepo> {
        HistoryStorageRepoImpl(
            networkClient = get(),
            sharedPreferences = get(),
            sharedPreferencesConverter = get(),
            favoritesDao = get()
        )
    }

    single<FavoritesRepo> {
        FavoritesRepoImpl(favoritesDao = get())
    }
}