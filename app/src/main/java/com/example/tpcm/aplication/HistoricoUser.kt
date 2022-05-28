package com.example.tpcm.aplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpcm.R
import com.example.tpcm.adapters.HistoricoAdapter
import com.example.tpcm.adapters.SearchAdapter
import com.example.tpcm.database.Connection
import com.example.tpcm.models.Historico
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_historico_user.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val PARAM_ID = "idBoleia"
class HistoricoUser : AppCompatActivity() {

    private lateinit var myList: ArrayList<Historico>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico_user)
        getHistorico()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this@HistoricoUser, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@HistoricoUser, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@HistoricoUser, AddBoleiaSemHist::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@HistoricoUser, Perfil::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getHistorico() {

        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()

        myList = ArrayList<Historico>()

        var document: HashMap<Int, QueryDocumentSnapshot>? = null
        GlobalScope.launch {
            document = Connection.historicoUser(idUser)

            for (doc in document!!) {
                val date = SimpleDateFormat("dd-MM-yyyy").parse(doc.value.data["date"] as String)
                myList.add(
                    Historico(
                        "${doc.value.data["from"]}-${doc.value.data["to"]}",
                        "${doc.value.data["date"]}",
                        date < Calendar.getInstance().time,
                        "${doc.value.data["idBoleia"]}"
                    )
                )

            }

            runOnUiThread {
                var adapter = HistoricoAdapter(myList)
                linhasHistorico.adapter = adapter

                adapter.setOnItemClickListener(object : HistoricoAdapter.onItemClickListener {
                    override fun onItemClick(idBoleia: TextView) {
                        val intent = Intent(this@HistoricoUser, Boleia::class.java).apply {
                            putExtra(PARAM_ID,idBoleia.text.toString())
                        }
                        startActivity(intent)
                    }
                })

                linhasHistorico.layoutManager = LinearLayoutManager(this@HistoricoUser)
            }
        }
    }
}