package com.example.playlistmaker.ui.medialibrary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.medialibrary.favorites.FavoritesFragment
import com.example.playlistmaker.ui.medialibrary.playlists.PlaylistsFragment

class MediaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle ) {

    companion object {
        private const val ITEM_COUNT = 2
    }

    override fun getItemCount(): Int = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoritesFragment() else PlaylistsFragment()
    }
}