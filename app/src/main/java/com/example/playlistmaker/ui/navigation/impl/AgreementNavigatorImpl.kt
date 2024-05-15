package com.example.playlistmaker.ui.navigation.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.ui.navigation.AgreementNavigator
import com.example.playlistmaker.ui.settings.AgreementActivity

class AgreementNavigatorImpl(private val context: Context) : AgreementNavigator {

    override fun openUserAgreementScreen() {
        val intent = Intent(context, AgreementActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}