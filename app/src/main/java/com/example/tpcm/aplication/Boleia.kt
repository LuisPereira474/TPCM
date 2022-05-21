package com.example.tpcm.aplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.tpcm.R

class Boleia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boleia)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this@Boleia, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@Boleia, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@Boleia, AddBoleiaSemHist::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@Boleia, Perfil::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}