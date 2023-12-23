package com.example.playlistmaker.data

import android.media.MediaPlayer
import android.os.Handler
import android.widget.TextView

// Интерфейс TrackService определяет методы, которые должны быть реализованы классом, предоставляющим сервисы треков
interface TrackService {
    // Форматирует время в миллисекундах в строку в формате "минуты:секунды"
    fun formatTime(milliseconds: Int): String

    // Обновляет текущее время проигрывания трека и отображает его на TextView
    fun updateCurrentTime(mediaPlayer: MediaPlayer?, currentTimeTextView: TextView?)
}

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
        if (mediaPlayer?.isPlaying == true) {
            val currentPosition = mediaPlayer.currentPosition ?: 0
            val currentTime = formatTime(currentPosition)
            currentTimeTextView?.text = currentTime
        }
        // Вызывает метод updateCurrentTime() каждую секунду, используя Handler
        handler.postDelayed({ updateCurrentTime(mediaPlayer, currentTimeTextView) }, 1000)
    }
}