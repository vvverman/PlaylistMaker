package com.example.playlistmaker.data.settings


import com.example.playlistmaker.domain.settings.model.ThemeList


interface SettingsRepo {
    fun getThemeSettings(): ThemeList
    fun updateThemeSettings(settings: ThemeList)
}