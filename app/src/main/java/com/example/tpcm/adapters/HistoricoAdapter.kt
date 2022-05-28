package com.example.tpcm.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpcm.R
import com.example.tpcm.models.Historico
import kotlinx.android.synthetic.main.historico_line.view.*
import kotlinx.android.synthetic.main.historico_line.view.date
import kotlinx.android.synthetic.main.historico_line.view.fromTo
import kotlinx.android.synthetic.main.historico_line.view.idBoleia
import kotlinx.android.synthetic.main.search_line.view.*

class HistoricoAdapter(
    private val linhas: ArrayList<Historico>
) : RecyclerView.Adapter<HistoricoViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(idBoleia: TextView)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        return HistoricoViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.historico_line,
                    parent,
                    false
                ),
            mListener
        )
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
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

class HistoricoViewHolder(
    itemView: View,
    listener: HistoricoAdapter.onItemClickListener
) : RecyclerView.ViewHolder(itemView) {
    val fromTo: TextView = itemView.fromTo
    val date: TextView = itemView.date
    val flag: TextView = itemView.flag
    val idBoleia: TextView = itemView.idBoleia

    init {
        itemView.setOnClickListener{
            listener.onItemClick(idBoleia)
        }
    }
}