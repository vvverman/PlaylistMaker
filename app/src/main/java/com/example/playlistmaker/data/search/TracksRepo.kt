package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepo {

    fun searchTracks(text: String): Flow<Resource<List<Track>>>

    fun addTrackToHistory(track: Track)

    fun getHistory(): MutableList<Track>

    fun clearHistory()

    fun saveHistory(history: MutableList<Track>)

    fun getPlayingTrack(): Track?

    fun savePlayingTrack(track: Track?)

    suspend fun deleteTrack(trackId: Long)
}