package com.example.tpcm.aplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcm.R

class AddBoleiaSemHist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_boleia_sem_hist)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this@AddBoleiaSemHist, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@AddBoleiaSemHist, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                true
            }
            R.id.nav_profile -> {
                Log.d("teste", "entrou")
                // Respond to navigation item 2 click
                true
            }
            else -> {
                Log.d("teste", "entrou")
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun addRide(view: View) {
        val intent = Intent(this@AddBoleiaSemHist, CriarBoleia::class.java)
        startActivity(intent)
    }
}