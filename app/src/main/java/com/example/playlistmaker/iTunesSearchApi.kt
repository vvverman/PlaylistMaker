import com.example.playlistmaker.Item
import com.example.playlistmaker.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


// Определение интерфейса для iTunes Search API
interface ITunesSearchApi {
    @GET("/search")
    fun searchTracks(@Query("term") term: String, @Query("media") media: String): Call<TracksResponse>
}

// Метод для создания плейлиста на основе полученных треков
fun createPlaylist(items: List<Item>) {
    println("Your Playlist:")
    for (item in items) {
        println("${item.compositionName} - ${item.artistName} (${item.durationInMillis})")
        println("Cover Image URL: ${item.coverImageURL}")
        println("----------------------------------")
    }
}

fun main() {
    // Создание экземпляра Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com") // Базовый URL API iTunes Search
        .addConverterFactory(GsonConverterFactory.create()) // Используем Gson для сериализации/десериализации
        .build()

    // Создание объекта ITunesSearchApi с использованием экземпляра Retrofit
    val iTunesSearchApi = retrofit.create(ITunesSearchApi::class.java)

    // Вызов метода поиска музыкальных треков
    val searchTerm = "track"
    val mediaType = "music"
    val call = iTunesSearchApi.searchTracks(searchTerm, mediaType)

    // Выполнение запроса асинхронно
    call.enqueue(object : Callback<TracksResponse> {
        override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
            if (response.isSuccessful) {
                val tracksResponse = response.body()
                tracksResponse?.items?.let { items ->
                    // Заполняем список items данными из API
                    val itemList = ArrayList<Item>()
                    for (item in items) {
                        // Выполнение расчетов и форматирования для durationInMillis
                        val minutes = item.durationInMillis / 1000 / 60
                        val seconds = (item.durationInMillis / 1000) % 60
                        val durationText = String.format("%02d:%02d", minutes, seconds)

                        // Создание объекта Item с учетом расчитанных значений
                        val newItem = Item(
                            itemId = 0,
                            compositionName = item.compositionName,
                            artistName = item.artistName,
                            durationInMillis = item.durationInMillis,
                            coverImageURL = item.coverImageURL
                        )

                        itemList.add(newItem)
                    }

                    // Теперь itemList содержит данные из API, вызываем метод для создания плейлиста на основе этих данных.
                    createPlaylist(itemList)
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
