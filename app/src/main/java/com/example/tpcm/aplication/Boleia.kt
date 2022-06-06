package com.example.tpcm.aplication

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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

        btnAbrirChat.setOnClickListener {
            val shared = getSharedPreferences("idUser", MODE_PRIVATE)
            val idUser = shared.getString("idUser", "").toString()

            val idBoleia = intent.getStringExtra(PARAM_ID).toString()

            openChat(idBoleia, idUser);
        }




            val shared = getSharedPreferences("idUser", MODE_PRIVATE)
            val idUser = shared.getString("idUser", "").toString()

            val idBoleia = intent.getStringExtra(PARAM_ID)
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

        var boleia: QueryDocumentSnapshot?
        var profile: QueryDocumentSnapshot?
        GlobalScope.launch {
            boleia = Connection.getDadosBoleia(idBoleia)
            profile = Connection.getProfileUser(idUser)
            runOnUiThread {
                tvTituloViagem.text =
                    boleia!!.data["from"].toString() + "-" + boleia!!.data["to"].toString()
                tvNomeCondutor.text = profile!!.data["nome"].toString()
                tvDataBoleia.text = boleia!!.data["date"].toString()
                tvModeloCarro.text = boleia!!.data["car"].toString()
                tvValorBoleia.text = boleia!!.data["price"].toString()
                tvPontoEncontro.text = boleia!!.data["meeting"].toString()
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

    fun openChat(idBoleia: String, idUser:String){
        val intent = Intent(this@Boleia, Chat::class.java).apply {
            putExtra(PARAM_ID_BOLEIA, idBoleia)
            putExtra(PARAM_ID_USER, idUser)
        }

        startActivity(intent)
    }


}