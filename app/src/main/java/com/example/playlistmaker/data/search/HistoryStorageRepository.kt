package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.player.model.Track

interface HistoryStorageRepository {
    fun addTrackToHistory(track: Track)
    fun getHistory(): MutableList<Track>

    fun clearHistory()

    fun saveHistory(history: MutableList<Track>)

}