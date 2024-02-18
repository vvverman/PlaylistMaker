package com.example.playlistmaker.data.search.model

import com.example.playlistmaker.data.network.Result


data class TracksResult(
    val resultCount: Int,
    val results: List<TrackDto>
): Result()