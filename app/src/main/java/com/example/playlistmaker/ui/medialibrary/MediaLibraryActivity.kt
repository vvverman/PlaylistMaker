package com.example.playlistmaker.ui.medialibrary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.medialibrary.model.MediaLibraryState
import com.example.playlistmaker.domain.utils.DateFormat
import com.example.playlistmaker.ui.medialibrary.view_model.MediaLibraryViewModel
import com.example.playlistmaker.ui.util.load
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryActivity : AppCompatActivity() {
    private var binding: ActivityMediaLibraryBinding? = null
    private val mediaLibraryViewModel: MediaLibraryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initViews()
        initObservers()
    }

    override fun onPause() {
        super.onPause()
        mediaLibraryViewModel.onPause()
    }

    override fun onStop() {
        super.onStop()
        mediaLibraryViewModel.onStop()
    }

    private fun initViews(){
        binding?.apply {
            backButton.setOnClickListener { onBackPressed() }
            addToFavoriteButton.setOnClickListener { mediaLibraryViewModel.onLikeButtonClicked() }
            playButton.setOnClickListener { mediaLibraryViewModel.onPlayButtonClicked() }
        }
    }

    private fun initObservers() {
        mediaLibraryViewModel.state.observe(this) {
            it.track?.let { track -> setTrackData(track) }
            binding?.currentTime?.text = it.trackTime.ifEmpty {
                "00:00"
            }
            it?.mediaLibraryState?.let { state ->
                when (state) {
                    MediaLibraryState.PLAYING -> binding?.playButton?.setImageResource(R.drawable.pause_button)
                    MediaLibraryState.PREPARED,
                    MediaLibraryState.PAUSED -> binding?.playButton?.setImageResource(R.drawable.play_button)
                }
            }
        }

        mediaLibraryViewModel.event.observe(this) {
            Toast.makeText(this, getString(R.string.playlist_create), Toast.LENGTH_LONG).show()
        }
    }

    private fun setTrackData(track: Track) {
        binding?.apply {
            artistName.text = track.artistName
            trackName.text = track.trackName
            if (track.albumName.isNotEmpty())
                collectionName.text = track.albumName
            else {
                collectionName.isVisible = false
                collectionNameTitle.isVisible = false
            }
            country.text = track.country
            releaseDate.text = DateFormat.getYearFromReleaseDate(track.releaseDate)
            durationData.text = DateFormat.formatMillisToString(track.trackTimeMillis).replaceFirst("0", "")
            primaryGenreName.text = track.genre
            coverImageView.load(track.artworkUrl100)
        }
    }
}