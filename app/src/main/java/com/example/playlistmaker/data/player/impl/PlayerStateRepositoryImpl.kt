package com.example.playlistmaker.data.player.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.player.PlayerStateRepository

class PlayerStateRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    PlayerStateRepository {

    private val AUDIO_PLAYER_STATE = "AudioPlayerState"
    override fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    override fun getString(name: String, defaultValue: String?): String? {

        return sharedPreferences.getString(name, defaultValue)
    }

    override fun putString(name: String, value: String?) {
        sharedPreferences.edit().putString(name, value).apply()
    }

    override fun removeString(name: String) {
        sharedPreferences.edit().remove(name).apply()
    }
}