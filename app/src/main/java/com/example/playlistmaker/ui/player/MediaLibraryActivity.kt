package com.example.playlistmaker.ui.player

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.PlayerStateRepository
import com.example.playlistmaker.data.player.impl.PlayerStateRepositoryImpl
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.ui.player.impl.TrackServiceImpl
import com.example.playlistmaker.domain.player.model.Track
import com.example.playlistmaker.ui.player.view_model.MediaLibraryViewModel

class MediaLibraryActivity : ComponentActivity() {

    private val viewModel by viewModels<MediaLibraryViewModel> { MediaLibraryViewModel.getViewModelFactory("123") }
    // 1
    private lateinit var binding: ActivityMediaLibraryBinding
}

    private lateinit var viewModel: MediaLibraryViewModel

    val trackService: TrackService = TrackServiceImpl()
    private lateinit var playerStateRepository: PlayerStateRepository
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    private var currentTimeTextView: TextView? = null
    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {

        val track = Track(
            compositionName = intent.getStringExtra("trackName") ?: "",
            artistName = intent.getStringExtra("artistName") ?: "",
            albumName = intent.getStringExtra("collectionName"),
            releaseDate = intent.getStringExtra("releaseDate"),
            genre = intent.getStringExtra("primaryGenreName"),
            country = intent.getStringExtra("country"),
            durationInMillis = intent.getStringExtra("trackTimeMills")?.toLongOrNull() ?: 0,
            coverImageURL = intent.getStringExtra("coverImageURL"),
            itemId = intent.getStringExtra("itemId")?.toLongOrNull() ?: 0,
            previewUrl = intent.getStringExtra("previewUrl")
        )

        super.onCreate(savedInstanceState)

        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MediaLibraryViewModel.getViewModelFactory(track))[MediaLibraryViewModel::class.java]

        viewModel.getLoadingLiveData().observe(this) { isLoading ->
            changeProgressBarVisibility(isLoading)


        setContentView(R.layout.activity_media_library)

        val backButton = findViewById<ImageButton>(R.id.backButton)

        currentTimeTextView = findViewById(R.id.current_time)



        // Найдите соответствующие TextView на макете активности
        val trackNameTextView = findViewById<TextView>(R.id.track_name)
        val artistNameTextView = findViewById<TextView>(R.id.artist_name)
        val collectionNameTextView = findViewById<TextView>(R.id.collection_name)
        val releaseDateTextView = findViewById<TextView>(R.id.release_date)
        val primaryGenreNameTextView = findViewById<TextView>(R.id.primary_genre_name)
        val countryTextView = findViewById<TextView>(R.id.country)
        val trackTimeMillsTextView = findViewById<TextView>(R.id.duration_data)


        // Заполните TextView данными о треке
        trackNameTextView.text = track.compositionName
        artistNameTextView.text = track.artistName
        collectionNameTextView.text = track.albumName
        releaseDateTextView.text = track.releaseDate
        primaryGenreNameTextView.text = track.genre
        countryTextView.text = track.country
        trackTimeMillsTextView.text = track.durationInMillis.toString()


        // Проверьте, что releaseDate не равен null и не пуст
        if (!track.releaseDate.isNullOrEmpty()) {
            // Извлеките только первые четыре символа (год) из строки releaseDate
            val year = track.releaseDate.substring(0, 4)

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
        val imageUrl = track.coverImageURL

        if (!imageUrl.isNullOrEmpty()) {
            // Опции для загрузки изображения
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.placeholder_track) // Заглушка, если изображение не загружено
                .transform(RoundedCorners(8))
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


        val previewUrl = intent.getStringExtra("previewUrl")

        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)

        if (!previewUrl.isNullOrEmpty()) {
            mediaPlayer = MediaPlayer().apply {

                setDataSource(previewUrl)
                prepareAsync()

                setOnPreparedListener {
                    mediaPlayer?.start()
                    trackService.updateCurrentTime(mediaPlayer, currentTimeTextView)


                }
                setOnCompletionListener {
                    mediaPlayer?.pause()
                    trackService.updateCurrentTime(mediaPlayer, currentTimeTextView)
                    // Установите текущее время в 00:00, когда воспроизведение завершается
                    currentTimeTextView?.text = "00:00"
                    // Измените видимость кнопок паузы и воспроизведения
                    pauseButton.visibility = View.GONE
                    playButton.visibility = View.VISIBLE

                }
            }
        }


        if (mediaPlayer?.isPlaying == true) {
            // Если трек уже воспроизводится, скройте кнопку "play" и покажите кнопку "pause"
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
        } else {
            // Если трек не воспроизводится, скройте кнопку "pause" и покажите кнопку "play"
            playButton.visibility = View.GONE
            pauseButton.visibility = View.VISIBLE
        }


        // Установите слушатель клика для кнопки play
        playButton.setOnClickListener {
            // Проверьте, воспроизводится ли трек в данный момент

            // Если трек уже воспроизводится, поставьте его на паузу
            mediaPlayer?.start()
            // Кнопка Pause появляется, кнопка Play исчезает
            pauseButton.visibility = View.VISIBLE
            playButton.visibility = View.GONE

        }

        pauseButton.setOnClickListener {
            // Проверьте, воспроизводится ли трек в данный момент

            // Если трек уже воспроизводится, поставьте его на паузу
            mediaPlayer?.pause()
            // Кнопка Pause появляется, кнопка Play исчезает
            pauseButton.visibility = View.GONE
            playButton.visibility = View.VISIBLE

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

        val sharedPreferences = getSharedPreferences("AudioPlayerState", Context.MODE_PRIVATE)
        playerStateRepository = PlayerStateRepositoryImpl(sharedPreferences)
        playerStateRepository.putString("currentTrackId", currentTrackId)


        mediaPlayer?.pause()
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeLoadingObserver()
        mediaPlayer?.release()
        mediaPlayer = null
    }
        private fun changeProgressBarVisibility(visible: Boolean) {
            // Обновляем видимость прогресс-бара
            val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
            progressBar.isVisible = visible
            binding.progressBar.isVisible = visible
        }


    // Метод вызывается при восстановлении активности из фонового режима
    override fun onResume() {
        super.onResume()

        trackService.updateCurrentTime(mediaPlayer, currentTimeTextView)

        handler.postDelayed(
            { trackService.updateCurrentTime(mediaPlayer, currentTimeTextView) },
            1000
        )


        // Восстановите состояние экрана "Аудиоплеер" из SharedPreferences
        val sharedPreferences = getSharedPreferences("AudioPlayerState", Context.MODE_PRIVATE)
        playerStateRepository = PlayerStateRepositoryImpl(sharedPreferences)
        val currentTrackId = playerStateRepository.getString("currentTrackId", null)
        // Используйте значение currentTrackId для выполнения необходимых действий


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
