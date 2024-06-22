package com.example.playlistmaker.data.external_storage.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.data.external_storage.ExternalStorageRepo
import java.io.File
import java.io.FileOutputStream

class ExternalStorageRepoImpl(private val context: Context) : ExternalStorageRepo {

    companion object {
        const val DIRECTORY_NAME = "playlist_maker"
        const val FILE_NAME = "playlist_cover_"
        const val FILE_EXTENSION = ".jpg"
        const val COMPRESS_QUALITY = 30
    }

    override suspend fun savePlaylistCover(playlistId: String, uri: Uri): String {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_NAME)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$FILE_NAME{playlistId}_${System.currentTimeMillis()}$FILE_EXTENSION")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, outputStream)
        inputStream?.close()
        outputStream.close()
        return file.toString()
    }
}