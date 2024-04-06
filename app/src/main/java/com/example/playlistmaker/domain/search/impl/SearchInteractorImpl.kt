package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.HistoryStorageRepo
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchInteractor
import java.util.concurrent.Executors

class SearchInteractorImpl(private val historyStorageRepo: HistoryStorageRepo) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(request: String, consumer: SearchInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(historyStorageRepo.searchTracks(request))
        }
    }

    override fun getTracksHistory(): List<Track> = historyStorageRepo.getHistory()

    override fun addTrackToHistory(track: Track) = historyStorageRepo.addTrackToHistory(track)

    override fun clearTrackHistory() = historyStorageRepo.clearHistory()

    override fun saveTrackForPlaying(track: Track) = historyStorageRepo.savePlayingTrack(track)
}