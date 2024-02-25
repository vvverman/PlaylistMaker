package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.domain.medialibrary.MediaLibraryInteractor
import com.example.playlistmaker.domain.medialibrary.impl.MediaLibraryInteractorImpl

object MediaLibraryCreator {

    private var mediaLibraryInteractor: MediaLibraryInteractor? = null

    fun provideMediaLibraryInteractor(context: Context): MediaLibraryInteractor {
        val trackRepository = TrackRepoCreator.createTracksRepository(context)
        return mediaLibraryInteractor ?: MediaLibraryInteractorImpl(trackRepository).apply {
            mediaLibraryInteractor = this
        }
    }
}