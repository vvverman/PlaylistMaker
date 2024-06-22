package com.example.playlistmaker.ui.medialibrary.create_playlist

sealed class CreatePlaylistEvent{
    object NavigateBack : CreatePlaylistEvent()
    object ShowBackConfirmationDialog : CreatePlaylistEvent()
    data class SetPlaylistCreatedResult(val playlistName: String) : CreatePlaylistEvent()
}