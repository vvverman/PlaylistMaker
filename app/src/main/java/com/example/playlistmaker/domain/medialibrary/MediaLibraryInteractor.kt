package com.example.playlistmaker.domain.medialibrary

import com.example.playlistmaker.domain.models.Track

interface MediaLibraryInteractor {

    fun getTrackForPlaying(): Track?
}


