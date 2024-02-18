package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.SettingsCreator

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        SettingsCreator.createInteractor(this)
    }
}