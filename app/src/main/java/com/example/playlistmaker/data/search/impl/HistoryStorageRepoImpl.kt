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

//    override fun searchTracks(history: MutableList<Track>) {
//        TODO("Not yet implemented")
//    }


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



//package com.example.playlistmaker.data.search.impl
//
//import android.content.SharedPreferences
//import com.example.playlistmaker.domain.search.HistoryStorageRepo
//import com.example.playlistmaker.domain.models.Track
//import com.example.playlistmaker.domain.utils.Resource
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
///**
// * Класс SearchHistory предназначен для управления историей поисковых запросов.
// *
// * @property sharedPreferences Используется для доступа к хранилищу SharedPreferences.
// */
//class HistoryStorageRepoImpl(private val sharedPreferences: SharedPreferences) : HistoryStorageRepo {
//
//    // Создаем объект Gson для сериализации и десериализации данных.
//    private val gson = Gson()
//
//    // Ключ, по которому будет сохраняться история в SharedPreferences.
//    private val historyKey = "search_history"
//
//    /**
//     * Метод для добавления элемента в историю поиска.
//     *
//     * @param track Элемент типа Item, который будет добавлен в историю.
//     */
//    override fun addTrackToHistory(track: Track) {
//        // Получаем текущую историю поиска.
//        val history = getHistory()
//
//        // Удаляем элемент с таким же идентификатором (дубликат), если такой уже существует.
//        history.removeIf {it == track}
//
//        // Добавляем новый элемент в начало списка.
//        history.add(0, track)
//
//        // Если история стала длиннее 10 элементов, удаляем последний элемент.
//        if (history.size > 10) {
//            history.removeAt(history.size - 1)
//        }
//
//        // Сохраняем обновленную историю.
//        saveHistory(history)
//    }
//
//    /**
//     * Метод для получения текущей истории поиска.
//     *
//     * @return Список элементов типа Item, представляющих историю поиска.
//     */
//    override fun getHistory(): MutableList<Track> {
//        // Получаем историю в виде JSON-строки из SharedPreferences.
//        val historyString = sharedPreferences.getString(historyKey, "")
//
//        return if (historyString.isNullOrEmpty()) {
//            // Если история пуста или отсутствует, возвращаем пустой список.
//            mutableListOf()
//        } else {
//            // Десериализуем JSON-строку в список элементов Item с использованием TypeToken.
//            gson.fromJson(historyString, object : TypeToken<MutableList<Track>>() {}.type)
//        }
//    }
//
//    /**
//     * Метод для очистки истории поиска.
//     */
//    override fun clearHistory() {
//        // Удаляем историю из SharedPreferences.
//        sharedPreferences.edit().remove(historyKey).apply()
//    }
//
//    /**
//     * Метод для сохранения обновленной истории в SharedPreferences.
//     *
//     * @param history Список элементов типа Item, представляющих обновленную историю поиска.
//     */
//    override fun saveHistory(history: MutableList<Track>) {
//        // Сериализуем список элементов в JSON-строку.
//        val historyString = gson.toJson(history)
//
//        // Сохраняем JSON-строку в SharedPreferences.
//        sharedPreferences.edit().putString(historyKey, historyString).apply()
//    }
//
//    override fun searchTracks(text: String): Resource<List<Track>> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getTrackForPlaying(): Track? {
//        TODO("Not yet implemented")
//    }
//}