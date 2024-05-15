package com.example.playlistmaker.ui.medialibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMedialibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MedialibraryFragment : Fragment() {

    private var binding: FragmentMedialibraryBinding? = null

    private var tabMediator: TabLayoutMediator? = null
    private val tabsTitleResIds = arrayOf(
        R.string.favorites,
        R.string.playlists
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMedialibraryBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }

    private fun initTabLayout() = binding?.run {
        viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(tabsTitleResIds[position])
        }
        tabMediator?.attach()
    }
}


