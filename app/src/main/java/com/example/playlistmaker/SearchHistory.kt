package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Класс для управления историей поисковых запросов
class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson() // Используется для сериализации и десериализации данных
    private val historyKey = "search_history" // Ключ для сохранения истории в SharedPreferences

    // Метод для добавления элемента в историю поиска
    fun addItemToHistory(item: Item) {
        val history = getHistory() // Получаем текущую историю поиска
        history.removeIf { it.itemId == item.itemId } // Удаляем элемент с таким же идентификатором (дубликат)
        history.add(0, item) // Добавляем новый элемент в начало списка
        if (history.size > 10) {
            history.removeAt(history.size - 1) // Если история стала длиннее 10 элементов, удаляем последний элемент
        }
        saveHistory(history) // Сохраняем обновленную историю
    }

    // Метод для получения текущей истории поиска
    fun getHistory(): MutableList<Item> {
        val historyString = sharedPreferences.getString(
            historyKey,
            ""
        ) // Получаем историю в виде JSON-строки из SharedPreferences
        return if (historyString.isNullOrEmpty()) {
            mutableListOf() // Если история пуста или отсутствует, возвращаем пустой список
        } else {
            // Десериализуем JSON-строку в список элементов Item с использованием TypeToken
            gson.fromJson(historyString, object : TypeToken<MutableList<Item>>() {}.type)
        }
    }

    // Метод для очистки истории поиска
    fun clearHistory() {
        sharedPreferences.edit().remove(historyKey).apply() // Удаляем историю из SharedPreferences
    }

    // Метод для сохранения обновленной истории в SharedPreferences
    private fun saveHistory(history: MutableList<Item>) {
        val historyString = gson.toJson(history) // Сериализуем список элементов в JSON-строку
        sharedPreferences.edit().putString(historyKey, historyString)
            .apply() // Сохраняем JSON-строку в SharedPreferences
    }
}
