package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.player.model.Track
import com.example.playlistmaker.domain.player.TracksInteractor


class MediaLibraryViewModel(
    private val track: Track,
    private val tracksInteractor: TracksInteractor,
) : ViewModel() {

    private var loadingLiveData = MutableLiveData(true)

    init {
        tracksInteractor.loadSomeData(
            onComplete = {
                loadingLiveData.postValue(false)
                loadingLiveData.value = false
            }
        )
    }

    fun getLoadingLiveData(): LiveData<Boolean> = loadingLiveData


    companion object {
        // 1
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            // 2
            initializer {
                // 3
                val interactor = (this[APPLICATION_KEY] as MyApplication).provideTracksInteractor()

                MediaLibraryViewModel(
                    track,
                    interactor,
                )
            }
        }
    }
}


