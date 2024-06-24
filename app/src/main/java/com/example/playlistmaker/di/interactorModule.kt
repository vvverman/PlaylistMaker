package com.example.playlistmaker.di

import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.example.playlistmaker.ui.navigation.AgreementNavigator
import com.example.playlistmaker.ui.navigation.impl.AgreementNavigatorImpl
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.favorites.impl.FavoritesInteractorImpl
import org.koin.dsl.module
import com.example.playlistmaker.domain.main.NavigationInteractor
import com.example.playlistmaker.domain.main.impl.NavigationInteractorImpl
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.impl.PlaylistInteractorImpl

val interactorModule = module {

    single<SearchInteractor> {
        SearchInteractorImpl(tracksRepo = get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(tracksRepo = get())
    }

    single<SettingsInteractor>(createdAtStart = true) {
        SettingsInteractorImpl(settingsRepo = get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get()) // Добавьте параметр agreementNavigator
    }

    single<AgreementNavigator>(createdAtStart = true) {
        AgreementNavigatorImpl(context = get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(favoritesRepo = get(), tracksRepo = get())
    }


    single<NavigationInteractor> {
        NavigationInteractorImpl(navigationRepo = get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(
            externalStorageRepo = get(),
            playlistRepo = get(),
            trackRepo = get()
        )
    }

}