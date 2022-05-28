package com.example.tpcm.aplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import kotlinx.android.synthetic.main.dialog_box.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
                val intent = Intent(this@CriarBoleia, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {

                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@CriarBoleia, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@CriarBoleia, Perfil::class.java)
                startActivity(intent)
                true
            }
            else -> {
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
        val seatsEditText = findViewById<EditText>(R.id.seatsCriarBoleia)
        val seats = Integer.parseInt(seatsEditText.text.toString())
        val obs = findViewById<EditText>(R.id.obsCriarBoleia).text.toString()
        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()

        GlobalScope.launch {
            if (Connection.createRide(from,to,meeting,car,date,price,seats,obs,idUser) == 1) {
                runOnUiThread {
                    createDialog(resources.getString(R.string.error))
                }
            } else {
                runOnUiThread {
                    createDialog(resources.getString(R.string.success))
                }
            }
        }

    }

    private fun createDialog(msg: String) {
        val dialog = Dialog(this@CriarBoleia)
        dialog.setContentView(R.layout.dialog_box)
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_background))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.popup_window_text.text = msg
        dialog.setCancelable(false)
        dialog.show()

        dialog.findViewById<Button>(R.id.popup_ok_btt).setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@CriarBoleia, AddBoleiaSemHist::class.java)
            startActivity(intent)
        }

    }
}
