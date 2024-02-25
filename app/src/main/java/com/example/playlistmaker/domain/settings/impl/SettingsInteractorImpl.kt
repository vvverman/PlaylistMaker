package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SettingsRepo
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings


class SettingsInteractorImpl(
    private val settingsRepo: SettingsRepo
) : SettingsInteractor {

    override fun getThemeSettings(): ThemeSettings = settingsRepo.getThemeSettings()

    override fun updateThemeSettings(ThemeSettings: ThemeSettings) {
        settingsRepo.updateThemeSettings(ThemeSettings)
    }
}