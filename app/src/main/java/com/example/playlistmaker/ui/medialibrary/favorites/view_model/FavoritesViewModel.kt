package com.example.playlistmaker.ui.medialibrary.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.playlistmaker.ui.medialibrary.favorites.FavoritesScreenEvent

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>(listOf())
    val tracks: LiveData<List<Track>> = _tracks


    val event = SingleLiveEvent<FavoritesScreenEvent>()

    init {
        subscribeOnFavorites()
    }

    fun onTrackClicked(track: Track) {
        favoritesInteractor.saveTrackForPlaying(track)
        event.value = FavoritesScreenEvent.OpenPlayerScreen
    }

    private fun subscribeOnFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.getFavorites().collect { _tracks.postValue(it) }
        }
    }
}