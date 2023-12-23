package com.example.playlistmaker.data

import android.media.MediaPlayer
import android.os.Handler
import android.widget.TextView

interface TrackService {
    fun formatTime(milliseconds: Int): String
    fun updateCurrentTime(mediaPlayer: MediaPlayer?, currentTimeTextView: TextView?)
}

class TrackServiceImpl : TrackService {

    private val handler = Handler()
    override fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun updateCurrentTime(mediaPlayer: MediaPlayer?, currentTimeTextView: TextView?) {
        if (mediaPlayer?.isPlaying == true) {
            val currentPosition = mediaPlayer.currentPosition ?: 0
            val currentTime = formatTime(currentPosition)
            currentTimeTextView?.text = currentTime
        }
        // Вызывать метод updateCurrentTime() каждую секунду
        handler.postDelayed({ updateCurrentTime(mediaPlayer, currentTimeTextView) }, 1000)
    }
}