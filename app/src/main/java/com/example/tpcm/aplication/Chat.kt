package com.example.tpcm.aplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val idBoleia = intent.getStringExtra(PARAM_ID).toString()

        btnFecharChat.setOnClickListener {
            finish()
        }
    }


    fun sendMessage(view: View) {
        val etmensagem = findViewById<EditText>(R.id.chat_input)
        val mensagem = etmensagem.text.toString()
        val idUser = getSharedPreferences("idUser", MODE_PRIVATE).getString("idUser","").toString()
        val idBoleia = intent.getStringExtra(PARAM_ID).toString()

        if (mensagem == "") {
            Toast.makeText(this@Chat, "Can´t send empty messages", Toast.LENGTH_SHORT).show()
        } else {
            GlobalScope.launch {
                if (Connection.sendMessage(idUser, idBoleia, mensagem) == 1) {
                    Toast.makeText(this@Chat, "Error sending message", Toast.LENGTH_SHORT)
                }
            }
            etmensagem.setText("")
        }
    }

}