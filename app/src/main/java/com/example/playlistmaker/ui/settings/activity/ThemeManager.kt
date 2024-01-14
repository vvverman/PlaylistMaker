package com.example.playlistmaker.ui.settings.activity

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings


class ThemeManager() : Application() {

    val settingsRepository = SettingsRepository()
    val themeManager = ThemeManager(settingsRepository)

    constructor() : this(settingsRepository) {
        // Пустой конструктор без аргументов
    }


    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val DARK_THEME_ENABLED = "dark_theme_enabled"
    }

    override fun onCreate() {
        super.onCreate()
        loadThemeFromSharedPreferences()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        saveThemeToSharedPreferences(darkThemeEnabled)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun loadThemeFromSharedPreferences() {
        val themeSettings: ThemeSettings = settingsRepository.getThemeSettings()
        switchTheme(themeSettings.isDarkThemeEnabled)
    }

    private fun saveThemeToSharedPreferences(darkThemeEnabled: Boolean) {
        settingsRepository.updateThemeSetting(ThemeSettings(darkThemeEnabled))
    }
}




