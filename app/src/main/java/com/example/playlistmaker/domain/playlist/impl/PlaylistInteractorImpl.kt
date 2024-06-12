package com.example.playlistmaker.domain.playlist.impl

import android.net.Uri
import com.example.playlistmaker.data.external_storage.ExternalStorageRepo
import com.example.playlistmaker.data.playlist.PlaylistRepo
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class PlaylistInteractorImpl(
    private val externalStorageRepo: ExternalStorageRepo,
    private val playlistRepo: PlaylistRepo
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

    override fun getPlaylists(): Flow<List<Playlist>> = playlistRepo.getPlaylists()

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {
        playlistRepo.updatePlaylist(playlist, track)
    }
}