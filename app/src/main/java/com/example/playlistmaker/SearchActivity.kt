package com.example.playlistmaker

import ITunesSearchApi
import ItemsAdapter
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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    // Переменные для хранения состояния и данных
    private var valueInSearchString = ""
    private var currentViewState = SearchViewState.NO_INTERNET
    private lateinit var searchHistory: SearchHistory
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchField: EditText
    private lateinit var clearSearchHistoryButton: ImageButton
    private lateinit var recyclerViewSearchHistory: RecyclerView

    // Перечисление для состояний поиска
    enum class SearchViewState {
        NO_INTERNET, // Нет интернет-соединения
        NO_RESULTS,  // Нет результатов поиска
        HAS_RESULTS  // Есть результаты поиска
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Инициализация элементов интерфейса и обработчиков событий

        // Поле ввода текста для поиска
        searchField = findViewById(R.id.searchField)

        // Кнопка "Назад"
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // Обработчик фокуса на поле поиска
        searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val searchInputLayoutInclude = findViewById<View>(R.id.search_input_layout_include)
                searchInputLayoutInclude.visibility = View.VISIBLE
            }
        }

        // Кнопка "Очистить" в поле поиска
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            searchField.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
        }

        // Обработчик изменения текста в поле поиска
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Перед изменением текста
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Во время изменения текста
                valueInSearchString = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                // Вызов метода для выполнения поиска музыкальных треков
                searchTracks(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // После изменения текста
            }
        }
        searchField.addTextChangedListener(simpleTextWatcher)

        // Настройка RecyclerView для отображения результатов поиска
        val itemsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val itemsAdapter = ItemsAdapter()
        itemsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemsRecyclerView.adapter = itemsAdapter

        // Инициализация SharedPreferences для хранения истории поиска
        sharedPreferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)

        // Создание экземпляра SearchHistory для управления историей поиска
        searchHistory = SearchHistory(sharedPreferences)

        // Кнопка "Очистить историю поиска"
        clearSearchHistoryButton = findViewById(R.id.clearSearchHistoryButton)
        recyclerViewSearchHistory = findViewById(R.id.recyclerViewSearchHistory)

        // Обработчик клика по кнопке "Очистить историю поиска"
        clearSearchHistoryButton.setOnClickListener {
            // Вызов метода для очистки истории поиска
                searchHistory.clearHistory()

            // Создайте пустой список данных, так как история очищена
            val emptyDataList: List<Item> = ArrayList()

            // Получаем адаптер для recyclerViewSearchHistory (в данном случае, это ItemsAdapter)
            val adapter = recyclerViewSearchHistory.adapter as? ItemsAdapter

            // Обновляем данные адаптера пустым списком
            adapter?.updateItems(emptyDataList)
        }
    }

    companion object {
        const val REQUEST_TEXT = "REQUEST_TEXT"
    }

    // Метод для сохранения состояния активности при повороте экрана и т. д.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(REQUEST_TEXT, valueInSearchString)
    }

    // Метод для восстановления состояния активности после поворота экрана и т. д.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        valueInSearchString = savedInstanceState.getString(REQUEST_TEXT, "")
        searchField.text = Editable.Factory.getInstance().newEditable(valueInSearchString)
    }

    // Метод для определения видимости кнопки "Очистить" в поле поиска
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    // Метод для выполнения поиска музыкальных треков
    private fun searchTracks(searchTerm: String) {
        // Логирование начала поиска
        Log.e("mylog", "Start searching for term: $searchTerm")

        // Проверка наличия интернет-соединения
        val internetConnection = hasInternetConnection()

        // Обновление видимости сообщения о проблемах с сетью
        updateNoNetworkMessageVisibility(internetConnection)

        if (internetConnection) {
            // Создание экземпляра Retrofit для работы с API iTunes Search
            val retrofit = Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/") // Базовый URL API iTunes Search
                .addConverterFactory(GsonConverterFactory.create()) // Используем Gson для сериализации/десериализации
                .build()

            // Создание объекта ITunesSearchApi с использованием экземпляра Retrofit
            val iTunesSearchApi = retrofit.create(ITunesSearchApi::class.java)

            // Вызов метода поиска музыкальных треков
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
                            val itemsAdapter =
                                findViewById<RecyclerView>(R.id.recyclerView).adapter as? ItemsAdapter
                            itemsAdapter?.updateItems(items)
                            currentViewState = if (items.isEmpty()) {
                                SearchViewState.NO_RESULTS
                            } else {
                                SearchViewState.HAS_RESULTS
                            }

                            // Добавление поискового запроса в историю после получения результатов
                            if (currentViewState == SearchViewState.HAS_RESULTS) {
                                val item = Item(
                                    itemId = 0, // Можете установить его на 0 или любой уникальный идентификатор для поискового запроса
                                    compositionName = searchTerm,
                                    artistName = "",
                                    durationInMillis = 0,
                                    coverImageURL = ""
                                )
                                searchHistory.addItemToHistory(item)
                            }
                        }
                    } else {
                        currentViewState = SearchViewState.NO_RESULTS
                    }
                    // Обновление видимости контейнеров в зависимости от состояния поиска
                    updateContainersVisibility()
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    // Обработка ошибки при неудачном запросе
                    currentViewState = SearchViewState.NO_INTERNET
                    // Обновление видимости контейнеров при ошибке
                    updateContainersVisibility()
                }
            })
        }
    }

    // Метод для проверки наличия интернет-соединения
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

    // Метод для обновления видимости сообщения о проблемах с сетью
    private fun updateNoNetworkMessageVisibility(hasConnection: Boolean) {
        val communicationProblems = findViewById<FrameLayout>(R.id.communicationProblems)
        if (hasConnection) {
            communicationProblems.visibility = View.GONE
        } else {
            communicationProblems.visibility = View.VISIBLE
        }
    }

    // Метод для обновления видимости контейнеров в зависимости от состояния поиска
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
