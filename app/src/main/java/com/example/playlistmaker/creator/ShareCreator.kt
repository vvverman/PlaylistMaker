package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.share.ExternalBrowser
import com.example.playlistmaker.data.share.impl.ExternalBrowserImpl
import com.example.playlistmaker.domain.share.SharingInteractor
import com.example.playlistmaker.domain.share.impl.SharingInteractorImpl

object ShareCreator {
    private var sharingInteractor: SharingInteractor? = null
    private var externalBrowser: ExternalBrowser? = null

    fun createInteractor(context: Context): SharingInteractor {
        val externalNavigator = this.externalBrowser ?: createExternalNavigator(context)
        return sharingInteractor ?: SharingInteractorImpl(externalNavigator).apply {
            sharingInteractor = this
        }
    }

    private fun createExternalNavigator(context: Context): ExternalBrowser {
        return externalBrowser ?: ExternalBrowserImpl(context).apply {
            externalBrowser = this
        }
    }
}