package com.example.playlistmaker.data.main.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.data.main.NavigationRepo

class NavigationRepoImpl : NavigationRepo {

    private val _isBottomNavigationVisible = MutableLiveData<Boolean>(true)
    override val isBottomNavigationVisible: LiveData<Boolean> = _isBottomNavigationVisible

    override fun setBottomNavigationVisibility(isVisible: Boolean) {
        _isBottomNavigationVisible.value = isVisible
    }
}