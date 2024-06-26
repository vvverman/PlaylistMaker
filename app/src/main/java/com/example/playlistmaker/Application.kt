package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.navigationModule
import com.example.playlistmaker.di.networkModule
import com.example.playlistmaker.di.repoModule
import com.example.playlistmaker.di.sharedPreferenceModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@Application)
            modules(
                interactorModule,
                navigationModule,
                networkModule,
                repoModule,
                sharedPreferenceModule,
                viewModelModule,
                databaseModule
            )
        }
    }
}