package com.example.tpcm.adapters

import android.R.attr.button
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.example.tpcm.R
import com.example.tpcm.models.Message
import kotlinx.android.synthetic.main.chat_item_left.view.*
import kotlinx.android.synthetic.main.dialog_help_homepage.view.*
import kotlinx.android.synthetic.main.search_line.view.*


class ChatAdapter(
    private val linhas: ArrayList<Message>,
    private val idUser: String,
    private val drawable: Drawable?
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {


        return MessageViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.chat_item_left,
                    parent,
                    false
                ),
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentLinha = linhas[position]

        if(currentLinha.idUser.equals(idUser)){

            holder.displayMessage.background = drawable


        }

        holder.nameUser.text = currentLinha.nameUser
        holder.message.text = currentLinha.message
    }

    override fun getItemCount(): Int {
        return linhas.size
    }


    class MessageViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        val nameUser: TextView = itemView.chat_username
        val message: TextView = itemView.chat_message
        val displayMessage: LinearLayout = itemView.displayMessage
    }
}


