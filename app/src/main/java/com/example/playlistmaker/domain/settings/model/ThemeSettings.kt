package com.example.playlistmaker.domain.settings.model

import com.google.gson.annotations.SerializedName

data class ThemeSettings(
    @SerializedName("isDarkThemeEnabled") val isDarkThemeEnabled: Boolean
)
