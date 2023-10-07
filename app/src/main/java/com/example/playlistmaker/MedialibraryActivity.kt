package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MedialibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)

        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Установите обработчик клика на кнопке "назад"
        backButton.setOnClickListener {
            // Вернуться на предыдущий экран
            onBackPressed()

        }
    }

    // Объявите переменную currentTrackId
    private var currentTrackId: String? = null

    // Метод вызывается, когда активность уходит в фоновый режим
    override fun onPause() {
        super.onPause()

        // Сохраните состояние экрана "Аудиоплеер" в SharedPreferences
        val sharedPreferences = getSharedPreferences("AudioPlayerState", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        // Сохраните данные о текущем треке или другие параметры
        editor.putString("currentTrackId", currentTrackId)
        editor.apply()
    }

    // Метод вызывается при восстановлении активности из фонового режима
    override fun onResume() {
        super.onResume()

        // Восстановите состояние экрана "Аудиоплеер" из SharedPreferences
        val sharedPreferences = getSharedPreferences("AudioPlayerState", Context.MODE_PRIVATE)
        val currentTrackId = sharedPreferences.getString("currentTrackId", null)

        // Проверьте, что текущий трек не равен null, и отобразите экран "Аудиоплеер" соответствующим образом
        if (currentTrackId != null) {
            // Здесь выполните действия для отображения экрана "Аудиоплеер"
            // Например, загрузите данные о треке и отобразите их
        }
    }
}