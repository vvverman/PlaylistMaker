package com.example.playlistmaker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.medialibrary.MediaLibraryActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity

// Класс MainActivity является главной активностью приложения
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим кнопку по ее идентификатору
        val searchButton = findViewById<Button>(R.id.editTextSearch)

        // Устанавливаем слушатель клика на кнопку поиска
        searchButton.setOnClickListener {
            // Создаем намерение (Intent) для перехода на активность SearchActivity
            val intent = Intent(this, SearchActivity::class.java)
            // Запускаем активность SearchActivity
            startActivity(intent)
        }

        // Находим кнопку "Медиатека" по ее идентификатору
        val PlayerButton = findViewById<Button>(R.id.Player)

        // Устанавливаем слушатель клика на кнопку "Медиатека"
        PlayerButton.setOnClickListener {
            // Создаем намерение (Intent) для перехода на активность PlayerActivity
            val intent = Intent(this, MediaLibraryActivity::class.java)
            // Запускаем активность PlayerActivity
            startActivity(intent)
        }

        // Находим кнопку "Настройки" по ее идентификатору
        val settingsButton = findViewById<Button>(R.id.settings)

        // Устанавливаем слушатель клика на кнопку "Настройки"
        settingsButton.setOnClickListener {
            // Создаем намерение (Intent) для перехода на активность SettingsActivity
            val intent = Intent(this, SettingsActivity::class.java)
            // Запускаем активность SettingsActivity
            startActivity(intent)
        }
    }
}