package com.example.tpcm.aplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tpcm.R
import com.example.tpcm.adapters.WishListAdapter
import kotlinx.android.synthetic.main.activity_wish_list.*


class WishList : AppCompatActivity() {

    private lateinit var myList: ArrayList<linhasWishList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)


        myList = ArrayList<linhasWishList>()
        myList.add(
            linhasWishList(
                "Porto-Viana",
                "23 Março 2012",
                "10 €",
                "João Maia",
                "123"
            )
        )

        linhasWish.adapter = WishListAdapter(myList)
        linhasWish.layoutManager = LinearLayoutManager(this@WishList)


    }
}