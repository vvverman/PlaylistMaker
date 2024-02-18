package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.playlistmaker.creator.SettingsCreator
import com.example.playlistmaker.creator.ShareCreator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.share.SharingInteractor

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sharingInteractor: SharingInteractor = ShareCreator.createInteractor(application)
    private val settingsInteractor: SettingsInteractor =
        SettingsCreator.createInteractor(application)

    private val _ThemeSettings =
        MutableLiveData<ThemeSettings>(settingsInteractor.getThemeSettings())
    val ThemeSettings: LiveData<Boolean> = _ThemeSettings.map { it == com.example.playlistmaker.domain.settings.model.ThemeSettings.DARK }

    fun onShareAppButtonClicked() = sharingInteractor.shareApp()

    fun onWriteSupportButtonClicked() = sharingInteractor.openSupport()

    fun onUserAgreementsButtonClicked() = sharingInteractor.openUserAgreement()

    fun onThemeSwitchClicked(isChecked: Boolean) {
        val ThemeSettings = if (isChecked) com.example.playlistmaker.domain.settings.model.ThemeSettings.DARK else com.example.playlistmaker.domain.settings.model.ThemeSettings.LIGHT
        settingsInteractor.updateThemeSettings(ThemeSettings)
    }
}