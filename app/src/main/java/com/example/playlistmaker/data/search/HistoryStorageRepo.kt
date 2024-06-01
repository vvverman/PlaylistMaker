package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс для работы с историей поиска треков.
 */
interface HistoryStorageRepo {

    /**
     * Поиск треков по заданному тексту.
     *
     * @param text Текст для поиска треков.
     * @return Поток ресурсов, содержащий список найденных треков или сообщение об ошибке.
     */
    fun searchTracks(text: String): Flow<Resource<List<Track>>>

    /**
     * Добавление трека в историю поиска.
     *
     * @param track Трек, который нужно добавить в историю.
     */
    fun addTrackToHistory(track: Track)

    /**
     * Получение списка треков из истории поиска.
     *
     * @return Список треков из истории поиска.
     */
    fun getHistory(): MutableList<Track>

    /**
     * Очистка истории поиска.
     */
    fun clearHistory()

    /**
     * Сохранение истории поиска.
     *
     * @param history Список треков, которую нужно сохранить.
     */
    fun saveHistory(history: MutableList<Track>)

    /**
     * Получение трека, который в данный момент воспроизводится.
     *
     * @return Трек, который в данный момент воспроизводится, или null, если в данный момент ничего не воспроизводится.
     */
    fun getPlayingTrack(): Track?

    /**
     * Сохранение трека для воспроизведения.
     *
     * @param track Трек, который нужно сохранить для воспроизведения.
     */
    fun savePlayingTrack(track: Track?)
}