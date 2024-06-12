package com.example.playlistmaker.domain.main.impl

import androidx.lifecycle.LiveData
import com.example.playlistmaker.data.main.NavigationRepo
import com.example.playlistmaker.domain.main.NavigationInteractor

class NavigationInteractorImpl(
    private val navigationRepo: NavigationRepo
) : NavigationInteractor {

    override val isBottomNavigationVisible: LiveData<Boolean> =
        navigationRepo.isBottomNavigationVisible

    override fun setBottomNavigationVisibility(isVisible: Boolean) {
        navigationRepo.setBottomNavigationVisibility(isVisible)
    }
}