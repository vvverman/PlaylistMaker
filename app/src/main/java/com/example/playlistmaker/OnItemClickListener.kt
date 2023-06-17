package com.example.playlistmaker

import android.content.ClipData

interface OnItemClickListener {
    fun onItemClick(item: ClipData.Item)
}