package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.domain.settings.SettingsRepo
import com.example.playlistmaker.data.settings.impl.SettingsRepoImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl

object SettingsCreator {

    private var settingsInteractor: SettingsInteractorImpl? = null
    private var settingsRepo: SettingsRepo? = null

    fun createInteractor(context: Context): SettingsInteractor {
        val settingsRepository = createSettingsRepository(context)
        return settingsInteractor ?: SettingsInteractorImpl(settingsRepository).apply {
            settingsInteractor = this
        }
    }

    private fun createSettingsRepository(context: Context): SettingsRepo {
        val sharedPreferences = SharedPreferencesCreator.createSharedPreferences(context)
        return settingsRepo ?: SettingsRepoImpl(sharedPreferences).apply {
            settingsRepo = this
        }
    }
}