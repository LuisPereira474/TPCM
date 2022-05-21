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

class Perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this@Perfil, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@Perfil, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@Perfil, AddBoleiaSemHist::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@Perfil, AddBoleiaSemHist::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun EditProfile(view: View) {
        val intent = Intent(this@Perfil, EditarPerfil::class.java)
        startActivity(intent)
    }
}