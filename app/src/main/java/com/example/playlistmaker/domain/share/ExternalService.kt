package com.example.playlistmaker.domain.share

interface ExternalService {
    fun shareLink(url: String)
    fun openEmail(email: String, subject: String, message: String)
}