package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.ThemeSettings
import com.example.playlistmaker.domain.settings.model.ThemeSettings.*
import com.example.playlistmaker.domain.share.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _applicationTheme =
        MutableLiveData<com.example.playlistmaker.domain.settings.model.ThemeSettings>(settingsInteractor.getThemeSettings())
    val ThemeSettings: LiveData<Boolean> = _applicationTheme.map { it == DARK }

    fun onShareAppButtonClicked() = sharingInteractor.shareApp()

    fun onWriteSupportButtonClicked() = sharingInteractor.openSupport()

    fun onUserAgreementsButtonClicked() = sharingInteractor.openUserAgreement()

    fun onThemeSwitchClicked(isChecked: Boolean) {
        val applicationTheme = if (isChecked) DARK else LIGHT
        settingsInteractor.updateThemeSettings(applicationTheme)
    }
}