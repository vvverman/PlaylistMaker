package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SettingsRepo
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeList


class SettingsInteractorImpl(
    private val settingsRepo: SettingsRepo
) : SettingsInteractor {

    override fun getThemeSettings(): ThemeList = settingsRepo.getThemeSettings()

    override fun updateThemeSettings(ThemeList: ThemeList) {
        settingsRepo.updateThemeSettings(ThemeList)
    }
}