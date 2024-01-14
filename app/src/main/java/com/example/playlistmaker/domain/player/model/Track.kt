package com.example.playlistmaker.domain.player.model

import com.google.gson.annotations.SerializedName

// Класс Track представляет модель данных для трека
data class Track(
    @SerializedName("ItemId") val itemId: Long, // Уникальный идентификатор трека
    @SerializedName("trackName") val compositionName: String, // Название композиции
    @SerializedName("artistName") val artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") val durationInMillis: Long, // Продолжительность трека в миллисекундах
    @SerializedName("artworkUrl100") val coverImageURL: String?, // URL обложки трека
    @SerializedName("collectionName") val albumName: String?, // Название альбома (может быть null)
    @SerializedName("releaseDate") val releaseDate: String?, // Год релиза трека (может быть null)
    @SerializedName("primaryGenreName") val genre: String?, // Жанр трека (может быть null)
    @SerializedName("country") val country: String?, // Страна исполнителя (может быть null)
    @SerializedName("previewUrl") val previewUrl: String? // Ссылка на отрывок трека (может быть null)
)

