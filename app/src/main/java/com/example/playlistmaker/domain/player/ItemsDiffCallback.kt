package com.example.playlistmaker.domain.player

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.player.model.Track

// Класс ItemsDiffCallback используется для вычисления разницы между старым и новым списками элементов
class ItemsDiffCallback(
    private val oldList: List<Track>,
    private val newList: List<Track>
) : DiffUtil.Callback() {

    // Метод возвращает размер старого списка
    override fun getOldListSize(): Int {
        return oldList.size
    }

    // Метод возвращает размер нового списка
    override fun getNewListSize(): Int {
        return newList.size
    }

    // Метод проверяет, являются ли элементы с одной и той же позицией одинаковыми в старом и новом списках
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    // Метод проверяет, являются ли содержимое элементов с одной и той же позицией одинаковыми в старом и новом списках
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}