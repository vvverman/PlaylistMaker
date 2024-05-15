package com.example.playlistmaker.domain.share.impl

import com.example.playlistmaker.data.share.impl.ExternalServiceImpl
import com.example.playlistmaker.domain.share.SharingInteractor
import com.example.playlistmaker.ui.navigation.AgreementNavigator


class SharingInteractorImpl(private val externalServiceImpl: ExternalServiceImpl,
                            private val agreementNavigator: AgreementNavigator
) : SharingInteractor {

    override fun shareApp() {
        externalServiceImpl.shareLink(SharingConstants.SHARE_APP_URL)
    }

    override fun openUserAgreement() {
        agreementNavigator.openUserAgreementScreen()
    }

    override fun openSupport() {
        val subject = SharingConstants.SUBJECT
        val message = SharingConstants.MESSAGE
        externalServiceImpl.openEmail(SharingConstants.EMAIL_ADDRESS, subject, message)
    }
}



