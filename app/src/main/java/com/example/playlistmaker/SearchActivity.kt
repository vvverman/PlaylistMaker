package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var valueInSearchString = ""
    private var currentViewState = SearchViewState.NO_INTERNET
    private lateinit var searchHistory: SearchHistory
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchField: EditText
    private lateinit var recyclerViewSearchHistory: RecyclerView
    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    enum class SearchViewState {
        NO_INTERNET,
        NO_RESULTS,
        HAS_RESULTS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPreferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        recyclerViewSearchHistory = findViewById(R.id.recyclerViewSearchHistory)
        recyclerViewSearchHistory.visibility = View.VISIBLE // Отображаем историю поиска сразу

        searchField = findViewById(R.id.searchField)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        searchField.setOnFocusChangeListener { _, hasFocus ->
            val searchInputLayoutInclude = findViewById<View>(R.id.search_input_layout_include)
            val recyclerViewSearchHistory =
                findViewById<RecyclerView>(R.id.recyclerViewSearchHistory)

            if (hasFocus) {
                searchInputLayoutInclude.visibility = View.VISIBLE
                recyclerViewSearchHistory.visibility = View.GONE
            } else {
                searchInputLayoutInclude.visibility = View.GONE
                recyclerViewSearchHistory.visibility = View.VISIBLE
            }
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            searchField.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                valueInSearchString = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                searchTracks(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchField.addTextChangedListener(simpleTextWatcher)

        val itemsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        itemsAdapter = ItemsAdapter()
        itemsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemsRecyclerView.adapter = itemsAdapter

        recyclerViewSearchHistory = findViewById(R.id.recyclerViewSearchHistory)
        searchHistoryAdapter = SearchHistoryAdapter(emptyList())
        recyclerViewSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewSearchHistory.adapter = searchHistoryAdapter

        // Установите историю поиска в адаптер сразу при создании активности
        searchHistoryAdapter.updateItems(searchHistory.getHistory())

        val clearSearchHistoryButton: RelativeLayout = findViewById(R.id.clearSearchHistoryButton)

        clearSearchHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            val emptyDataList: List<Item> = ArrayList()
            searchHistoryAdapter.updateItems(emptyDataList)
        }
    }

    companion object {
        const val REQUEST_TEXT = "REQUEST_TEXT"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(REQUEST_TEXT, valueInSearchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        valueInSearchString = savedInstanceState.getString(REQUEST_TEXT, "")
        searchField.text = Editable.Factory.getInstance().newEditable(valueInSearchString)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchTracks(searchTerm: String) {
        Log.e("mylog", "Start searching for term: $searchTerm")

        currentViewState = SearchViewState.NO_RESULTS

        val internetConnection = hasInternetConnection()
        updateNoNetworkMessageVisibility(internetConnection)

        if (internetConnection) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val iTunesSearchApi = retrofit.create(ITunesSearchApi::class.java)

            val mediaType = "music"
            val call = iTunesSearchApi.searchTracks(searchTerm, mediaType)

            call.enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.isSuccessful) {
                        val tracksResponse = response.body()
                        tracksResponse?.items?.let { items ->
                            itemsAdapter.updateItems(items)
                            currentViewState = if (items.isEmpty()) {
                                SearchViewState.NO_RESULTS
                            } else {
                                SearchViewState.HAS_RESULTS
                            }
                        }
                    } else {
                        currentViewState = SearchViewState.NO_RESULTS
                    }
                    updateContainersVisibility()
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    currentViewState = SearchViewState.NO_INTERNET
                    updateContainersVisibility()
                }
            })
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        }
        return false
    }

    private fun updateNoNetworkMessageVisibility(hasConnection: Boolean) {
        val communicationProblems = findViewById<FrameLayout>(R.id.communicationProblems)
        if (hasConnection) {
            communicationProblems.visibility = View.GONE
        } else {
            communicationProblems.visibility = View.VISIBLE
        }
    }

    private fun updateContainersVisibility() {
        val noInternetContainer = findViewById<FrameLayout>(R.id.communicationProblems)
        val noResultsContainer = findViewById<FrameLayout>(R.id.noSearchResults)
        val resultsContainer = findViewById<RecyclerView>(R.id.recyclerView)

        when (currentViewState) {
            SearchViewState.NO_INTERNET -> {
                noInternetContainer.visibility = View.VISIBLE
                noResultsContainer.visibility = View.GONE
                resultsContainer.visibility = View.GONE
            }

            SearchViewState.NO_RESULTS -> {
                noInternetContainer.visibility = View.GONE
                noResultsContainer.visibility = View.VISIBLE
                resultsContainer.visibility = View.GONE
            }

            SearchViewState.HAS_RESULTS -> {
                noInternetContainer.visibility = View.GONE
                noResultsContainer.visibility = View.GONE
                resultsContainer.visibility = View.VISIBLE
            }
        }
    }
}
