package com.example.tpcm.aplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_boleia.*
import kotlinx.android.synthetic.main.activity_more_info.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoreInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_info)

        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()

        val idBoleia = intent.getStringExtra(PARAM_ID_BOLEIA)
        if (idBoleia != null) {
            getDadosBoleia(idBoleia)
        }
        backMoreInfoMoreInfo.setOnClickListener{
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
                val intent = Intent(this@MoreInfo, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@MoreInfo, HistBoleiasAceites::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@MoreInfo, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@MoreInfo, Perfil::class.java)
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
        val tvTituloViagem = findViewById<TextView>(R.id.tvTituloViagemMoreInfo)
        val tvNomeCondutor = findViewById<TextView>(R.id.tvNomeCondutorMoreInfo)
        val tvDataBoleia = findViewById<TextView>(R.id.tvDataBoleiaMoreInfo)
        val tvModeloCarro = findViewById<TextView>(R.id.tvModeloCarroMoreInfo)
        val tvPontoEncontro = findViewById<TextView>(R.id.tvPontoEncontroMoreInfo)
        val tvValorBoleia = findViewById<TextView>(R.id.tvValorBoleiaMoreInfo)
        val valueSexoMoreInfo = findViewById<TextView>(R.id.valueSexoMoreInfo)
        val valueAvalCondMoreInfo = findViewById<TextView>(R.id.valueAvalCondMoreInfo)
        var boleia: QueryDocumentSnapshot?
        var profile: QueryDocumentSnapshot?
        var condutor: QueryDocumentSnapshot?
        GlobalScope.launch {
            boleia = Connection.getDadosBoleia(idBoleia)
            profile = Connection.getProfileUser(idUser)
            condutor = Connection.getCondutorUser(idUser)
            var sexo = ""
            when {
                profile!!.data["sexo"].toString().toInt()==1 -> {
                    sexo = getString(R.string.male)
                }
                profile!!.data["sexo"].toString().toInt()==2 -> {
                    sexo = getString(R.string.female)
                }
                profile!!.data["sexo"].toString().toInt()==3 -> {
                    sexo = getString(R.string.other)
                }
            }
            runOnUiThread {
                val from_localidade = boleia!!.data["from"].toString().split("_")[1]
                val to_localidade = boleia!!.data["to"].toString().split("_")[1]
                val meeting = boleia!!.data["from"].toString().split("_")[0]
                Log.d("TAG",meeting)
                tvTituloViagem.text =
                    from_localidade + "-" + to_localidade
                tvNomeCondutor.text = profile!!.data["nome"].toString()
                tvDataBoleia.text = boleia!!.data["date"].toString()
                tvModeloCarro.text = boleia!!.data["carBrand"].toString() + " " + boleia!!.data["carModel"].toString() + " " + boleia!!.data["carYear"].toString() + " " + boleia!!.data["carFuelType"].toString()
                tvValorBoleia.text = boleia!!.data["price"].toString()+ "€"
                tvPontoEncontro.text = meeting
                valueLugaresDisponiveisMoreInfo.text = boleia!!.data["seats"].toString()
                valueSexoMoreInfo.text = sexo
                valueAvalCondMoreInfo.text = condutor!!.data["avaliacao"].toString()
            }
        }

    }
}