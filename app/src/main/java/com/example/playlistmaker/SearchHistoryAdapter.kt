package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchHistoryAdapter(private var searchHistory: List<Item>) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageURL: ImageView = itemView.findViewById(R.id.coverImageURL)
        val compositionName: TextView = itemView.findViewById(R.id.compositionName)
        val artistName: TextView = itemView.findViewById(R.id.artistName)
        var duration: TextView = itemView.findViewById(R.id.duration)
        val vector: View = itemView.findViewById(R.id.vector)
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchItem = searchHistory[position]

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(searchItem)
        }


        Glide.with(holder.itemView)
            .load(searchItem.coverImageURL)
            .into(holder.coverImageURL)

        holder.compositionName.text = searchItem.compositionName
        holder.artistName.text = searchItem.artistName
        holder.duration.text = searchItem.durationInMillis.toString() // Преобразовано значение duration в String

//        holder.ellipse.visibility = View.GONE
//        holder.vector.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return searchHistory.size
    }

    fun updateItems(newSearchHistory: List<Item>) {
        searchHistory = newSearchHistory
        notifyDataSetChanged()
    }
}
