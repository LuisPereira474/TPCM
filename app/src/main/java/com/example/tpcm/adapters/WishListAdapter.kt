package com.example.tpcm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpcm.R
import com.example.tpcm.models.Wishlist
import kotlinx.android.synthetic.main.wish_list_line.view.*


class WishListAdapter(
    private val linhas: ArrayList<Wishlist>

) : RecyclerView.Adapter<WishListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        return WishListViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.search_line,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        val currentLinha = linhas[position]
        holder.fromToWishList.text = currentLinha.fromToWishList
        holder.dateWishList.text = currentLinha.dateWishList
        holder.priceWishList.text = currentLinha.priceWishList
        holder.nomeCriadorWishList.text = currentLinha.nomeCriadorWishList
        holder.idBoleiaWishList.text = currentLinha.idBoleiaWishList
    }

    override fun getItemCount(): Int {
        return linhas.size
    }
}

class WishListViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    val fromToWishList: TextView = itemView.fromToWishList
    val dateWishList: TextView = itemView.dateWishList
    val priceWishList: TextView = itemView.priceWishList
    val nomeCriadorWishList: TextView = itemView.nomeCriadorWishList
    val idBoleiaWishList: TextView = itemView.idBoleiaWishList

}