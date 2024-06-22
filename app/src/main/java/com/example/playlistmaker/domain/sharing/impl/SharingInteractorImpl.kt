package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.share.impl.ExternalServiceImpl
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.sharing.SharingInteractor
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

    override fun sharePlaylist(name: String, description: String, tracks: List<Track>) {
        externalServiceImpl.sharePlaylist(name, description, tracks)
    }
}



