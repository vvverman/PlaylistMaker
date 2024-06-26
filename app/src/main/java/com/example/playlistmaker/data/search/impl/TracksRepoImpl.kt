package com.example.playlistmaker.data.search.impl


import android.content.SharedPreferences
import com.example.playlistmaker.data.db.favorites.FavoritesDao
import com.example.playlistmaker.data.db.track.TrackDao
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.search.model.TracksResult
import com.example.playlistmaker.data.search.model.TracksSearchRequest
import com.example.playlistmaker.data.search.TracksRepo
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.utils.Resource
import com.example.playlistmaker.domain.utils.SharedPreferenceConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first


class TracksRepoImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesConverter: SharedPreferenceConverter,
    private val favoritesDao: FavoritesDao,
    private val trackDao: TrackDao
) : TracksRepo {
    companion object {
        const val KEY_TRACKS_HISTORY = "key_tracks_history"
        const val MAX_TRACKS_HISTORY_SIZE = 10
    }

    private var playingTrack: Track? = null

    override fun searchTracks(text: String): Flow<Resource<List<Track>>> {
        return flow {
            val response = networkClient.doRequest(TracksSearchRequest(text))
            when (response.resultCode) {
                -1 -> emit(Resource.Error("Проверьте подключение к интернету"))
                200 -> {
                    val tracks = (response as TracksResult).results.map { it.mapToDomain() }
                    val favoritesIds = favoritesDao.getTracksIds().first().toSet()
                    emit(
                        Resource.Success(
                            tracks.map { it.copy(isFavorite = it.id in favoritesIds) }
                        )
                    )
                }

                else -> emit(Resource.Error("Ошибка сервера"))
            }
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


    override suspend fun deleteTrack(trackId: Long) = trackDao.deleteTrack(trackId)

}
