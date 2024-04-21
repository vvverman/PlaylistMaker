package com.example.playlistmaker.data.share.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.share.ExternalService

class ExternalServiceImpl(private val context: Context) : ExternalService {
    override fun shareLink(url: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = context.getString(R.string.type_share_text)
        }
        val shareIntent = Intent
            .createChooser(sendIntent, null)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openEmail(email: String, subject: String, message: String) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(context.getString(R.string.url_mail))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
    }

}}