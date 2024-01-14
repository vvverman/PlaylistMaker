package com.example.playlistmaker.ui.settings.activity

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.settings.model.ThemeSettings



class ThemeManager() : Application() {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        settingsRepository = SettingsRepositoryImpl(sharedPreferences)
        val themeSettings: ThemeSettings = settingsRepository.getThemeSettings()
        switchTheme(themeSettings.isDarkThemeEnabled)
    }

    private fun saveThemeToSharedPreferences(darkThemeEnabled: Boolean) {
        sharedPreferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        settingsRepository = SettingsRepositoryImpl(sharedPreferences)
        settingsRepository.updateThemeSetting(ThemeSettings(darkThemeEnabled))
    }
}




