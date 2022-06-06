package com.example.tpcm.aplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*

class Chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val idUser = intent.getStringExtra(PARAM_ID_USER).toString()
        val idBoleia = intent.getStringExtra(PARAM_ID_USER).toString()

        btnFecharChat.setOnClickListener {
            finish()
        }



        btnsend.setOnClickListener {
            sendMessage()
        }


    }

    private fun sendMessage() {
        val text = findViewById<EditText>(R.id.chat_input).text.toString()

    }

}