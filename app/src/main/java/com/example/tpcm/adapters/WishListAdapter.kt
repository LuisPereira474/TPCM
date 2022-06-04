package com.example.tpcm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpcm.R
import com.example.tpcm.models.Wishlist
import kotlinx.android.synthetic.main.search_line.view.*
import kotlinx.android.synthetic.main.wish_list_line.view.*


class WishListAdapter(
    private val linhas: ArrayList<Wishlist>

) : RecyclerView.Adapter<WishListViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(idBoleia: TextView)
        fun onRemoveWishlistClick(idBoleia: TextView)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        return WishListViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.wish_list_line,
                    parent,
                    false
                ),
            mListener
        )
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        val currentLinha = linhas[position]
        holder.fromToWishList.text = currentLinha.fromToWishList
        holder.dateWishList.text = currentLinha.dateWishList
        holder.priceWishList.text = currentLinha.priceWishList
        holder.nomeCriadorWishList.text = currentLinha.nomeCriadorWishList
        holder.idBoleia.text = currentLinha.idBoleiaWishList
    }

    override fun getItemCount(): Int {
        return linhas.size
    }
}

class WishListViewHolder(
    itemView: View,
    listener: WishListAdapter.onItemClickListener
) : RecyclerView.ViewHolder(itemView) {
    val fromToWishList: TextView = itemView.fromToWishList
    val dateWishList: TextView = itemView.dateWishList
    val priceWishList: TextView = itemView.priceWishList
    val nomeCriadorWishList: TextView = itemView.nomeCriadorWishList
    val idBoleia: TextView = itemView.idBoleiaWishList

    init {
        itemView.acceptBolWishList.setOnClickListener{
            listener.onItemClick(idBoleia)
        }
        itemView.removeWishList.setOnClickListener{
            listener.onRemoveWishlistClick(idBoleia)
        }
    }
}