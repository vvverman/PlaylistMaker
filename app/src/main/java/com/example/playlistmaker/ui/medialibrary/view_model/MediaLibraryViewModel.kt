package com.example.playlistmaker.ui.medialibrary.view_model

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.domain.medialibrary.MediaLibraryInteractor
import com.example.playlistmaker.domain.medialibrary.model.MediaLibraryState
import com.example.playlistmaker.domain.utils.DateFormat
import com.example.playlistmaker.ui.medialibrary.MediaLibraryScreenEvent
import com.example.playlistmaker.ui.medialibrary.MediaLibraryScreenState
import com.example.playlistmaker.ui.util.SingleLiveEvent

class MediaLibraryViewModel(
    playerInteractor: MediaLibraryInteractor,
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private const val TIME_STEP_MILLIS = 300L
    }

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private var trackDurationRunnable = object : Runnable {
        override fun run() {
            val time = DateFormat.formatMillisToString(mediaPlayer.currentPosition.toLong())
            if (getCurrentScreenState().mediaLibraryState == MediaLibraryState.PLAYING) {
                handler.postDelayed(this, TIME_STEP_MILLIS)
                _state.postValue(MediaLibraryScreenState(MediaLibraryState.PLAYING, track, time))
            } else {
                pausePlayer()
            }
        }
    }

    private val track = playerInteractor.getTrackForPlaying()

    private val _state = MutableLiveData<MediaLibraryScreenState>()
    val state: LiveData<MediaLibraryScreenState> = _state

    val event = SingleLiveEvent<MediaLibraryScreenEvent>()

    init {
        _state.value = MediaLibraryScreenState(MediaLibraryState.PAUSED, track)
        initPlayer()
    }

    override fun onCleared() {
        handler.removeCallbacks(trackDurationRunnable)
        mediaPlayer.release()
        super.onCleared()
    }

    fun onPause() = pausePlayer()

    fun onStop() {
        if (getCurrentScreenState().mediaLibraryState != MediaLibraryState.PAUSED) {
            handler.removeCallbacks(trackDurationRunnable)
            mediaPlayer.release()
        }
    }

    fun onPlayButtonClicked() {
        track?.let {
            when (getCurrentScreenState().mediaLibraryState) {
                MediaLibraryState.PLAYING -> pausePlayer()
                MediaLibraryState.PREPARED, MediaLibraryState.PAUSED -> startPlayer()
            }
            handler.post(trackDurationRunnable)
        }
    }

    fun onLikeButtonClicked() {
        event.postValue(MediaLibraryScreenEvent.ShowPlayListCreatedToast)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        _state.value = getCurrentScreenState().copy(mediaLibraryState = MediaLibraryState.PAUSED)
    }

    private fun startPlayer() {
        if (getCurrentScreenState().mediaLibraryState != MediaLibraryState.PLAYING) {
            mediaPlayer.start()
            _state.value = getCurrentScreenState().copy(mediaLibraryState = MediaLibraryState.PLAYING)
        }
    }

    private fun initPlayer() {
        track?.let {
            mediaPlayer.apply {
                setDataSource(getApplication(), Uri.parse(it.previewUrl))
                prepareAsync()
                setOnPreparedListener {
                    _state.value = getCurrentScreenState().copy(mediaLibraryState = MediaLibraryState.PREPARED)
                }
                setOnCompletionListener {
                    _state.value =
                        getCurrentScreenState().copy(
                            mediaLibraryState = MediaLibraryState.PREPARED,
                            trackTime = ""
                        )
                }
            }
        }
    }

    private fun getCurrentScreenState() = _state.value ?: MediaLibraryScreenState()
}