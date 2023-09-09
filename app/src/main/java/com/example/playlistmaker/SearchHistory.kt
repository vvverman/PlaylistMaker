package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.playlistmaker.Item


class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()
    private val historyKey = "search_history"

    fun addItemToHistory(item: Item) {
        val history = getHistory()
        history.removeIf { it.itemId == item.itemId }
        history.add(0, item)
        if (history.size > 10) {
            history.removeAt(history.size - 1)
        }
        saveHistory(history)
    }

    fun getHistory(): MutableList<Item> {
        val historyString = sharedPreferences.getString(historyKey, "")
        return if (historyString.isNullOrEmpty()) {
            mutableListOf()
        } else {
            gson.fromJson(historyString, object : TypeToken<MutableList<Item>>() {}.type)
        }
    }

    fun clearHistory() {
        sharedPreferences.edit().remove(historyKey).apply()
    }

    private fun saveHistory(history: MutableList<Item>) {
        val historyString = gson.toJson(history)
        sharedPreferences.edit().putString(historyKey, historyString).apply()
    }
}
