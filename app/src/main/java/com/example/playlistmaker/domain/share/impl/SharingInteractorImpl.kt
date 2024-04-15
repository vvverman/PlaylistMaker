package com.example.playlistmaker.domain.share.impl

import com.example.playlistmaker.data.share.ExternalBrowser
import com.example.playlistmaker.domain.share.SharingInteractor

class SharingInteractorImpl(private val externalBrowser: ExternalBrowser) : SharingInteractor {

    companion object{
        private const val SHARE_APP_URL = "https://practicum.yandex.ru/android-developer/"
        private const val USER_AGREEMENT_URL = "https://yandex.ru/legal/practicum_offer/"
        private const val EMAIL_ADDRESS = "vermosti@yandex.ru"
    }

    override fun shareApp() = externalBrowser.shareLink(SHARE_APP_URL)

    override fun openUserAgreement() = externalBrowser.openUserAgreement(USER_AGREEMENT_URL)

    override fun openSupport() {
        val subject = "Сообщение разработчикам и разработчицам..."
        val message = "Спасибо разработчикам и разработчицам за отличное приложение!!!"
        externalBrowser.openEmail(EMAIL_ADDRESS, subject, message)
    }
}