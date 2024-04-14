package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.ui.medialibrary.MediaLibraryActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private var binding: ActivitySearchBinding? = null
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var trackHistoryAdapter: TrackHistoryAdapter
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        initViews()
        initObservers()
    }

    private fun initViews() {
        binding?.apply {
            backButton.setOnClickListener { onBackPressed() }
            noNetworkUpdateButton.setOnClickListener { viewModel.onMessageButtonClicked() }
            initTracksRecyclerView()
            initTracksHistoryRecyclerView()
            clearButton.setOnClickListener {
                viewModel.onClearButtonClicked()
            }
        }
        initEditTextSearch()
        initClearButton()
    }

    private fun initTracksRecyclerView() {
        trackAdapter = TrackAdapter(viewModel::onTrackClicked)
        binding?.recyclerView?.adapter = trackAdapter
    }

    private fun initTracksHistoryRecyclerView() {
        trackHistoryAdapter = TrackHistoryAdapter(viewModel::onTrackClicked)
        binding?.searchInputLayout?.recyclerViewSearchHistory?.adapter = trackHistoryAdapter
    }

    private fun initEditTextSearch() {
        binding?.searchField?.apply {
            doOnTextChanged { text, _, _, _ ->
                viewModel.onSearchRequestChanged(text?.toString()?.trim() ?: "")
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.onEnterClicked()
                }
                return@setOnEditorActionListener false
            }
            setOnFocusChangeListener { _, hasFocus -> viewModel.onSearchFocusChanged(hasFocus) }
        }
    }

    private fun initClearButton() {
        binding?.searchInputLayout?.clearSearchHistoryButton?.setOnClickListener {
            viewModel.onClearHistoryButtonClicked()
        }
    }

    private fun initObservers() {
        viewModel.state.observe(this) {
            binding?.apply {
                clearButton.isVisible = it.clearButtonVisible
                trackAdapter.updateData(it.tracks)
                recyclerView.isVisible = it.tracks.isNotEmpty()
                trackHistoryAdapter.update(it.tracksHistory)
                searchInputLayout.searchInputLayout.isVisible = it.tracksHistoryVisible
                progressBar.isVisible = it.progressVisible

            }
        }

        viewModel.event.observe(this) {
            when (it) {
                is SearchScreenEvent.OpenPlayerScreen -> {
                    startActivity(Intent(this, MediaLibraryActivity()::class.java))
                }
                is SearchScreenEvent.ClearSearch->binding?.searchField?.text?.clear()

                else -> hideKeyboard()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.clearButton?.windowToken, 0)
    }
}
