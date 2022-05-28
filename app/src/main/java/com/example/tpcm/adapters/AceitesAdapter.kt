package com.example.tpcm.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpcm.R
import com.example.tpcm.models.Aceites
import kotlinx.android.synthetic.main.boleias_aceites_line.view.*


class AceitesAdapter(
    private val linhas: ArrayList<Aceites>
) : RecyclerView.Adapter<AceitesViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(idBoleia: TextView)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AceitesViewHolder {
        return AceitesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.boleias_aceites_line,
                    parent,
                    false
                ),
            mListener
        )
    }

    override fun onBindViewHolder(holder: AceitesViewHolder, position: Int) {
        val currentLinha = linhas[position]
        val green: Int = Color.parseColor("#54C000")
        val red: Int = Color.parseColor("#FF3939")

        holder.fromTo.text = currentLinha.fromTo
        holder.date.text = currentLinha.date
        if (currentLinha.flag){
            holder.flag.setBackgroundColor(red)
        }else{
            holder.flag.setBackgroundColor(green)
        }
        holder.idBoleia.text = currentLinha.idBoleia
    }

    override fun getItemCount(): Int {
        return linhas.size
    }
}

class AceitesViewHolder(
    itemView: View,
    listener: AceitesAdapter.onItemClickListener
) : RecyclerView.ViewHolder(itemView) {
    val fromTo: TextView = itemView.fromToAceites
    val date: TextView = itemView.dateAceites
    val flag: TextView = itemView.flagAceites
    val idBoleia: TextView = itemView.idBoleiaAceites

    init {
        itemView.setOnClickListener{
            listener.onItemClick(idBoleia)
        }
    }
}