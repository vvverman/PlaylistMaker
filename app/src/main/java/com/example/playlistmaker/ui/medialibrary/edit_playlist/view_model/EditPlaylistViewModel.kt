package com.example.playlistmaker.ui.medialibrary.edit_playlist.view_model

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.main.NavigationInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.ui.medialibrary.create_playlist.CreatePlaylistEvent
import com.example.playlistmaker.ui.medialibrary.create_playlist.view_model.CreatePlaylistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val navigationInteractor: NavigationInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val savedStateHandle: SavedStateHandle
) : CreatePlaylistViewModel(
    navigationInteractor,
    playlistInteractor
) {

    companion object {
        private const val KEY_PLAYLIST = "playlist"
    }

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    init {
        setPlaylistData()
    }

    override fun onCompleteButtonClicked() {
        playlist.value?.let {
            val updatedPlaylist = it.copy(name = playlistName, description = playlistDescription)
            viewModelScope.launch(Dispatchers.IO) {
                playlistInteractor.addTrackToPlaylist(updatedPlaylist, playlistCoverUri.value)
                event.postValue(CreatePlaylistEvent.NavigateBack)
            }
        }
    }

    override fun onBackPressed() = event.postValue(CreatePlaylistEvent.NavigateBack)

    private fun setPlaylistData() {
        savedStateHandle.get<Playlist>(KEY_PLAYLIST)?.let {
            _playlistCoverUri.value = it.coverUri?.toUri()
            _playlist.value = it
        }
    }
}
