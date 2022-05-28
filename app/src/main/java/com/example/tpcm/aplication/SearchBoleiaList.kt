package com.example.tpcm.aplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpcm.R
import com.example.tpcm.adapters.SearchAdapter
import com.example.tpcm.database.Connection
import com.example.tpcm.models.Search
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_search_boleia_list.*
import kotlinx.android.synthetic.main.dialog_box.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SearchBoleiaList : AppCompatActivity() {

    private lateinit var myList: ArrayList<Search>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_boleia_list)
        getBoleias()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this@SearchBoleiaList, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {

                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@SearchBoleiaList, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@SearchBoleiaList, Perfil::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getBoleias() {
        val from = intent.getStringExtra(PARAM_FROM)
        val to = intent.getStringExtra(PARAM_TO)
        val date = intent.getStringExtra(PARAM_DATE)
        myList = ArrayList<Search>()

        var document: HashMap<String, QueryDocumentSnapshot>? = null
        GlobalScope.launch {
            val errorNoResults = findViewById<TextView>(R.id.errorNoResults)
            errorNoResults.visibility = View.INVISIBLE

            document = Connection.getBoleias(from.toString(), to.toString(), date.toString())

            for (doc in document!!) {
                val date = SimpleDateFormat("dd-MM-yyyy").parse(doc.value.data["date"] as String)

                if (date > Calendar.getInstance().time) {
                    myList.add(
                        Search(
                            "${doc.value.data["from"]}-${doc.value.data["to"]}",
                            "${doc.value.data["date"]}",
                            "${doc.value.data["price"]}",
                            doc.key,
                            "${doc.value.data["idBoleia"]}"
                        )
                    )
                }

            }
            runOnUiThread {
                if (myList.isEmpty()) {
                    errorNoResults.visibility = View.VISIBLE
                } else {
                    var adapter = SearchAdapter(myList)
                    linhasSearch.adapter = adapter
                    adapter.setOnItemClickListener(object : SearchAdapter.onItemClickListener {
                        override fun onItemClick(idBoleia: TextView) {
                            acceptBoleia(idBoleia)
                        }
                    })
                    linhasSearch.layoutManager = LinearLayoutManager(this@SearchBoleiaList)
                }
            }
        }
    }

    fun acceptBoleia(idBoleia: TextView) {
        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()
        GlobalScope.launch {
            if (Connection.acceptBoleia(idUser, idBoleia.text.toString()) == 1) {
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
        val dialog = Dialog(this@SearchBoleiaList)
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
        }

    }
}