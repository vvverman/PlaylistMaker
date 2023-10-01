package com.example.playlistmaker

import TracksViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

// Класс ItemsAdapter является адаптером для RecyclerView и отвечает за отображение элементов списка
class TracksAdapter : RecyclerView.Adapter<TracksViewHolder>() {

    // Список элементов, которые будут отображаться в RecyclerView
    private val itemsList = ArrayList<Track>()

    // Интерфейс для обработки события клика на элемент списка
    interface OnItemClickListener {
        fun onItemClick(track: Track)
    }

    // Переменная для хранения слушателя кликов
    private var listener: OnItemClickListener? = null

    // Метод для установки слушателя кликов
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // Метод для обновления списка элементов в адаптере с использованием DiffUtil
    fun updateTracks(newTracks: List<Track>) {
        // Рассчитываем разницу между старым и новым списками элементов
        val diffResult = DiffUtil.calculateDiff(ItemsDiffCallback(itemsList, newTracks))

        // Очищаем текущий список и добавляем новые элементы
        itemsList.clear()
        itemsList.addAll(newTracks)

        // Применяем разницу к RecyclerView, чтобы обновить его
        diffResult.dispatchUpdatesTo(this)
    }

    // Метод вызывается для создания нового ViewHolder, который будет отображать элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        // Создаем View элемента списка из layout-ресурса "R.layout.item"
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)

        // Возвращаем созданный ViewHolder
        return TracksViewHolder(view)
    }

    // Метод вызывается для связывания данных элемента списка с ViewHolder
    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        // Получаем элемент списка по позиции
        val item = itemsList[position]

        // Устанавливаем слушатель клика на элемент списка
        holder.itemView.setOnClickListener {
            // Проверяем, что слушатель установлен и вызываем его
            listener?.onItemClick(item)
        }

        // Вызываем метод bind() ViewHolder'а для отображения данных элемента списка
        holder.bind(item)
    }

    // Метод возвращает общее количество элементов в списке
    override fun getItemCount(): Int {
        return itemsList.size
    }

    // Класс ItemsDiffCallback используется для вычисления разницы между старым и новым списками элементов
    private class ItemsDiffCallback(
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
}