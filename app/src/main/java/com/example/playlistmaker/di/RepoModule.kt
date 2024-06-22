package com.example.playlistmaker.di

import com.example.playlistmaker.data.external_storage.ExternalStorageRepo
import com.example.playlistmaker.data.external_storage.impl.ExternalStorageRepoImpl
import com.example.playlistmaker.data.favorites.FavoritesRepo
import com.example.playlistmaker.data.favorites.impl.FavoritesRepoImpl
import com.example.playlistmaker.data.main.NavigationRepo
import com.example.playlistmaker.data.main.impl.NavigationRepoImpl
import com.example.playlistmaker.data.playlist.PlaylistRepo
import com.example.playlistmaker.data.playlist.PlaylistRepoImpl
import com.example.playlistmaker.data.search.impl.TracksRepoImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepoImpl
import com.example.playlistmaker.data.search.TracksRepo
import com.example.playlistmaker.data.settings.SettingsRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {

    single<SettingsRepo> {
        SettingsRepoImpl(sharedPreferences = get())
    }

    single<TracksRepo> {
        TracksRepoImpl(
            networkClient = get(),
            sharedPreferences = get(),
            sharedPreferencesConverter = get(),
            favoritesDao = get(),
            trackDao = get()
        )
    }

    single<FavoritesRepo> {
        FavoritesRepoImpl(favoritesDao = get())
    }


    single<NavigationRepo> { NavigationRepoImpl() }

    single<PlaylistRepo> {
        PlaylistRepoImpl(playlistDao = get(), trackDao = get())
    }

    single<ExternalStorageRepo> {
        ExternalStorageRepoImpl(context = androidContext())
    }
}