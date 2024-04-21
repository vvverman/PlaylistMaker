package com.example.playlistmaker.di

import com.example.playlistmaker.data.share.impl.ExternalServiceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val navigationModule = module {

    single<ExternalServiceImpl> {
        ExternalServiceImpl(context = androidContext())
    }
}