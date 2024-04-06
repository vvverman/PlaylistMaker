package com.example.playlistmaker.data.search.impl


import android.content.SharedPreferences
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.search.model.TracksResult
import com.example.playlistmaker.data.search.model.TracksSearchRequest
import com.example.playlistmaker.domain.search.HistoryStorageRepo
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.utils.Resource
import com.example.playlistmaker.domain.utils.SharedPreferenceConverter

class HistoryStorageRepoImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesConverter: SharedPreferenceConverter
) : HistoryStorageRepo {
    companion object {
        const val KEY_TRACKS_HISTORY = "key_tracks_history"
        const val MAX_TRACKS_HISTORY_SIZE = 10
    }

    private var playingTrack: Track? = null

    override fun searchTracks(text: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(text))
        return when (response.resultCode){
            -1 -> Resource.Error("Проверьте подключение к интернету")
            200 -> {
                Resource.Success((response as TracksResult).results.map {
                    Track(
                        itemId = it.itemId,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeMillis = it.trackTimeMillis,
                        artworkUrl100 = it.artworkUrl100,
                        genre = it.primaryGenreName,
                        albumName = it.collectionName,
                        country = it.country,
                        releaseDate = it.releaseDate,
                        previewUrl = it.previewUrl
                    )
                })
            }
            else -> Resource.Error("Ошибка сервера")
        }
    }



    override fun addTrackToHistory(track: Track) {
        val tracks: MutableList<Track> =
            sharedPreferences.getString(KEY_TRACKS_HISTORY, null)?.let {
                sharedPreferencesConverter.convertJsonToList(it).toMutableList()
            } ?: mutableListOf()

        if (track in tracks.toSet()) {
            tracks.remove(track)
        }

        if (tracks.size >= MAX_TRACKS_HISTORY_SIZE) {
            tracks.removeLast()
        }
        tracks.add(0, track)
        val tracksString = sharedPreferencesConverter.convertListToJson(tracks)
        sharedPreferences.edit()
            .putString(KEY_TRACKS_HISTORY, tracksString)
            .apply()
    }

    override fun getHistory(): MutableList<Track> {
        return sharedPreferences.getString(KEY_TRACKS_HISTORY, null)?.let {
            sharedPreferencesConverter.convertJsonToList(it).toMutableList()
        } ?: mutableListOf()
    }

    override fun clearHistory() {
        sharedPreferences.edit().putString(KEY_TRACKS_HISTORY, null).apply()
    }


    override fun saveHistory(history: MutableList<Track>) {
        TODO("Not yet implemented")
    }



    override fun getPlayingTrack(): Track? {
        return playingTrack?.let {
            val copy = playingTrack?.copy()
            playingTrack = null
            copy
        }
    }

    override fun savePlayingTrack(track: Track?) {
        playingTrack = track
    }
}
