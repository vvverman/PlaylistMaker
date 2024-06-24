package com.example.playlistmaker.ui.medialibrary.edit_playlist

import android.os.Bundle
import android.view.View
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.medialibrary.create_playlist.CreatePlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.ui.medialibrary.edit_playlist.view_model.EditPlaylistViewModel

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel: EditPlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            binding?.apply {
                editPlaylistName.setText(playlist.name)
                editTextDescription.setText(playlist.description)
            }
        }
    }

    override fun initToolbar() {
        super.initToolbar()
        binding?.apply { toolbar.setTitle(R.string.edit) }
    }

    override fun initCompleteButton() {
        super.initCompleteButton()
        binding?.apply { buttonComplete.setText(R.string.save) }
    }
}