package com.example.playlistmaker.ui.search

sealed class SearchScreenEvent{
    data object HideKeyboard : SearchScreenEvent()
    data object OpenPlayerScreen : SearchScreenEvent()
    data object ClearSearch : SearchScreenEvent()
}
