package com.example.playlistmaker.ui.player.impl

import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import android.widget.TextView
import com.example.playlistmaker.ui.player.TrackService
import java.lang.IllegalStateException

// Класс TrackServiceImpl реализует интерфейс TrackService и предоставляет конкретную реализацию его методов
class TrackServiceImpl : TrackService {

    private val handler = Handler()

    // Форматирует время в миллисекундах в строку в формате "минуты:секунды"
    override fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    // Обновляет текущее время проигрывания трека и отображает его на TextView
    override fun updateCurrentTime(mediaPlayer: MediaPlayer?, currentTimeTextView: TextView?) {
        try {
            if (mediaPlayer?.isPlaying == true) {
                val currentPosition = mediaPlayer.currentPosition ?: 0
                val currentTime = formatTime(currentPosition)
                currentTimeTextView?.text = currentTime
                // Вызывает метод updateCurrentTime() каждую секунду, используя Handler
                handler.postDelayed({ updateCurrentTime(mediaPlayer, currentTimeTextView) }, 1000)
            }
        } catch (e: IllegalStateException) {
            Log.e("TrackService", "updateCurrentTime: MediaPlayer is not initialized", e)
        }
    }
}