package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.ui.search.view_model.SearchViewModel

class SearchFragment : Fragment() {
    private var binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var trackHistoryAdapter: TrackHistoryAdapter
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
    }


    private fun initViews() {
        binding?.run {
//            backButton.setOnClickListener { viewModel.onMessageButtonClicked() }
            clearButton.setOnClickListener {
                viewModel.onClearButtonClicked()
            }
        }
        initTracksRecyclerView()
        initTracksHistoryRecyclerView()
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
                communicationProblems.isVisible = it.noWebVisible
//
                val messageResId = if (it.noTracksVisible) R.string.no_search_results_message else R.string.communication_problems_message
                noSearchResultsText.text = getString(messageResId)

                val messageImageResId =
                    if (it.noTracksVisible) R.drawable.no_search_results_image else R.drawable.communication_problems_image

                noSearchResultsImage.setImageResource(messageImageResId)

                noSearchResults.isVisible = it.messageVisible
                //
            }
        }

        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is SearchScreenEvent.OpenPlayerScreen -> {
                    startActivity(Intent(requireContext(), PlayerActivity()::class.java))
                }
                is SearchScreenEvent.ClearSearch->binding?.searchField?.text?.clear()

                else -> hideKeyboard()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.clearButton?.windowToken, 0)
    }
}
