package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.domain.settings.SettingsRepo
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SettingsRepoImpl(private val sharedPreferences: SharedPreferences?) : SettingsRepo {

    private val PREFS_NAME = "app_settings"
    private val gson = Gson()

    override fun getThemeSettings(): ThemeSettings {
        val appSettingsString = sharedPreferences?.getString(PREFS_NAME, "")
        return if (appSettingsString.isNullOrBlank()) {
            ThemeSettings.LIGHT
        } else {
            try {
                gson.fromJson(appSettingsString, object : TypeToken<ThemeSettings>() {}.type)
            } catch (e: Exception) {
                Log.e("SETTINGS", "Error reading shared preferences, read error", e);
                val themeLight = ThemeSettings.LIGHT
                updateThemeSettings(themeLight)
                themeLight
            }

        }
    }

    override fun updateThemeSettings(settings: ThemeSettings) {
        val appSettingsString = gson.toJson(settings)
        sharedPreferences?.edit()?.putString(PREFS_NAME, appSettingsString)?.apply()
    }
}