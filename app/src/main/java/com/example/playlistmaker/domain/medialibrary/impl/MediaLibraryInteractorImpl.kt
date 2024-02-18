package com.example.playlistmaker.domain.medialibrary.impl

import com.example.playlistmaker.data.search.HistoryStorageRepo
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.medialibrary.MediaLibraryInteractor
class MediaLibraryInteractorImpl(private val trackRepository: HistoryStorageRepo) : MediaLibraryInteractor {

    override fun getTrackForPlaying(): Track? = trackRepository.getPlayingTrack()

}