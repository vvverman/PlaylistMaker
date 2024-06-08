package com.example.playlistmaker.ui.medialibrary.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.ui.medialibrary.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.ui.search.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var binding: FragmentFavoritesBinding? = null
    private val viewModel: FavoritesViewModel by viewModel()
    private var trackAdapter: TrackAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTracksRecyclerView()
        initObservers()
    }


    private fun initTracksRecyclerView() {
        trackAdapter = TrackAdapter(viewModel::onTrackClicked)
        binding?.favorites?.adapter = trackAdapter
    }

    private fun initObservers() {
        viewModel.tracks.observe(viewLifecycleOwner) {
            trackAdapter?.updateData(it)
            binding?.apply {
                layoutNoFavorites.isVisible = it.isEmpty()
                favorites.isVisible = it.isNotEmpty()
            }
        }

        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is FavoritesScreenEvent.OpenPlayerScreen -> {
                    startActivity(Intent(requireContext(), PlayerActivity()::class.java))
                }
            }
        }
    }
}