package com.example.playlistmaker.domain.playlist.impl

import android.net.Uri
import com.example.playlistmaker.data.external_storage.ExternalStorageRepo
import com.example.playlistmaker.data.playlist.PlaylistRepo
import com.example.playlistmaker.data.search.TracksRepo
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import java.util.UUID

class PlaylistInteractorImpl(
    private val externalStorageRepo: ExternalStorageRepo,
    private val playlistRepo: PlaylistRepo,
    private val trackRepo: TracksRepo
) : PlaylistInteractor {

    override suspend fun addPlaylist(name: String, description: String?, coverUri: Uri?) {
        val id = UUID.randomUUID().toString()
        val playlistCoverUri = coverUri?.let {
            externalStorageRepo.savePlaylistCover(id, coverUri)
        }
        val playlist = Playlist(
            id = id,
            name = name,
            description = description,
            coverUri = playlistCoverUri
        )
        playlistRepo.addPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = playlistRepo.getPlaylistsFlow()

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistRepo.addTrackToPlaylist(playlist, track)
    }

    override fun getPlaylistById(playlistId: String): Flow<Playlist> {
        return playlistRepo.getPlaylistById(playlistId).filterNotNull()
    }

    override fun getPlaylistTracks(tracksIds: List<Long>): Flow<List<Track>> {
        return playlistRepo.getPlaylistTracks(tracksIds)
    }

    override fun saveTrackForPlaying(track: Track) = trackRepo.savePlayingTrack(track)

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, trackId: Long) {
        val newPlaylistTracksIds = playlist.tracksIds
            .toMutableList()
            .apply { remove(trackId) }
        val updatedPlaylist = playlist.copy(
            tracksIds = newPlaylistTracksIds,
            tracksCount = playlist.tracksCount - 1
        )
        playlistRepo.updatePlaylist(updatedPlaylist)
        checkOtherPlaylistsContainTrack(trackId)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepo.deletePlaylist(playlist)
        playlist.tracksIds.forEach { checkOtherPlaylistsContainTrack(it) }
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, coverUri: Uri?) {
        val playlistCoverUri = if (playlist.coverUri != coverUri.toString())
            coverUri?.let { externalStorageRepo.savePlaylistCover(playlist.id, coverUri)}
        else
            playlist.coverUri
        playlistRepo.updatePlaylist(playlist.copy(coverUri = playlistCoverUri))
    }

    private suspend fun checkOtherPlaylistsContainTrack(trackId: Long) {
        val playlistsContainTrack = playlistRepo.getPlaylists().filter {
            trackId in it.tracksIds.toSet()
        }
        if (playlistsContainTrack.isEmpty()) {
            trackRepo.deleteTrack(trackId)
        }
    }
}