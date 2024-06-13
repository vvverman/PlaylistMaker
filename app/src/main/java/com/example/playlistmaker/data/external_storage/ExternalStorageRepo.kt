package com.example.playlistmaker.data.external_storage

import android.net.Uri

interface ExternalStorageRepo {
    suspend fun savePlaylistCover(playlistId: String, uri: Uri): String
}