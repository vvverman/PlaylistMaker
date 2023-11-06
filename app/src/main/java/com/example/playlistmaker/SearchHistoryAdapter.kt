package com.example.playlistmaker

import TracksViewHolder
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryAdapter(
    private var searchHistory: List<Track>
) : RecyclerView.Adapter<TracksViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(track: Track)
    }

    private var listener: OnItemClickListener? = null

    // Метод для установки слушателя кликов
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return TracksViewHolder(view)
    }

    // Функция для преобразования миллисекунд в минуты:секунды
    private fun formatDuration(durationInMillis: Long): String {
        val minutes = durationInMillis / 1000 / 60
        val seconds = durationInMillis /1000 % 60
        return "${minutes}:${seconds}"
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val track = searchHistory[position]

        // Устанавливаем слушатель клика на элемент списка
        holder.itemView.setOnClickListener {
            // Создаем интент для перехода на экран "Аудиоплеер"
            val intent = Intent(holder.itemView.context, MediaLibraryActivity::class.java)

            val coverImageURL = track.coverImageURL.replaceAfterLast("/","512x512bb.jpg", )


            // Передаем данные о треке в новую активность
            intent.putExtra("trackName", track.compositionName)
            intent.putExtra("artistName", track.artistName)
            intent.putExtra("collectionName", track.albumName)
            intent.putExtra("releaseDate", track.releaseDate)
            intent.putExtra("primaryGenreName", track.genre)
            intent.putExtra("country", track.country)
            intent.putExtra("trackTimeMills", formatDuration(track.durationInMillis))
            intent.putExtra("coverImageURL", coverImageURL)

            // Запускаем активность "Аудиоплеер"
            holder.itemView.context.startActivity(intent)
        }

        // Вызываем метод bind() ViewHolder'а для отображения данных элемента списка
        holder.bind(track)

    }

    override fun getItemCount(): Int {
        return searchHistory.size
    }

    fun updateItems(newSearchHistory: List<Track>) {
        searchHistory = newSearchHistory
        notifyDataSetChanged()
    }

}