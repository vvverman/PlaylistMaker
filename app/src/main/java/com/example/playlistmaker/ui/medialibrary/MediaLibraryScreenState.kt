package com.example.playlistmaker.ui.medialibrary

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.medialibrary.model.MediaLibraryState

data class MediaLibraryScreenState(
    val mediaLibraryState: MediaLibraryState = MediaLibraryState.PREPARED,
    val track: Track?=null,
    val trackTime: String = ""
)
