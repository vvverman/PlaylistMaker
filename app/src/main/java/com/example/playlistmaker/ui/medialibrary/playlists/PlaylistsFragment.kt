package com.example.playlistmaker.ui.medialibrary.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.medialibrary.playlists.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.ui.util.ResultKeyHolder

class PlaylistsFragment : Fragment() {

    private var binding: FragmentPlaylistsBinding? = null
    private val viewModel: PlaylistsViewModel by viewModel()
    private val playlistAdapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(layoutInflater)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            ResultKeyHolder.KEY_PLAYLIST_CREATED,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(ResultKeyHolder.KEY_PLAYLIST_NAME)?.let { showPlaylistCreatedMessage(it) }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        binding?.btnCreatePlaylist?.setOnClickListener { viewModel.onNewPlaylistButtonClicked() }
        binding?.playlists?.adapter = playlistAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    private fun initObservers() {
        viewModel.playlists.observe(viewLifecycleOwner) {
            playlistAdapter.submitList(it)
            binding?.layoutNoPlaylists?.isVisible = it.isEmpty()
            binding?.playlists?.isVisible = it.isNotEmpty()
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                PlaylistsScreenEvent.NavigateToNewPlaylist -> navigateToNewPlaylist()
            }
        }
    }

    private fun navigateToNewPlaylist() {
        findNavController()
            .navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
    }

    private fun showPlaylistCreatedMessage(playlistName: String) {
        Toast.makeText(requireContext(), getString(R.string.playlist_created_snackbar, playlistName), Toast.LENGTH_SHORT)
            .show()
    }
}