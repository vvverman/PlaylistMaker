package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView



class ItemsAdapter(
    private val items: ArrayList<Item>

) : RecyclerView.Adapter<ItemsViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }

}

