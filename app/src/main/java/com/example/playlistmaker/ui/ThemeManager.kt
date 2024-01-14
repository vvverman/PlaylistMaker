package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager : Application() {
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
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val darkThemeEnabled = sharedPreferences.getBoolean(DARK_THEME_ENABLED, false)
        switchTheme(darkThemeEnabled)
    }

    private fun saveThemeToSharedPreferences(darkThemeEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(DARK_THEME_ENABLED, darkThemeEnabled)
        editor.apply()
    }
}