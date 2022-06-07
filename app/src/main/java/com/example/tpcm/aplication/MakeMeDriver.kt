package com.example.tpcm.aplication

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.tpcm.R
import kotlinx.android.synthetic.main.activity_make_me_driver.*
import kotlinx.android.synthetic.main.activity_search_boleia.*

class MakeMeDriver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_me_driver)

        btnHelpPageMakeMeDriver.setOnClickListener{
            createDialog()
        }
    }

    private fun createDialog() {
        val dialog = Dialog(this@MakeMeDriver)
        dialog.setContentView(R.layout.dialog_help_make_me_driver)
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_background))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.show()

        dialog.findViewById<TextView>(R.id.iconClosePopUpMakeMeDriver).setOnClickListener {
            dialog.dismiss()
        }

    }



}