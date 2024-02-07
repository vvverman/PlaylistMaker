package com.example.playlistmaker.creator

object Creator {
    fun getRepository(): TracksRepositoryImpl {
        return TracksRepositoryImpl(NetworkClientImpl())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getRepository())
    }
}