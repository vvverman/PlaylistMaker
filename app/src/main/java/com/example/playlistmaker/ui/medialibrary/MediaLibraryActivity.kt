package com.example.playlistmaker.ui.medialibrary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.medialibrary.model.MediaLibraryState
import com.example.playlistmaker.domain.utils.DateFormat
import com.example.playlistmaker.ui.medialibrary.view_model.MediaLibraryViewModel
import com.example.playlistmaker.ui.util.load

class MediaLibraryActivity : AppCompatActivity() {
    private var binding: ActivityMediaLibraryBinding? = null
    private lateinit var viewModel: MediaLibraryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this).get<MediaLibraryViewModel>()
        initViews()
        initObservers()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    private fun initViews(){
        binding?.apply {
            backButton.setOnClickListener { onBackPressed() }
            addToFavoriteButton.setOnClickListener { viewModel.onLikeButtonClicked() }
            playButton.setOnClickListener { viewModel.onPlayButtonClicked() }
        }
    }

    private fun initObservers() {
        viewModel.state.observe(this) {
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

        viewModel.event.observe(this) {
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
            coverImageView.load(track.previewUrl)
        }
    }
}