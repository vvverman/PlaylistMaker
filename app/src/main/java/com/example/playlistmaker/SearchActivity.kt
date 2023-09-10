package com.example.playlistmaker

import ITunesSearchApi
import ItemsAdapter
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    // Переменная для хранения строки поиска
    private var valueInSearchString = ""

    // Перечисление для состояния поиска
    private var currentViewState = SearchViewState.NO_INTERNET

    // Переменные для управления историей поиска
    private lateinit var searchHistory: SearchHistory
    private lateinit var sharedPreferences: SharedPreferences

    // Элементы пользовательского интерфейса
    private lateinit var searchField: EditText
    private lateinit var searchHint: TextView

    // Перечисление для состояния поиска
    enum class SearchViewState {
        NO_INTERNET,
        NO_RESULTS,
        HAS_RESULTS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Находим элементы по их идентификаторам
        searchField = findViewById(R.id.searchField)
        searchHint = findViewById(R.id.searchHint)

        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Обработчик клика по кнопке "назад"
        backButton.setOnClickListener {
            finish()
        }

        searchField.setOnClickListener {
            findViewById<View>(R.id.search_input_layout).visibility = View.VISIBLE
        }

        val inputEditTextView = findViewById<EditText>(R.id.inputEditTextView)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        // Обработчик клика по кнопке "очистить"
        clearButton.setOnClickListener {
            inputEditTextView.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditTextView.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Перед изменением текста
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Во время изменения текста
                valueInSearchString = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                // Вызов метода поиска музыкальных треков
                searchTracks(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // После изменения текста
            }
        }
        inputEditTextView.addTextChangedListener(simpleTextWatcher)

        val itemsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val itemsAdapter = ItemsAdapter()
        itemsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemsRecyclerView.adapter = itemsAdapter

        // Инициализируйте SharedPreferences для хранения истории поиска
        sharedPreferences = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)

        // Создайте экземпляр SearchHistory для управления историей поиска
        searchHistory = SearchHistory(sharedPreferences)
    }

    companion object {
        const val REQUEST_TEXT = "REQUEST_TEXT"
    }

    // Метод для сохранения состояния активности
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(REQUEST_TEXT, valueInSearchString)
    }

    // Метод для восстановления состояния активности
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        valueInSearchString = savedInstanceState.getString(REQUEST_TEXT, "")
        val inputEditTextView = findViewById<EditText>(R.id.inputEditTextView)
        inputEditTextView.text = Editable.Factory.getInstance().newEditable(valueInSearchString)
    }

    // Метод для определения видимости кнопки "очистить"
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    // Метод для выполнения поиска музыкальных треков
    private fun searchTracks(searchTerm: String) {

        // Логируем
        Log.e("mylog", "Start searching for term: $searchTerm")

        // Проверяем наличие интернет-соединения
        val internetConnection = hasInternetConnection()

        // При отсутствии интернет-соединения показываем сообщение о проблемах с сетью
        updateNoNetworkMessageVisibility(internetConnection)

        if (internetConnection) {
            // Создание экземпляра Retrofit
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
                override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                    if (response.isSuccessful) {
                        val tracksResponse = response.body()
                        tracksResponse?.items?.let { items ->
                            val itemsAdapter = findViewById<RecyclerView>(R.id.recyclerView).adapter as? ItemsAdapter
                            itemsAdapter?.updateItems(items)
                            currentViewState = if (items.isEmpty()) {
                                SearchViewState.NO_RESULTS
                            } else {

                                // Логируем
                                Log.e("mylog", "Response failed. Error code: ${response.code()}, message: ${response.message()}")

                                SearchViewState.HAS_RESULTS
                            }

                            // Добавьте поисковый запрос в историю при получении результатов
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
                    updateContainersVisibility()
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    //Логируем
                    Log.e("mylog", "faile $t")

                    currentViewState = SearchViewState.NO_INTERNET
                    updateContainersVisibility()
                }
            })
        }
    }

    // Метод для проверки наличия интернет-соединения
    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        // Логируем
        Log.e("mylog", "Internet connection status: $connectivityManager")

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                }
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo != null && networkInfo.isConnected
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
        //Логируем
        Log.e("mylog", "state: $currentViewState")

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
