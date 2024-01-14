package com.example.playlistmaker.domain.player.impl

import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import android.widget.TextView
import java.lang.IllegalStateException

// Интерфейс TrackService определяет методы, которые должны быть реализованы классом, предоставляющим сервисы треков
interface TrackService {
    // Форматирует время в миллисекундах в строку в формате "минуты:секунды"
    fun formatTime(milliseconds: Int): String

    // Обновляет текущее время проигрывания трека и отображает его на TextView
    fun updateCurrentTime(mediaPlayer: MediaPlayer?, currentTimeTextView: TextView?)
}

