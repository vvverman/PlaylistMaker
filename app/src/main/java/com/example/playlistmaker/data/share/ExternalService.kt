package com.example.playlistmaker.data.share

import com.example.playlistmaker.domain.model.Track

interface ExternalService {
    fun shareLink(url: String)
    fun openUserAgreement(url: String)
    fun openEmail(email: String, subject: String, message: String)
    fun sharePlaylist(name: String, description: String, tracks: List<Track>)

}