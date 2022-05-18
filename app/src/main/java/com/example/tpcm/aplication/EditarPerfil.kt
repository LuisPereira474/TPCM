package com.example.tpcm.aplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.example.tpcm.R

class EditarPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)
    }


    fun GoProfile(view: View) {
        val intent = Intent(this@EditarPerfil, Perfil::class.java)
        startActivity(intent)
    }
}