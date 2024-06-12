package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс, который определяет контракт для поиска и работы с треками.
 */
interface SearchInteractor {

    /**
     * Выполняет поиск треков, соответствующих указанному запросу.
     *
     * @param request Строка поискового запроса.
     * @return Поток ресурсов, содержащий список треков в виде ресурса.
     */
    fun searchTracks(request: String): Flow<Resource<List<Track>>>

    /**
     * Получает список треков из истории.
     *
     * @return Список треков.
     */
    fun getTracksHistory(): List<Track>

    /**
     * Добавляет указанный трек в историю.
     *
     * @param track Трек, который нужно добавить в историю.
     */
    fun addTrackToHistory(track: Track)

    /**
     * Очищает историю треков.
     */
    fun clearTrackHistory()

    /**
     * Сохраняет указанный трек для последующего воспроизведения.
     *
     * @param track Трек, который нужно сохранить для воспроизведения.
     */
    fun saveTrackForPlaying(track: Track)
}