package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.player.model.Track

interface HistoryStorageInteractor {
    fun addTrackToHistory(track: Track)
    fun getHistory(): MutableList<Track>

    fun clearHistory()

    fun saveHistory(history: MutableList<Track>)

}