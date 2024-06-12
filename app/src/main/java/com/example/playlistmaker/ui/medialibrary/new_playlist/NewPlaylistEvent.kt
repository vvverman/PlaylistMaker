package com.example.playlistmaker.ui.medialibrary.new_playlist

sealed class NewPlaylistEvent{
    object NavigateBack : NewPlaylistEvent()
    object ShowBackConfirmationDialog : NewPlaylistEvent()
    data class SetPlaylistCreatedResult(val playlistName: String) : NewPlaylistEvent()
}