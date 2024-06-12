package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.data.search.TracksRepo
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerInteractor
class PlayerInteractorImpl(private val tracksRepo: TracksRepo) : PlayerInteractor {

    override fun getTrackForPlaying(): Track? = tracksRepo.getPlayingTrack()

}