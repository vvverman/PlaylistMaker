package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView



class ItemsAdapter(
    val items: ArrayList<Item>
//    private val onItemClickListener: AdapterView.OnItemClickListener
) : RecyclerView.Adapter<ItemsViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(items[position])
//        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(items[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

