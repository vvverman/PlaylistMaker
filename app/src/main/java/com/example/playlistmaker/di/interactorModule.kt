package com.example.playlistmaker.di

import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.share.SharingInteractor
import com.example.playlistmaker.domain.share.impl.SharingInteractorImpl
import com.example.playlistmaker.ui.navigation.AgreementNavigator
import com.example.playlistmaker.ui.navigation.impl.AgreementNavigatorImpl
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.favorites.impl.FavoritesInteractorImpl
import org.koin.dsl.module


val interactorModule = module {

    single<SearchInteractor> {
        SearchInteractorImpl(historyStorageRepo = get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(historyStorageRepo = get())
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
        FavoritesInteractorImpl(favoritesRepo = get(), historyStorageRepo = get())
    }

}