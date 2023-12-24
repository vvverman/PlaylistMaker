package com.example.playlistmaker.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R

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
        val medialibraryButton = findViewById<Button>(R.id.medialibrary)

        // Устанавливаем слушатель клика на кнопку "Медиатека"
        medialibraryButton.setOnClickListener {
            // Создаем намерение (Intent) для перехода на активность MediaLibraryActivity
            val intent = Intent(this, MediaLibraryActivity::class.java)
            // Запускаем активность MediaLibraryActivity
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