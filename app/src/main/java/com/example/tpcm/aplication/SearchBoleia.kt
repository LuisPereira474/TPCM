package com.example.tpcm.aplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import com.example.tpcm.R
import java.text.SimpleDateFormat
import java.util.*


const val PARAM_FROM = "from"
const val PARAM_TO = "to"
const val PARAM_DATE = "date"

class SearchBoleia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_boleia)
    }

    @SuppressLint("SimpleDateFormat")
    fun searchBoleia(view: View) {
        val from = findViewById<EditText>(R.id.fromSearchBoleia).text
        val to = findViewById<EditText>(R.id.toSearchBoleia).text
        val datePicker = findViewById<DatePicker>(R.id.dateSearchBoleia)

        val erroFrom = findViewById<TextView>(R.id.erroMissingFrom)
        val erroTo = findViewById<TextView>(R.id.erroMissingTo)
        erroFrom.visibility = View.INVISIBLE
        erroTo.visibility = View.INVISIBLE

        when {
            from.isEmpty() -> {
                erroFrom.visibility = View.VISIBLE
            }
            to.isEmpty() -> {
                erroTo.visibility = View.VISIBLE
            }
            else -> {
                val date = formatDate(datePicker.dayOfMonth,datePicker.month,datePicker.year)
                val intent = Intent(this, SearchBoleiaList::class.java).apply {
                    putExtra(PARAM_FROM,from.toString())
                    putExtra(PARAM_TO,to.toString())
                    putExtra(PARAM_DATE,date)
                }
                startActivity(intent)
            }
        }
    }
    private fun formatDate(day: Int, month: Int, year: Int): String? {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = 0
        cal.set(year, month, day)
        val date: Date = cal.time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(date)
    }
}