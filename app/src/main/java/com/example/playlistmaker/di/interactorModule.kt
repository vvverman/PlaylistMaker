package com.example.playlistmaker.di

import com.example.playlistmaker.domain.medialibrary.MediaLibraryInteractor
import com.example.playlistmaker.domain.medialibrary.impl.MediaLibraryInteractorImpl
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.share.SharingInteractor
import com.example.playlistmaker.domain.share.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<SearchInteractor> {
        SearchInteractorImpl(historyStorageRepo = get())
    }

    single<MediaLibraryInteractor> {
        MediaLibraryInteractorImpl(trackRepository = get())
    }

    single<SettingsInteractor>(createdAtStart = true) {
        SettingsInteractorImpl(settingsRepo = get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(externalBrowser = get())
    }
}