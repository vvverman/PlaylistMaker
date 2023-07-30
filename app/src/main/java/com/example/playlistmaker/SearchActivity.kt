package com.example.playlistmaker

import ITunesSearchApi
import ItemsAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var inputEditText = ""
    var valueInSearchString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                valueInSearchString = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                // Вызов метода поиска музыкальных треков
                searchTracks(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        val itemsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val itemsAdapter = ItemsAdapter()
        itemsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemsRecyclerView.adapter = itemsAdapter
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
        inputEditText = valueInSearchString

        Log.d("REQUEST_TEXT", "valueInSearchString before $valueInSearchString")
        valueInSearchString = savedInstanceState.getString(REQUEST_TEXT, "")
        Log.d("REQUEST_TEXT", "valueInSearchString after $valueInSearchString")
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchTracks(searchTerm: String) {
        // Проверяем наличие интернет-соединения
        val internetConnection = hasInternetConnection()

        // При отсутствии интернет-соединения показываем сообщение о проблемах с сетью
        updateNoNetworkMessageVisibility(internetConnection)

        if (internetConnection) {
            // Создание экземпляра Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl("https://itunes.apple.com") // Базовый URL API iTunes Search
                .addConverterFactory(GsonConverterFactory.create()) // Используем Gson для сериализации/десериализации
                .build()

            // Создание объекта ITunesSearchApi с использованием экземпляра Retrofit
            val iTunesSearchApi = retrofit.create(ITunesSearchApi::class.java)

            // Вызов метода поиска музыкальных треков
            val mediaType = "music"
            val call = iTunesSearchApi.searchTracks(searchTerm, mediaType)

            // Выполнение запроса асинхронно
            call.enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                    if (response.isSuccessful) {
                        val tracksResponse = response.body()
                        tracksResponse?.items?.let { items ->
                            // Обновите адаптер вашего RecyclerView с новыми данными из API
                            val itemsAdapter = findViewById<RecyclerView>(R.id.recyclerView).adapter as? ItemsAdapter
                            itemsAdapter?.updateItems(items)
                            // Проверка наличия результатов и показ/скрытие плейсхолдера
                            val placeholderImageView = findViewById<FrameLayout>(R.id.noSearchResults)
                            val communicationProblems = findViewById<FrameLayout>(R.id.communicationProblems)

                            if (items.isEmpty()) {
                                // Показываем плейсхолдер, если результаты пусты
                                placeholderImageView.visibility = View.VISIBLE
                            } else {
                                // Скрываем плейсхолдер, если есть результаты
                                placeholderImageView.visibility = View.GONE
                            }
                        }
                    } else {
                        // Обработка ошибки при запросе к API
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    // Обработка ошибки при сбое запроса к API
                }
            })
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
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

    private fun updateNoNetworkMessageVisibility(hasConnection: Boolean) {
        val communicationProblems = findViewById<FrameLayout>(R.id.communicationProblems)
        communicationProblems.visibility = if (hasConnection) View.GONE else View.VISIBLE
    }
}
