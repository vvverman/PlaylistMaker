package com.example.playlistmaker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.medialibrary.MediaLibraryActivity
import com.example.playlistmaker.ui.settings.SettingsActivity
import com.example.playlistmaker.ui.search.SearchActivity


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpButtonListeners()
    }

    private fun setUpButtonListeners() {
        binding?.apply {
            editTextSearch.setOnClickListener {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
            Player.setOnClickListener {
                startActivity(Intent(this@MainActivity, MediaLibraryActivity::class.java))
            }
            settings.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }
    }
}