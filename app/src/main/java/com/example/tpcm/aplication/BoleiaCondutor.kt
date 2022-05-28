package com.example.tpcm.aplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BoleiaCondutor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boleia_condutor)

        val idBoleia = intent.getStringExtra(PARAM_ID)
        if (idBoleia != null) {
            getDadosBoleia(idBoleia)
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
                val intent = Intent(this@BoleiaCondutor, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {

                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@BoleiaCondutor, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@BoleiaCondutor, Perfil::class.java)
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
        val tvTituloViagem = findViewById<TextView>(R.id.tvTituloViagemBoleiaCondutor)
        val tvNomeCondutor = findViewById<TextView>(R.id.tvNomeCondutorBoleiaCondutor)
        val tvDataBoleia = findViewById<TextView>(R.id.tvDataBoleiaBoleiaCondutor)
        val tvModeloCarro = findViewById<TextView>(R.id.tvModeloCarroBoleiaCondutor)
        val tvValorBoleia = findViewById<TextView>(R.id.tvValorBoleiaBoleiaCondutor)
        val tvPontoEncontro = findViewById<TextView>(R.id.tvPontoEncontroBoleiaCondutor)

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

    fun scanQrCode(view: View) {
        val intent = Intent(this@BoleiaCondutor, ScanQrCode::class.java)
        startActivity(intent)
    }
}