package com.example.playlistmaker.di

import com.example.playlistmaker.data.search.impl.HistoryStorageRepoImpl


import com.example.playlistmaker.data.settings.impl.SettingsRepoImpl
import com.example.playlistmaker.domain.search.HistoryStorageRepo

import com.example.playlistmaker.domain.settings.SettingsRepo
import org.koin.dsl.module

val repositoryModule = module {

    single<SettingsRepo> {
        SettingsRepoImpl(sharedPreferences = get())
    }

    single<HistoryStorageRepo> {
        HistoryStorageRepoImpl(
            networkClient = get(),
            sharedPreferences = get(),
            sharedPreferencesConverter = get()
        )
    }
}