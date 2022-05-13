package com.example.tpcm.aplication

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
                Log.d("teste","entrou")
                // Respond to navigation item 2 click
                true
            }
            R.id.nav_rides -> {
                Log.d("teste","entrou")
                // Respond to navigation item 2 click
                true
            }
            R.id.nav_services -> {
                Log.d("teste","entrou")
                // Respond to navigation item 2 click
                true
            }
            R.id.nav_profile -> {
                Log.d("teste","entrou")
                // Respond to navigation item 2 click
                true
            }
            else -> {
                Log.d("teste","entrou")
                super.onOptionsItemSelected(item)
            }
        }
    }
}