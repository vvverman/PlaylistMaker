package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.TracksRepo
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Реализация интерфейса SearchInteractor, которая предоставляет функциональность поиска и работы с историей треков.
 *
 * @param tracksRepo Репозиторий для работы с историей треков в хранилище.
 */
class SearchInteractorImpl(private val tracksRepo: TracksRepo) : SearchInteractor {

    /**
     * Выполняет поиск треков, соответствующих указанному запросу.
     *
     * @param request Строка поискового запроса.
     * @return Поток ресурсов, содержащий список треков в виде ресурса.
     */
    override fun searchTracks(request: String): Flow<Resource<List<Track>>> {
        return tracksRepo.searchTracks(request)
    }

    /**
     * Получает список треков из истории.
     *
     * @return Список треков.
     */
    override fun getTracksHistory(): List<Track> = tracksRepo.getHistory()

    /**
     * Добавляет указанный трек в историю.
     *
     * @param track Трек, который нужно добавить в историю.
     */
    override fun addTrackToHistory(track: Track) = tracksRepo.addTrackToHistory(track)

    /**
     * Очищает историю треков.
     */
    override fun clearTrackHistory() = tracksRepo.clearHistory()

    /**
     * Сохраняет указанный трек для последующего воспроизведения.
     *
     * @param track Трек, который нужно сохранить для воспроизведения.
     */
    override fun saveTrackForPlaying(track: Track) = tracksRepo.savePlayingTrack(track)
}