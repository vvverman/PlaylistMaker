package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeList.*
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _applicationTheme =
        MutableLiveData(settingsInteractor.getThemeSettings())
    val themeSettings: LiveData<Boolean> = _applicationTheme.map { it == DARK }

    fun onShareAppButtonClicked() = sharingInteractor.shareApp()

    fun onWriteSupportButtonClicked() = sharingInteractor.openSupport()

    fun onUserAgreementsButtonClicked() = sharingInteractor.openUserAgreement()

    fun onThemeSwitchClicked(isChecked: Boolean) {
        val applicationTheme = if (isChecked) DARK else LIGHT
        settingsInteractor.updateThemeSettings(applicationTheme)
        _applicationTheme.value = settingsInteractor.getThemeSettings()
    }
}