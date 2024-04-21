package com.example.playlistmaker.domain.share.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.data.share.ExternalBrowser
import com.example.playlistmaker.domain.share.SharingInteractor
import com.example.playlistmaker.ui.settings.AgreementActivity



class SharingInteractorImpl(private val context: Context,
                            private val externalBrowser: ExternalBrowser) : SharingInteractor {

    companion object{
        private const val SHARE_APP_URL = "https://practicum.yandex.ru/android-developer/"
        private const val EMAIL_ADDRESS = "vermosti@yandex.ru"
    }

    override fun shareApp() {
        externalBrowser.shareLink(SHARE_APP_URL)
    }

    override fun openUserAgreement() {
        val intent = Intent(context, AgreementActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    override fun openSupport() {
        val subject = "Сообщение разработчикам и разработчицам..."
        val message = "Спасибо разработчикам и разработчицам за отличное приложение!!!"
        externalBrowser.openEmail(EMAIL_ADDRESS, subject, message)
    }
}