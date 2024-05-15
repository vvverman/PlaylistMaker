package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.data.search.HistoryStorageRepo
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerInteractor
class PlayerInteractorImpl(private val historyStorageRepo: HistoryStorageRepo) : PlayerInteractor {

    override fun getTrackForPlaying(): Track? = historyStorageRepo.getPlayingTrack()

}