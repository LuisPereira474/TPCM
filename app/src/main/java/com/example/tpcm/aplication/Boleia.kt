package com.example.tpcm.aplication

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import com.example.tpcm.R
import kotlinx.android.synthetic.main.activity_boleia.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tpcm.database.Connection
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_boleia.*
import kotlinx.android.synthetic.main.search_line.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Console

const val PARAM_ID_BOLEIA = "idBoleia"
const val PARAM_ID_USER = "idUser"

class Boleia : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boleia)



            val shared = getSharedPreferences("idUser", MODE_PRIVATE)
            val idUser = shared.getString("idUser", "").toString()

            val idBoleia = intent.getStringExtra(PARAM_ID)
            btnAbrirChat.setOnClickListener {
                if (idBoleia != null) {
                    openChat(idBoleia)
                }
            }
            if (idBoleia != null) {
                getDadosBoleia(idBoleia)
            }
            generateQrCode.setOnClickListener {
                if (idBoleia != null) {
                    getQrCode(idBoleia, idUser)
                }
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
                val intent = Intent(this@Boleia, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@Boleia, HistBoleiasAceites::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@Boleia, HistoricoUser::class.java)
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

    fun getDadosBoleia(idBoleia: String) {

        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()
        val tvTituloViagem = findViewById<TextView>(R.id.tvTituloViagem)
        val tvNomeCondutor = findViewById<TextView>(R.id.tvNomeCondutor)
        val tvDataBoleia = findViewById<TextView>(R.id.tvDataBoleia)
        val tvModeloCarro = findViewById<TextView>(R.id.tvModeloCarro)
        val tvValorBoleia = findViewById<TextView>(R.id.tvValorBoleia)
        val tvPontoEncontro = findViewById<TextView>(R.id.tvPontoEncontro)
        val rbAvaliacao = findViewById<RatingBar>(R.id.RB_RideEvaluation)

        var boleia: QueryDocumentSnapshot?
        var profile: QueryDocumentSnapshot?
        GlobalScope.launch {
            boleia = Connection.getDadosBoleia(idBoleia)
            profile = Connection.getProfileUser(boleia!!.data["idCriador"] as String)

            Connection.updateRideEvaluation(idBoleia, Connection.calculateRideEvaluation(idBoleia))


            runOnUiThread {
                val from_localidade = boleia!!.data["from"].toString().split("_")[1]
                val to_localidade = boleia!!.data["to"].toString().split("_")[1]
                tvTituloViagem.text = "$from_localidade - $to_localidade"
                tvNomeCondutor.text = profile!!.data["nome"].toString()
                tvDataBoleia.text = boleia!!.data["date"].toString()
                tvModeloCarro.text = boleia!!.data["carBrand"].toString() + " " + boleia!!.data["carModel"].toString() + " " + boleia!!.data["carYear"].toString() + " " + boleia!!.data["carFuelType"].toString()
                tvValorBoleia.text = boleia!!.data["price"].toString()+ "€"
                tvPontoEncontro.text = boleia!!.data["from"].toString().split("_")[0]
                valueLugaresDisponiveis.text = boleia!!.data["seats"].toString()

                var rating = boleia!!.data["avaliacao"].toString().toFloat()

                if (rating >= 0 && rating <= 5) {
                    rbAvaliacao.rating = rating
                } else {
                    rbAvaliacao.rating = 3.0F
                }

            }
        }

    }

    fun getQrCode(idBoleia: String, idUser: String) {
        val intent = Intent(this@Boleia, QrCode::class.java).apply {
            putExtra(PARAM_ID_BOLEIA,idBoleia)
            putExtra(PARAM_ID_USER,idUser)
        }
        startActivity(intent)
    }


    fun evaluateRide(view: View) {
        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()
        val idBoleia = intent.getStringExtra(PARAM_ID)
        val ratingBar = findViewById<RatingBar>(R.id.RB_RideEvaluation)
        GlobalScope.launch {
            if (idBoleia != null) {
                Connection.evaluateRide(idUser, idBoleia, ratingBar.rating)
            } else {
                Log.d("TAGG", "Something went wrong")
            }
        }
    }


    fun openChat(idBoleia: String){
        val intent = Intent(this@Boleia, Chat::class.java).apply {
            putExtra(PARAM_ID_BOLEIA, idBoleia)
        }

        startActivity(intent)
    }


}