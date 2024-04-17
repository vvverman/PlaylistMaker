package com.example.playlistmaker.di

import com.example.playlistmaker.ui.medialibrary.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.medialibrary.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.player.view_model.MediaLibraryViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(searchInteractor = get())
    }

    viewModel<MediaLibraryViewModel> {
        MediaLibraryViewModel(playerInteractor = get(), androidApplication())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(settingsInteractor = get(),sharingInteractor = get() )
    }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel()
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel()
    }
}