package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.model.PlayerState
import com.example.playlistmaker.domain.utils.DateFormat
import com.example.playlistmaker.ui.player.fragment.view_model.MediaLibraryViewModel
import com.example.playlistmaker.ui.util.ResultKeyHolder
import com.example.playlistmaker.ui.util.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private var binding: FragmentPlayerBinding? = null
    private val viewModel: MediaLibraryViewModel by viewModel()

    private lateinit var playlistAdapter: BottomSheetPlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            ResultKeyHolder.KEY_PLAYLIST_CREATED,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(ResultKeyHolder.KEY_PLAYLIST_NAME)?.let {
                val text = getString(R.string.playlist_created_snackbar, it)
                showToast(text)
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlaylistsBottomSheet()
        initButtons()
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initButtons() {
        binding?.apply {
            toolbarMedia.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            addToFavoriteButton.setOnClickListener { viewModel.onAddButtonClicked() }
            addToFavorite.setOnClickListener { viewModel.onLikeButtonClicked() }
            playButton.setOnClickListener { viewModel.onPlayButtonClicked() }
            createPlaylistButton.setOnClickListener { viewModel.onCreatePlaylistButtonClicked() }
        }
    }

    private fun initPlaylistsBottomSheet() {
        binding?.let {
            BottomSheetBehavior.from(it.bottomSheetPlaylists).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(
                    object : BottomSheetBehavior.BottomSheetCallback() {

                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            it.overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    })
            }
            playlistAdapter = BottomSheetPlaylistAdapter(viewModel::onPlaylistClicked)
            it.rvPlaylists.adapter = playlistAdapter
        }
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            it.track?.let { track -> setTrackData(track) }
            binding?.timeTrack?.text = it.trackTime.ifEmpty {
                getString(R.string.duration_value)
            }
            it?.playerState?.let { state ->
                when (state) {
                    PlayerState.PLAYING -> binding?.playButton?.setImageResource(R.drawable.pause_button)
                    PlayerState.PREPARED,
                    PlayerState.PAUSED -> binding?.playButton?.setImageResource(R.drawable.play_button)
                }
            }
            if (it.isFavorites)
                binding?.addToFavorite?.setImageResource(R.drawable.add_to_favorites_filled)
            else
                binding?.addToFavorite?.setImageResource(R.drawable.add_to_favorite)
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlistAdapter.submitList(it) }

        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is PlayerScreenEvent.OpenPlaylistsBottomSheet -> {
                    binding?.bottomSheetPlaylists?.let {
                        val bottomSheetBehavior = BottomSheetBehavior.from(it)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                is PlayerScreenEvent.ShowTrackAddedMessage -> {
                    showToast(getString(R.string.added_to_playlist, it.playlistName))
                }

                is PlayerScreenEvent.ShowTrackAlreadyInPlaylistMessage -> {
                    showToast(getString(R.string.track_already_in_playlist, it.playlistName))
                }

                is PlayerScreenEvent.ClosePlaylistsBottomSheet -> {
                    binding?.bottomSheetPlaylists?.let {
                        val bottomSheetBehavior = BottomSheetBehavior.from(it)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }

                is PlayerScreenEvent.NavigateToCreatePlaylistScreen -> {
                    findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
                }
            }
        }
    }

    private fun setTrackData(track: Track) {
        binding?.apply {
            artistName.text = track.artistName
            trackName.text = track.trackName
            if (track.albumName?.isNotEmpty() == true)
                albumValue.text = track.albumName
            else {
                albumValue.isVisible = false
                albumTitle.isVisible = false
            }
            countryValue.text = track.country
            yearValue.text = DateFormat.getYearFromReleaseDate(track.releaseDate)
            durationValue.text =
                DateFormat.formatMillisToString(track.trackTimeMillis).replaceFirst("0", "")
            genreValue.text = track.genre
            playerMain.load(track.artworkUrl100, true)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}