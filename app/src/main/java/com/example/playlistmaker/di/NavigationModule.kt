package com.example.playlistmaker.di

import com.example.playlistmaker.data.share.ExternalBrowser

import com.example.playlistmaker.data.share.impl.ExternalBrowserImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val navigationModule = module {

    single<ExternalBrowser> {
        ExternalBrowserImpl(context = androidContext())
    }
}