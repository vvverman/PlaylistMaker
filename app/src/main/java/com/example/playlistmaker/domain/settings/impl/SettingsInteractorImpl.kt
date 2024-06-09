package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.data.settings.SettingsRepo
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeList


class SettingsInteractorImpl(private val settingsRepo: SettingsRepo) : SettingsInteractor {

    override fun getThemeSettings(): ThemeList = settingsRepo.getThemeSettings()

    override fun updateThemeSettings(themeList: ThemeList) {
        settingsRepo.updateThemeSettings(themeList)
    }
}