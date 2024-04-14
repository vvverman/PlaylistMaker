package com.example.playlistmaker.domain.settings


import com.example.playlistmaker.domain.settings.model.ThemeList


interface SettingsRepo {
    fun getThemeSettings(): ThemeList
    fun updateThemeSettings(settings: ThemeList)
}