package com.example.playlistmaker.data.main

import androidx.lifecycle.LiveData

interface NavigationRepo {
    val isBottomNavigationVisible: LiveData<Boolean>

    fun setBottomNavigationVisibility(isVisible: Boolean)
}