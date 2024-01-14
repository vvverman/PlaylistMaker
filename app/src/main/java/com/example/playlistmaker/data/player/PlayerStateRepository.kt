package com.example.playlistmaker.data.player

import android.content.SharedPreferences

interface PlayerStateRepository {
    fun getEditor() : SharedPreferences.Editor
    fun getString(name : String, defaultValue : String?) : String?
    fun putString(name: String, value: String?)
    fun removeString(name: String)
}

