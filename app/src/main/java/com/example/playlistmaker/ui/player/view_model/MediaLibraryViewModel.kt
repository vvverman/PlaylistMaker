package com.example.playlistmaker.ui.player.view_model

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.model.PlayerState
import com.example.playlistmaker.domain.utils.DateFormat
import com.example.playlistmaker.ui.player.PlayerScreenEvent
import com.example.playlistmaker.ui.player.PlayerScreenState
import com.example.playlistmaker.ui.util.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaLibraryViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    playerInteractor: PlayerInteractor,
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private const val TIME_STEP_MILLIS = 300L
    }

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var trackDurationRunnable: Job? = null

    private var track = playerInteractor.getTrackForPlaying()

    private val _state = MutableLiveData<PlayerScreenState>()
    val state: LiveData<PlayerScreenState> = _state

    val event = SingleLiveEvent<PlayerScreenEvent>()

    init {
        _state.value = PlayerScreenState(PlayerState.PAUSED, track)
        subscribeOnFavorites()
        initPlayer()
    }

    override fun onCleared() {
        mediaPlayer.release()
        super.onCleared()
    }

    fun onPause() = pausePlayer()

    fun onStop() {
        if (getCurrentScreenState().playerState != PlayerState.PAUSED) {
            trackDurationRunnable?.cancel()
            mediaPlayer.release()
        }
    }

    fun onPlayButtonClicked() {
        track?.let {
            when (getCurrentScreenState().playerState) {
                PlayerState.PLAYING -> pausePlayer()
                PlayerState.PREPARED, PlayerState.PAUSED -> startPlayer()
            }
        }
    }

    fun onLikeButtonClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            track?.apply {
                if (isFavorite) {
                    favoritesInteractor.deleteFromFavorites(this)
                } else favoritesInteractor.addToFavorites(this)
            }
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        _state.value = getCurrentScreenState().copy(playerState = PlayerState.PAUSED)
    }

    private fun startPlayer() {
        if (getCurrentScreenState().playerState != PlayerState.PLAYING) {
            mediaPlayer.start()
            trackDurationRunnable?.cancel()
            trackDurationRunnable = viewModelScope.launch {
                while (mediaPlayer.isPlaying){                    val time =
                    DateFormat.formatMillisToString(mediaPlayer.currentPosition.toLong())
                    _state.postValue(
                        getCurrentScreenState().copy(
                            playerState = PlayerState.PLAYING,
                            track = track,
                            trackTime = time
                        )
                    )
                    delay(TIME_STEP_MILLIS)
                }
            }
        }
    }

    private fun initPlayer() {
        track?.let {
            mediaPlayer.apply {
                setDataSource(getApplication(), Uri.parse(it.previewUrl))
                prepareAsync()
                setOnPreparedListener {
                    _state.value = getCurrentScreenState().copy(playerState = PlayerState.PREPARED)
                }
                setOnCompletionListener {
                    _state.value =
                        getCurrentScreenState().copy(
                            playerState = PlayerState.PREPARED,
                            trackTime = ""
                        )
                }
            }
        }
    }

    private fun subscribeOnFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.getFavorites().collect { favorites ->
                track?.let {
                    track = it.copy(isFavorite = it.id in favorites.map { track ->
                        track.id
                    }.toSet())
                    _state.postValue(
                        getCurrentScreenState().copy(isFavorites = track?.isFavorite ?: false)
                    )
                }
            }
        }
    }

    private fun getCurrentScreenState() = _state.value ?: PlayerScreenState()
}