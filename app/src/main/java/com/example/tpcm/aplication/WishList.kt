package com.example.tpcm.aplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpcm.R
import com.example.tpcm.adapters.WishListAdapter
import com.example.tpcm.models.Wishlist
import kotlinx.android.synthetic.main.activity_wish_list.*


class WishList : AppCompatActivity() {

    private lateinit var myList: ArrayList<Wishlist>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)


        myList = ArrayList<Wishlist>()
        myList.add(
            Wishlist(
                "Porto-Viana",
                "23 Março 2012",
                "10 €",
                "João Maia",
                "123"
            )
        )

        linhasWishList.adapter = WishListAdapter(myList)
        linhasWishList.layoutManager = LinearLayoutManager(this@WishList)


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
}