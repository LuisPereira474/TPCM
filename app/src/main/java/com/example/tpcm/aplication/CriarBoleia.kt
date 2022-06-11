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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcm.R
import com.example.tpcm.carAPI.Car
import com.example.tpcm.carAPI.EndPointsCarAPI
import com.example.tpcm.carAPI.ServiceBuilderCarAPI
import com.example.tpcm.database.Connection
import kotlinx.android.synthetic.main.dialog_box.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

const val KEY = "078c45d56bmsh6f45aa6f7fef0f4p16e173jsndf11a2342d51"
const val HOST = "cars-by-api-ninjas.p.rapidapi.com"

class CriarBoleia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_boleia)

        val from_mapas = intent.getStringExtra(PARAM_FROM_MAPAS)
        val to_mapas = intent.getStringExtra(PARAM_TO_MAPAS)

        val from = findViewById<EditText>(R.id.fromCriarBoleia)
        from.isEnabled = false
        from.setText(from_mapas)
        val to = findViewById<EditText>(R.id.toCriarBoleia)
        to.isEnabled = false
        to.setText(to_mapas)

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
                val intent = Intent(this@CriarBoleia, HistBoleiasAceites::class.java)
                startActivity(intent)
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
        val brandCar = findViewById<EditText>(R.id.brandCarCriarBoleia).text.toString()
        val modelCar = findViewById<EditText>(R.id.modelCarCriarBoleia).text.toString()
        val yearCar = findViewById<EditText>(R.id.yearCarCriarBoleia).text.toString().toInt()
        val price = findViewById<EditText>(R.id.priceCriarBoleia).text.toString().toInt()
        val seatsEditText = findViewById<EditText>(R.id.seatsCriarBoleia)
        val seats = Integer.parseInt(seatsEditText.text.toString())
        val obs = findViewById<EditText>(R.id.obsCriarBoleia).text.toString()
        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()

        val from_mapas = intent.getStringExtra(PARAM_FROM_MAPAS)
        val to_mapas = intent.getStringExtra(PARAM_TO_MAPAS)

        GlobalScope.launch {
        val fuelType = getTypeOfFuel(brandCar, modelCar, yearCar)


            if (Connection.createRide(from_mapas.toString(),
                    to_mapas.toString(),from,Car(brandCar,modelCar, yearCar,fuelType),date,price,seats,obs,idUser) == 1) {
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
            val intent = Intent(this@CriarBoleia, HistoricoUser::class.java)
            startActivity(intent)
        }

    }

    private suspend fun getTypeOfFuel(make: String, model: String, year: Int):String{
        var typeOfFuel = getString(R.string.undefined)
        var canGO = false
        val request = ServiceBuilderCarAPI.buildService(EndPointsCarAPI::class.java)
        val call = request.getCarsDetails(HOST,KEY,make,model,year)

        call.enqueue(object : Callback<List<Car>> {
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful) {
                    if (response.body()!!.isNotEmpty()) {
                        val carros: List<Car> = response.body()!!
                        typeOfFuel = carros[0].fuel_type
                        canGO = true
                    }else{
                        Toast.makeText(this@CriarBoleia, getString(R.string.carError), Toast.LENGTH_SHORT).show()
                        canGO = true
                    }
                } else {
                    canGO=true
                    Log.d("TAG","erro")
                }
            }

            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Toast.makeText(this@CriarBoleia, "${t.message}", Toast.LENGTH_SHORT).show()
                canGO=true
            }
        })
        while (!canGO){
            delay(1)
        }
        return typeOfFuel
    }
}
