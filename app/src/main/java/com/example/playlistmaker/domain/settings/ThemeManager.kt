package com.example.playlistmaker.domain.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.settings.impl.SettingsRepoImpl
import com.example.playlistmaker.domain.settings.model.ThemeSettings



class ThemeManager() : Application() {

    private var settingsRepo: SettingsRepo? = null
    private var sharedPreferences: SharedPreferences? = null


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
        settingsRepo = SettingsRepoImpl(sharedPreferences)
        val themeSettings: ThemeSettings = (settingsRepo as SettingsRepoImpl).getThemeSettings()
        switchTheme(themeSettings == ThemeSettings.DARK)
    }

    private fun saveThemeToSharedPreferences(darkThemeEnabled: Boolean) {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        settingsRepo = SettingsRepoImpl(sharedPreferences)
        val themeSettings = if (darkThemeEnabled) ThemeSettings.DARK else ThemeSettings.LIGHT
        (settingsRepo as SettingsRepoImpl).updateThemeSettings(themeSettings)
    }
}




