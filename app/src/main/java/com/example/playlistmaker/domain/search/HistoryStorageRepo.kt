package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.utils.Resource

interface HistoryStorageRepo {
    fun addTrackToHistory(track: Track)
    fun getHistory(): MutableList<Track>
    fun clearHistory()
    fun saveHistory(history: MutableList<Track>)
    fun searchTracks(text: String): Resource<List<Track>>
    fun getPlayingTrack(): Track?
    fun savePlayingTrack(track: Track?)

}