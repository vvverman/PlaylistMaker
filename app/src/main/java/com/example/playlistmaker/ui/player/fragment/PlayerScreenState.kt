package com.example.playlistmaker.ui.player.fragment

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.model.PlayerState

data class PlayerScreenState(
    val playerState: PlayerState = PlayerState.PREPARED,
    val track: Track?=null,
    val trackTime: String = "",
    val isFavorites: Boolean = false
)
