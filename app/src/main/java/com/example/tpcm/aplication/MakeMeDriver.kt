package com.example.tpcm.aplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import kotlinx.android.synthetic.main.activity_make_me_driver.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MakeMeDriver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_me_driver)
        goBackMakeMeDriver.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this@MakeMeDriver, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@MakeMeDriver, HistBoleiasAceites::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@MakeMeDriver, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@MakeMeDriver, Perfil::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun submitMakeMeDriver(view: View) {
        val numCC = findViewById<EditText>(R.id.valueCartaoCidadao).text.toString()
        val numCarta = findViewById<EditText>(R.id.valueCartaConducao).text.toString()

        val errorMissingCitizenCard = findViewById<TextView>(R.id.errorMissingCitizenCard)
        val errorMissingDriverLicense = findViewById<TextView>(R.id.errorMissingDriverLicense)
        errorMissingCitizenCard.visibility = View.INVISIBLE
        errorMissingDriverLicense.visibility = View.INVISIBLE

        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()

        when {
            numCC.isEmpty() -> {
                errorMissingCitizenCard.visibility = View.VISIBLE

            }
            numCarta.isEmpty() -> {
                errorMissingDriverLicense.visibility = View.VISIBLE

            }
            else -> {
                GlobalScope.launch {
                    Connection.makeMeDriver(numCC, numCarta, idUser)
                }
            }
        }
    }
}