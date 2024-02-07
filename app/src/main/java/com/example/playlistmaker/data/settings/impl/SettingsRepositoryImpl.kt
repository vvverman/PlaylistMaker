package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences?) : SettingsRepository {

    private val PREFS_NAME = "app_settings"
    private val gson = Gson()
    override fun getThemeSettings(): ThemeSettings {

        val appSettingsString = sharedPreferences?.getString(PREFS_NAME, "")
        return if (appSettingsString.isNullOrBlank()) {
            ThemeSettings(
                isDarkThemeEnabled = false
            )
        } else {
            gson.fromJson(appSettingsString, object : TypeToken<ThemeSettings>() {}.type)
        }
    }

    override fun updateThemeSetting(settings: ThemeSettings) {

        val appSettingsString = gson.toJson(settings)

        sharedPreferences?.edit()?.putString(PREFS_NAME, appSettingsString)?.apply()
    }
}


