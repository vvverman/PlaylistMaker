package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null
    private  val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpButtons()
        setupThemeSwitch()
    }

    private fun setUpButtons() {
        binding?.apply {
            shareButton.setOnClickListener { settingsViewModel.onShareAppButtonClicked() }
            supportButton.setOnClickListener { settingsViewModel.onWriteSupportButtonClicked() }
            termsOfUseButton.setOnClickListener { settingsViewModel.onUserAgreementsButtonClicked() }
        }
    }

    private fun setupThemeSwitch() {
        binding?.themeSwitcher?.apply {
            settingsViewModel.ThemeSettings.observe(this@SettingsFragment) {
                isChecked = it
            }
            setOnClickListener { settingsViewModel.onThemeSwitchClicked(this.isChecked) }
        }
    }
}


