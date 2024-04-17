package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.model.PlayerState
import com.example.playlistmaker.domain.utils.DateFormat
import com.example.playlistmaker.ui.player.view_model.MediaLibraryViewModel
import com.example.playlistmaker.ui.util.load
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {
    private var binding: ActivityPlayerBinding? = null
    private val mediaLibraryViewModel: MediaLibraryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
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
            it?.playerState?.let { state ->
                when (state) {
                    PlayerState.PLAYING -> binding?.playButton?.setImageResource(R.drawable.pause_button)
                    PlayerState.PREPARED,
                    PlayerState.PAUSED -> binding?.playButton?.setImageResource(R.drawable.play_button)
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