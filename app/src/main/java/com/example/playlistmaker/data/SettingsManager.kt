package com.example.playlistmaker.data

import android.content.SharedPreferences

interface SettingsManager {
    fun getEditor() : SharedPreferences.Editor
    fun getString(name : String, defaultValue : String?) : String?
    fun putString(name: String, value: String?)
    fun removeString(name: String)
}


class SettingsManagerImpl(private val sharedPreferences: SharedPreferences) : SettingsManager {
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
