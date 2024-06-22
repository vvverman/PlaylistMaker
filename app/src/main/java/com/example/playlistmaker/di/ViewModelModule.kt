
package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.ui.medialibrary.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.medialibrary.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.player.fragment.view_model.MediaLibraryViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.ui.main.view_model.MainViewModel
import com.example.playlistmaker.ui.media.playlist_info.view_model.PlaylistInfoViewModel
import com.example.playlistmaker.ui.medialibrary.create_playlist.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.ui.medialibrary.edit_playlist.view_model.EditPlaylistViewModel


val viewModelModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(searchInteractor = get())
    }

    factory { MediaPlayer() }

    viewModel<MediaLibraryViewModel> {
        MediaLibraryViewModel(
            favoritesInteractor = get(),
            playlistInteractor = get(),
            playerInteractor = get(),
            mediaPlayer = get(),
            application = androidApplication())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(settingsInteractor = get(),sharingInteractor = get() )
    }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel(favoritesInteractor = get())
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel(
            navigationInteractor = get(), playlistInteractor = get())
    }
    viewModel<CreatePlaylistViewModel> {
        CreatePlaylistViewModel(navigationInteractor = get(), playlistInteractor = get())
    }

    viewModel<MainViewModel> {
        MainViewModel(navigationInteractor = get())
    }


    viewModel<PlaylistInfoViewModel> {
        PlaylistInfoViewModel(
            playlistInteractor = get(),
            savedStateHandle = get(),
            sharingInteractor = get(),
            navigationInteractor = get()
        )
    }

    viewModel<EditPlaylistViewModel> {
        EditPlaylistViewModel(
            navigationInteractor = get(),
            playlistInteractor = get(),
            savedStateHandle = get()
        )
    }

}