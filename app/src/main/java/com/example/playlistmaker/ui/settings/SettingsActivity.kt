package com.example.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private var binding: ActivitySettingsBinding? = null
    private  val settingsViewModel: SettingsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpToolbar()
        setUpButtons()
        setUpThemeSwitch()
        }

    private fun setUpToolbar() = binding?.backButton?.setOnClickListener {
        onBackPressed()
    }

    private fun setUpButtons() {
        binding?.apply {
            shareButton.setOnClickListener { settingsViewModel.onShareAppButtonClicked() }
            supportButton.setOnClickListener { settingsViewModel.onWriteSupportButtonClicked() }
            termsOfUseButton.setOnClickListener { settingsViewModel.onUserAgreementsButtonClicked() }
        }
    }

    private fun setUpThemeSwitch() {
        binding?.themeSwitcher?.apply {
            settingsViewModel.ThemeSettings.observe(this@SettingsActivity) {
                isChecked = it
            }
            setOnClickListener { settingsViewModel.onThemeSwitchClicked(this.isChecked) }
        }
    }
}