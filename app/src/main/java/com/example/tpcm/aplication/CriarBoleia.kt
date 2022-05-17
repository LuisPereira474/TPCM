package com.example.tpcm.aplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import java.text.SimpleDateFormat
import java.util.*


class CriarBoleia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_boleia)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                Log.d("teste", "entrou")
                // Respond to navigation item 2 click
                true
            }
            R.id.nav_rides -> {

                val intent = Intent(this@CriarBoleia, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@CriarBoleia, AddBoleiaSemHist::class.java)
                startActivity(intent)
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

    @SuppressLint("SimpleDateFormat")
    fun addRide(view: View) {
        val datePicker = findViewById<DatePicker>(R.id.dateCriarBoleia)
        val day: Int = datePicker.dayOfMonth
        val month: Int = datePicker.month
        val year: Int = datePicker.year
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date: String = sdf.format(calendar.time)

        val from = findViewById<EditText>(R.id.fromCriarBoleia).text.toString()
        val to = findViewById<EditText>(R.id.toCriarBoleia).text.toString()
        val meeting = findViewById<EditText>(R.id.meetingCriarBoleia).text.toString()
        val car = findViewById<EditText>(R.id.carCriarBoleia).text.toString()
        val price = findViewById<EditText>(R.id.priceCriarBoleia).text.toString()
        val seats = findViewById<EditText>(R.id.seatsCriarBoleia).text.toString()
        val obs = findViewById<EditText>(R.id.obsCriarBoleia).text.toString()
        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()

        Connection.createRide(from,to,meeting,car,date,price,seats,obs,idUser)

        val intent = Intent(this@CriarBoleia, AddBoleiaSemHist::class.java)
        startActivity(intent)
    }
}
