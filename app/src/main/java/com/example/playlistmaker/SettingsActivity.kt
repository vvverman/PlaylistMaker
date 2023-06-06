package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<LinearLayout>(R.id.shareButton)

        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                val shareButtonText = getResources().getString(R.string.shareButtonText)
                putExtra(Intent.EXTRA_TEXT, shareButtonText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val supportButton = findViewById<LinearLayout>(R.id.supportButton)

        supportButton.setOnClickListener {
//            val message = "«Спасибо разработчикам и разработчицам за крутое приложение!»"
//            val title = "«Сообщение разработчикам и разработчицам приложения Playlist Maker»"
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            val supportButtonMessage = getResources().getString(R.string.supportButtonMessage)
            val supportButtonText = getResources().getString(R.string.supportButtonText)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("vermosti@ya.ru"))
            shareIntent.putExtra(Intent.EXTRA_TEXT, supportButtonMessage)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, supportButtonText)
            startActivity(shareIntent)
        }

        val termsOfUseButton = findViewById<LinearLayout>(R.id.termsOfUseButton)

        termsOfUseButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_VIEW)
            val termsOfUseArticle = getResources().getString(R.string.termsOfUseArticle)
            shareIntent.data = Uri.parse(termsOfUseArticle)
            startActivity(shareIntent)
        }
    }


}




