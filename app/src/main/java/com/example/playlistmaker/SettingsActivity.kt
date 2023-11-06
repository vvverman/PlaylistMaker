package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

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
                val shareButtonText = resources.getString(R.string.shareButtonText)
                putExtra(Intent.EXTRA_TEXT, shareButtonText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val supportButton = findViewById<LinearLayout>(R.id.supportButton)

        supportButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            val supportButtonMessage = resources.getString(R.string.supportButtonMessage)
            val supportButtonText = resources.getString(R.string.supportButtonText)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("vermosti@ya.ru"))
            shareIntent.putExtra(Intent.EXTRA_TEXT, supportButtonMessage)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, supportButtonText)
            startActivity(shareIntent)
        }

        val termsOfUseButton = findViewById<LinearLayout>(R.id.termsOfUseButton)

        termsOfUseButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_VIEW)
            val termsOfUseArticle = resources.getString(R.string.termsOfUseArticle)
            shareIntent.data = Uri.parse(termsOfUseArticle)
            startActivity(shareIntent)
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

    }
}

class App : Application() {
    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val DARK_THEME_ENABLED = "dark_theme_enabled"
    }

    override fun onCreate() {
        super.onCreate()
        loadThemeFromSharedPreferences()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        saveThemeToSharedPreferences(darkThemeEnabled)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun loadThemeFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val darkThemeEnabled = sharedPreferences.getBoolean(DARK_THEME_ENABLED, false)
        switchTheme(darkThemeEnabled)
    }

    private fun saveThemeToSharedPreferences(darkThemeEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(DARK_THEME_ENABLED, darkThemeEnabled)
        editor.apply()
    }
}