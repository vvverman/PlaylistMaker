package com.example.playlistmaker.data.share

interface ExternalService {
    fun shareLink(url: String)
    fun openUserAgreement(url: String)
    fun openEmail(email: String, subject: String, message: String)
}