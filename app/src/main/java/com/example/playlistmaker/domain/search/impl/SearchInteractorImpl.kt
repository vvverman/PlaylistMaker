package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.HistoryStorageRepo
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchInteractor
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: HistoryStorageRepo) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(request: String, consumer: SearchInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(request))
        }
    }

    override fun getTracksHistory(): List<Track> = repository.getHistory()

    override fun addTrackToHistory(track: Track) = repository.addTrackToHistory(track)

    override fun clearTrackHistory() = repository.clearHistory()

    override fun saveTrackForPlaying(track: Track) = repository.savePlayingTrack(track)
}