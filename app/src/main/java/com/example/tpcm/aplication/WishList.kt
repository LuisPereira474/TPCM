package com.example.tpcm.aplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpcm.R
import com.example.tpcm.adapters.SearchAdapter
import com.example.tpcm.adapters.WishListAdapter
import com.example.tpcm.database.Connection
import com.example.tpcm.models.Search
import com.example.tpcm.models.Wishlist
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_search_boleia_list.*
import kotlinx.android.synthetic.main.activity_wish_list.*
import kotlinx.android.synthetic.main.dialog_box.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WishList : AppCompatActivity() {

    private lateinit var myList: ArrayList<Wishlist>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)
        getWishlist()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this@WishList, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@WishList, HistBoleiasAceites::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@WishList, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@WishList, Perfil::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun sendToProfile(view: View) {
        val intent = Intent(this@WishList, Perfil::class.java)
        startActivity(intent)
    }

    @SuppressLint("SimpleDateFormat")
    fun getWishlist() {
        myList = ArrayList<Wishlist>()

        var document: HashMap<String, QueryDocumentSnapshot>? = null
        GlobalScope.launch {
            val errorNoResultsWhislist = findViewById<TextView>(R.id.errorNoResultsWhislist)
            errorNoResultsWhislist.visibility = View.INVISIBLE

            val shared = getSharedPreferences("idUser", MODE_PRIVATE)
            val idUser = shared.getString("idUser", "").toString()

            document = Connection.getWishlist(idUser)

            for (doc in document!!) {
                val date = SimpleDateFormat("dd-MM-yyyy").parse(doc.value.data["date"] as String)
                val user = Connection.getProfileUser(doc.key)
                if (date > Calendar.getInstance().time) {
                    myList.add(
                        Wishlist(
                            "${doc.value.data["from"]}-${doc.value.data["to"]}",
                            "${doc.value.data["date"]}",
                            "${doc.value.data["price"]}",
                            user!!.data["nome"].toString(),
                            "${doc.value.data["idBoleia"]}"
                        )
                    )
                }

            }
            runOnUiThread {
                if (myList.isEmpty()) {
                    errorNoResultsWhislist.visibility = View.VISIBLE
                } else {
                    var adapter = WishListAdapter(myList)
                    linhasWishList.adapter = adapter
                    adapter.setOnItemClickListener(object : WishListAdapter.onItemClickListener {
                        override fun onItemClick(idBoleia: TextView) {
                            acceptBoleia(idBoleia)
                        }

                        override fun onRemoveWishlistClick(idBoleia: TextView) {
                            removeWishlist(idBoleia)
                        }
                    })
                    linhasWishList.layoutManager = LinearLayoutManager(this@WishList)
                }
            }
        }
    }

    fun acceptBoleia(idBoleia: TextView) {
        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()
        GlobalScope.launch {
            if (Connection.acceptBoleia(idUser, idBoleia.text.toString()) == 1) {
                Connection.removeWishList(idUser, idBoleia.text.toString())
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

    fun removeWishlist(idBoleia: TextView) {
        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()
        GlobalScope.launch {
            val result = Connection.removeWishList(idUser, idBoleia.text.toString())
            if (result == 0) {
                runOnUiThread {
                    createDialog(resources.getString(R.string.success))
                }
            } else {
                runOnUiThread {
                    createDialog(resources.getString(R.string.error))
                }
            }
        }
    }

    private fun createDialog(msg: String) {
        val dialog = Dialog(this@WishList)
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
            finish();
            startActivity(intent);
        }

    }
}