package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.SettingsRepo
import com.example.playlistmaker.domain.settings.model.ThemeList


class ThemeManager(
    private val sharedPreferences: SharedPreferences
) : SettingsRepo {

    companion object {
        private const val KEY_APPLICATION_THEME = "key_value"
    }

    init {
        updateThemeSettings(getThemeSettings())
    }

    override fun getThemeSettings(): ThemeList {
        val themeName = sharedPreferences.getString(
            KEY_APPLICATION_THEME, ThemeList.LIGHT.name
        ) ?: ThemeList.LIGHT.name
        return ThemeList.valueOf(themeName)
    }

    override fun updateThemeSettings(themeList: ThemeList) {
        val nightMode = when (themeList) {
            ThemeList.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeList.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        saveThemeSettings(themeList)
    }

    private fun saveThemeSettings(applicationTheme: ThemeList) {
        with(sharedPreferences.edit()) {
            putString(KEY_APPLICATION_THEME, applicationTheme.name)
            apply()
        }
    }
}