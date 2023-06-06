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
            finish()
        }

        val supportButton = findViewById<LinearLayout>(R.id.supportButton)

        supportButton.setOnClickListener {
            val message = "«Спасибо разработчикам и разработчицам за крутое приложение!»"
            val title = "«Сообщение разработчикам и разработчицам приложения Playlist Maker»"
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("vermosti@ya.ru"))
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            startActivity(shareIntent)
        }

        val termsOfUseButton = findViewById<LinearLayout>(R.id.termsOfUseButton)

        termsOfUseButton.setOnClickListener {
            finish()
        }
    }


}




