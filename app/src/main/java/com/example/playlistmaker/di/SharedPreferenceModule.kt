package com.example.playlistmaker.di

import android.app.Application
import android.content.SharedPreferences
import com.example.playlistmaker.data.utils.SharedPreferenceConverterImpl
import com.example.playlistmaker.domain.utils.SharedPreferenceConverter
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val KEY_SHARED_PREFERENCES = "shared_preferences"

val sharedPreferenceModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(KEY_SHARED_PREFERENCES, Application.MODE_PRIVATE)
    }

    single<SharedPreferenceConverter> {
        SharedPreferenceConverterImpl(gson = get())
    }

    factory<Gson> { Gson() }
}