package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class MediaLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)

        val backButton = findViewById<ImageButton>(R.id.backButton)


        // Получите данные о треке из Intent
        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")
        val trackTimeMills = intent.getStringExtra("trackTimeMills")
        val coverImageURL = intent.getStringExtra("coverImageURL")

        // Найдите соответствующие TextView на макете активности
        val trackNameTextView = findViewById<TextView>(R.id.track_name)
        val artistNameTextView = findViewById<TextView>(R.id.artist_name)
        val collectionNameTextView = findViewById<TextView>(R.id.collection_name)
        val releaseDateTextView = findViewById<TextView>(R.id.release_date)
        val primaryGenreNameTextView = findViewById<TextView>(R.id.primary_genre_name)
        val countryTextView = findViewById<TextView>(R.id.country)
        val trackTimeMillsTextView = findViewById<TextView>(R.id.duration_data)

        // Заполните TextView данными о треке
        trackNameTextView.text = trackName
        artistNameTextView.text = artistName
        collectionNameTextView.text = collectionName
        releaseDateTextView.text = releaseDate
        primaryGenreNameTextView.text = primaryGenreName
        countryTextView.text = country
        trackTimeMillsTextView.text = trackTimeMills


        // Проверьте, что releaseDate не равен null и не пуст
        if (!releaseDate.isNullOrEmpty()) {
            // Извлеките только первые четыре символа (год) из строки releaseDate
            val year = releaseDate.substring(0, 4)

            // Найдите соответствующий TextView на макете активности для отображения года релиза
            val releaseYearTextView = findViewById<TextView>(R.id.release_date)

            // Установите отформатированный год в TextView
            releaseYearTextView.text = year
        } else {
            // Если releaseDate равен null или пуст, скройте TextView или установите текст по умолчанию
            val releaseYearTextView = findViewById<TextView>(R.id.release_date)
            releaseYearTextView.visibility = View.GONE // или установите текст по умолчанию
        }

        val coverImageView = findViewById<ImageView>(R.id.coverImageView)
// Получите URL обложки альбома из объекта Track (предположим, что это поле называется coverImageURL)
        val imageUrl = coverImageURL

        if (!imageUrl.isNullOrEmpty()) {
            // Опции для загрузки изображения
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.placeholder_track) // Заглушка, если изображение не загружено
                .error(R.drawable.placeholder_track) // Заглушка, если произошла ошибка загрузки

            // Загрузка изображения с использованием Glide
            Glide.with(this)
                .load(imageUrl)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade()) // Плавное переключение между изображениями
                .into(coverImageView)
        } else {
            // Если URL обложки отсутствует, вы можете установить заглушку или другое действие по умолчанию
            coverImageView.setImageResource(R.drawable.placeholder_track)
        }



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

            val trackNameTextView = findViewById<TextView>(R.id.track_name)
            val artistNameTextView = findViewById<TextView>(R.id.artist_name)
            val collectionNameTextView = findViewById<TextView>(R.id.collection_name)
            val releaseDateTextView = findViewById<TextView>(R.id.release_date)
            val primaryGenreNameTextView = findViewById<TextView>(R.id.primary_genre_name)
            val countryTextView = findViewById<TextView>(R.id.country)
            val trackTimeMillisTextView = findViewById<TextView>(R.id.duration)

            // Ваши данные о треке
            val trackName = "Название трека"
            val artistName = "Имя исполнителя"
            val collectionName = "Название альбома"
            val releaseDate = "Год релиза трека"
            val primaryGenreName = "Жанр трека"
            val country = "Страна исполнителя"
            val trackTimeMillis = "Продолжительность трека"

            // Заполните TextView данными
            trackNameTextView.text = trackName
            artistNameTextView.text = artistName
            collectionNameTextView.text = collectionName
            releaseDateTextView.text = releaseDate
            primaryGenreNameTextView.text = primaryGenreName
            countryTextView.text = country
            trackTimeMillisTextView.text = trackTimeMillis
        }
    }
}