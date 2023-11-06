package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Класс SearchHistory предназначен для управления историей поисковых запросов.
 *
 * @property sharedPreferences Используется для доступа к хранилищу SharedPreferences.
 */
class SearchHistory(private val sharedPreferences: SharedPreferences) {

    // Создаем объект Gson для сериализации и десериализации данных.
    private val gson = Gson()

    // Ключ, по которому будет сохраняться история в SharedPreferences.
    private val historyKey = "search_history"

    /**
     * Метод для добавления элемента в историю поиска.
     *
     * @param track Элемент типа Item, который будет добавлен в историю.
     */
    fun addTrackToHistory(track: Track) {
        // Получаем текущую историю поиска.
        val history = getHistory()

        // Удаляем элемент с таким же идентификатором (дубликат), если такой уже существует.
        history.removeIf {it == track}

        // Добавляем новый элемент в начало списка.
        history.add(0, track)

        // Если история стала длиннее 10 элементов, удаляем последний элемент.
        if (history.size > 10) {
            history.removeAt(history.size - 1)
        }

        // Сохраняем обновленную историю.
        saveHistory(history)
    }

    /**
     * Метод для получения текущей истории поиска.
     *
     * @return Список элементов типа Item, представляющих историю поиска.
     */
    fun getHistory(): MutableList<Track> {
        // Получаем историю в виде JSON-строки из SharedPreferences.
        val historyString = sharedPreferences.getString(historyKey, "")

        return if (historyString.isNullOrEmpty()) {
            // Если история пуста или отсутствует, возвращаем пустой список.
            mutableListOf()
        } else {
            // Десериализуем JSON-строку в список элементов Item с использованием TypeToken.
            gson.fromJson(historyString, object : TypeToken<MutableList<Track>>() {}.type)
        }
    }

    /**
     * Метод для очистки истории поиска.
     */
    fun clearHistory() {
        // Удаляем историю из SharedPreferences.
        sharedPreferences.edit().remove(historyKey).apply()
    }

    /**
     * Метод для сохранения обновленной истории в SharedPreferences.
     *
     * @param history Список элементов типа Item, представляющих обновленную историю поиска.
     */
    private fun saveHistory(history: MutableList<Track>) {
        // Сериализуем список элементов в JSON-строку.
        val historyString = gson.toJson(history)

        // Сохраняем JSON-строку в SharedPreferences.
        sharedPreferences.edit().putString(historyKey, historyString).apply()
    }
}