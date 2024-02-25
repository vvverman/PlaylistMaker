package com.example.playlistmaker.data.share

interface ExternalBrowser {
    fun shareLink(url: String)
    fun openUserAgreement(url: String)
    fun openEmail(email: String)
}