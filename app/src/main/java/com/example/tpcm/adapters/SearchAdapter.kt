package com.example.tpcm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpcm.R
import com.example.tpcm.models.Search
import kotlinx.android.synthetic.main.search_line.view.*


class SearchAdapter(
    private val linhas: ArrayList<Search>
) : RecyclerView.Adapter<SearchViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(idBoleia: TextView)
        fun onWishlistClick(idBoleia: TextView)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.search_line,
                    parent,
                    false
                ),
            mListener
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentLinha = linhas[position]
        holder.fromTo.text = currentLinha.fromTo
        holder.date.text = currentLinha.date
        holder.price.text = currentLinha.price
        holder.nomeCriador.text = currentLinha.nomeCriador
        holder.idBoleia.text = currentLinha.idBoleia
    }

    override fun getItemCount(): Int {
        return linhas.size
    }
}

class SearchViewHolder(
    itemView: View,
    listener: SearchAdapter.onItemClickListener
) : RecyclerView.ViewHolder(itemView) {
    val fromTo: TextView = itemView.fromTo
    val date: TextView = itemView.date
    val price: TextView = itemView.price
    val nomeCriador: TextView = itemView.nomeCriador
    val idBoleia: TextView = itemView.idBoleia

    init {
        itemView.confirmButton.setOnClickListener{
            listener.onItemClick(idBoleia)
        }
        itemView.wishList.setOnClickListener{
            listener.onWishlistClick(idBoleia)
        }
    }
}