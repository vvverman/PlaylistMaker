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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
    private lateinit var searchInputLayout: LinearLayout
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter



    enum class SearchViewState {
        NO_INTERNET,
        NO_RESULTS,
        HAS_RESULTS,
        HISTORY,
        EMPTY
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPreferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        recyclerViewSearchHistory = findViewById(R.id.recyclerViewSearchHistory)
        searchInputLayout = findViewById(R.id.search_input_layout)
        searchHistoryAdapter = SearchHistoryAdapter(searchHistory.getHistory())
        recyclerViewSearchHistory.adapter = searchHistoryAdapter

        tracksAdapter = TracksAdapter(this)

        searchInputLayout.visibility = View.GONE // Отображаем историю поиска сразу

        searchField = findViewById(R.id.searchField)

        initBackButton()

        val clearButton = initClearButton()

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                valueInSearchString = s.toString()
                clearButton?.let { cb ->
                    cb.visibility = clearButtonVisibility(s)
                }


            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        // Устанавливаем TextWatcher
        searchField.addTextChangedListener(simpleTextWatcher)


        val searchField = findViewById<EditText>(R.id.searchField)

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Вызывайте функцию поиска при нажатии на клавишу "Enter"
                val searchTerm = searchField.text.toString()
                if (searchTerm.isNotEmpty()) {
                    searchTracks(searchTerm)
                    updateContainersVisibility()
                }
                true // Возвращаем true, чтобы показать, что событие обработано
            } else {
                false
            }

        }


        val itemsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        tracksAdapter = TracksAdapter(this@SearchActivity)

        itemsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        itemsRecyclerView.adapter = tracksAdapter

        recyclerViewSearchHistory = findViewById(R.id.recyclerViewSearchHistory)
        searchHistoryAdapter = SearchHistoryAdapter(emptyList())

        // Устанавливаем слушателя клика на элемент списка:
        tracksAdapter.setOnItemClickListener(object : TracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {

                //  RecyclerView для истории, надо обновить его:
                searchHistoryAdapter.updateItems(searchHistory.getHistory())


                // Внутри onItemClick
                searchHistory.addTrackToHistory(track)

// После добавления элемента в историю, обновите отображение истории
                updateSearchHistoryView()
            }
        })


        searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchHistory.getHistory().isNotEmpty()) {
                // Если поле поиска в фокусе и есть элементы в истории поиска, покажите RecyclerView для истории поиска
                searchInputLayout.visibility = View.VISIBLE

            }
        }

        val clearSearchHistoryButton: RelativeLayout = findViewById(R.id.clearSearchHistoryButton)

        clearSearchHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            val emptyDataList: List<Track> = ArrayList()
            searchHistoryAdapter.updateItems(emptyDataList)
            currentViewState = SearchViewState.EMPTY
            updateContainersVisibility()
        }
    }

    private fun initBackButton() {
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun initClearButton(): ImageView? {
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            searchField.setText("")
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
        }
        return clearButton
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

        currentViewState = SearchViewState.HAS_RESULTS
        updateContainersVisibility()
        val internetConnection = hasInternetConnection()

        if (!hasInternetConnection()) {
            updateNoNetworkMessageVisibility(internetConnection)
        }

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
                    tracksResponse?.tracks?.let { tracks ->

                        tracksAdapter.updateTracks(tracks)
                        currentViewState = if (tracks.isNotEmpty()) {
                            SearchViewState.HAS_RESULTS
                        } else {
                            SearchViewState.NO_RESULTS
                        }

                    }
                }
                updateContainersVisibility()
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                currentViewState = SearchViewState.NO_INTERNET
                updateContainersVisibility()
            }

        })
        updateContainersVisibility()
    }

    private fun updateSearchHistoryView() {
        // Получите текущую историю поиска из объекта SearchHistory
        val historyItems = searchHistory.getHistory()

        // Обновите адаптер истории поиска
        searchHistoryAdapter.updateItems(historyItems)


        // Убедитесь, что RecyclerView для истории видим
        recyclerViewSearchHistory.visibility = View.VISIBLE
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

        println(currentViewState)

        when (currentViewState) {
            SearchViewState.NO_INTERNET -> {
                noInternetContainer.visibility = View.VISIBLE
                noResultsContainer.visibility = View.GONE
                resultsContainer.visibility = View.GONE
                recyclerViewSearchHistory.visibility = View.GONE

            }

            SearchViewState.NO_RESULTS -> {
                noInternetContainer.visibility = View.GONE
                noResultsContainer.visibility = View.VISIBLE
                resultsContainer.visibility = View.GONE
                recyclerViewSearchHistory.visibility = View.GONE
            }

            SearchViewState.HAS_RESULTS -> {
                noInternetContainer.visibility = View.GONE
                noResultsContainer.visibility = View.GONE
                resultsContainer.visibility = View.VISIBLE
                recyclerViewSearchHistory.visibility = View.GONE
                searchInputLayout.visibility = View.GONE
            }

            SearchViewState.HISTORY -> {
                noInternetContainer.visibility = View.GONE
                noResultsContainer.visibility = View.VISIBLE
                resultsContainer.visibility = View.GONE
                recyclerViewSearchHistory.visibility = View.GONE
            }

            SearchViewState.EMPTY -> {
                noInternetContainer.visibility = View.GONE
                noResultsContainer.visibility = View.GONE
                resultsContainer.visibility = View.GONE
                searchInputLayout.visibility = View.GONE
            }
        }
    }
}
