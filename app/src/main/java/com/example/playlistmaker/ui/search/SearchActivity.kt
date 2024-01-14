package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.ITunesSearchInteractor
import com.example.playlistmaker.data.search.impl.HistoryStorageInteractorImpl
import com.example.playlistmaker.data.search.model.TracksResponse
import com.example.playlistmaker.data.search.HistoryStorageInteractor
import com.example.playlistmaker.domain.player.model.Track
import com.example.playlistmaker.domain.player.TracksAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    // Переменные для отслеживания состояния поиска
    private var valueInSearchString = ""
    private var currentViewState = SearchViewState.NO_INTERNET

    // Переменные для управления историей поиска
    private lateinit var historyStorageInteractor: HistoryStorageInteractor
    private lateinit var sharedPreferences: SharedPreferences

    // Элементы пользовательского интерфейса
    private lateinit var searchField: EditText
    private lateinit var recyclerViewSearchHistory: RecyclerView
    private lateinit var searchInputLayout: LinearLayout
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    private val debounceTimer = Handler(Looper.getMainLooper())
    private lateinit var progressBar: ProgressBar


    // Enum для управления состоянием поиска
    enum class SearchViewState {
        NO_INTERNET,
        NO_RESULTS,
        HAS_RESULTS,
        HISTORY,
        EMPTY
    }

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable = Runnable { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Инициализация ProgressBar
        progressBar = findViewById(R.id.progressBar)



        // Инициализация SharedPreferences и SearchHistory
        sharedPreferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        historyStorageInteractor = HistoryStorageInteractorImpl(sharedPreferences)

        // Инициализация элементов пользовательского интерфейса
        recyclerViewSearchHistory = findViewById(R.id.recyclerViewSearchHistory)
        searchInputLayout = findViewById(R.id.search_input_layout)
        searchHistoryAdapter = SearchHistoryAdapter(historyStorageInteractor.getHistory())
        recyclerViewSearchHistory.adapter = searchHistoryAdapter

        tracksAdapter = TracksAdapter(this)

        // Начальная настройка видимости истории поиска
        searchInputLayout.visibility = View.GONE

        searchField = findViewById(R.id.searchField)

        // Инициализация кнопок и слушателей
        initBackButton()
        val clearButton = initClearButton()



        // Настройка TextWatcher для отслеживания изменений в поле поиска
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

        // Установка TextWatcher
        searchField.addTextChangedListener(simpleTextWatcher)

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Удаление предыдущего запланированного поиска
                handler.removeCallbacks(searchRunnable)

                // Планирование нового поиска с задержкой
                searchRunnable = Runnable {
                    val searchTerm = s.toString()
                    if (searchTerm.isNotEmpty()) {
                        searchTracks(searchTerm)
                        updateContainersVisibility()
                    }
                }
                handler.postDelayed(searchRunnable, 300) // Задержка в 300 миллисекунд
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Настройка слушателя для клавиши "Enter" на клавиатуре
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Вызов функции поиска при нажатии на клавишу "Enter"
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

        // Инициализация RecyclerView для отображения найденных треков
        val itemsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        tracksAdapter = TracksAdapter(this@SearchActivity)
        itemsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemsRecyclerView.adapter = tracksAdapter

        // Настройка слушателя клика на элемент списка
        tracksAdapter.setOnItemClickListener(object : TracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                // Добавление трека в историю поиска с использованием debounce
                debounceTimer.removeCallbacksAndMessages(null)
                debounceTimer.postDelayed({
                    // Обновление RecyclerView для истории поиска
                    searchHistoryAdapter.updateItems(historyStorageInteractor.getHistory())

                    // Добавление трека в историю поиска
                    historyStorageInteractor.addTrackToHistory(track)

                    // После добавления элемента в историю, обновление отображения истории
                    updateSearchHistoryView()
                }, 300) // Задержка в 300 миллисекунд
            }
        })

        // Обработка фокуса на поле поиска и отображение истории
        searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && historyStorageInteractor.getHistory().isNotEmpty()) {
                searchInputLayout.visibility = View.VISIBLE
            }
        }

        // Настройка кнопки очистки истории поиска
        val clearSearchHistoryButton: RelativeLayout = findViewById(R.id.clearSearchHistoryButton)
        clearSearchHistoryButton.setOnClickListener {
            historyStorageInteractor.clearHistory()
            val emptyDataList: List<Track> = ArrayList()
            searchHistoryAdapter.updateItems(emptyDataList)
            currentViewState = SearchViewState.EMPTY
            updateContainersVisibility()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Удаление запланированного поиска при уничтожении активности
        handler.removeCallbacks(searchRunnable)
    }

    // Инициализация кнопки "назад"
    private fun initBackButton() {
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    // Инициализация кнопки очистки поля поиска
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

    // Сохранение состояния активности при изменении конфигурации
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(REQUEST_TEXT, valueInSearchString)
    }

    // Восстановление состояния активности после изменения конфигурации
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        valueInSearchString = savedInstanceState.getString(REQUEST_TEXT, "")
        searchField.text = Editable.Factory.getInstance().newEditable(valueInSearchString)
    }

    // Определение видимости кнопки очистки поля поиска
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    // Функция поиска треков
    private fun searchTracks(searchTerm: String) {
        Log.e("mylog", "Start searching for term: $searchTerm")

        // Показать ProgressBar при начале поиска
        progressBar.visibility = View.VISIBLE

        // Изменение состояния поиска на "есть результаты"
        currentViewState = SearchViewState.HAS_RESULTS
        updateContainersVisibility()

        // Проверка наличия интернет-соединения
        val internetConnection = hasInternetConnection()

        if (!hasInternetConnection()) {
            updateNoNetworkMessageVisibility(internetConnection)
        }

        // Настройка Retrofit для обращения к iTunes API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iTunesSearchInteractor = retrofit.create(ITunesSearchInteractor::class.java)

        val mediaType = "music"
        val call = iTunesSearchInteractor.searchTracks(searchTerm, mediaType)

        // Обработка ответа от сервера
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
                // Скрыть ProgressBar
                progressBar.visibility = View.GONE
                // Обновить видимость контейнеров
                updateContainersVisibility()
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                currentViewState = SearchViewState.NO_INTERNET
                updateContainersVisibility()
            }
        })
        updateContainersVisibility()
    }

    // Обновление отображения истории поиска
    private fun updateSearchHistoryView() {
        val historyItems = historyStorageInteractor.getHistory()
        searchHistoryAdapter.updateItems(historyItems)
        recyclerViewSearchHistory.visibility = View.VISIBLE
    }

    // Проверка наличия интернет-соединения
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

    // Обновление видимости сообщения об отсутствии интернет-соединения
    private fun updateNoNetworkMessageVisibility(hasConnection: Boolean) {
        val communicationProblems = findViewById<FrameLayout>(R.id.communicationProblems)
        if (hasConnection) {
            communicationProblems.visibility = View.GONE
        } else {
            communicationProblems.visibility = View.VISIBLE
        }
    }

    // Обновление видимости контейнеров в зависимости от состояния поиска
    private fun updateContainersVisibility() {
        val noInternetContainer = findViewById<FrameLayout>(R.id.communicationProblems)
        val noResultsContainer = findViewById<FrameLayout>(R.id.noSearchResults)
        val resultsContainer = findViewById<RecyclerView>(R.id.recyclerView)

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
