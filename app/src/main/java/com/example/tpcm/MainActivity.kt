package com.example.tpcm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcm.aplication.AddBoleiaSemHist
import com.example.tpcm.aplication.Boleia
import com.example.tpcm.aplication.SignUp
import com.example.tpcm.database.Connection


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_boleia)

//        val intent = Intent(this@MainActivity, PopUpWindow::class.java)
//        val string: String = getString(R.string.popUpPoints)
//        intent.putExtra("popuptext", string)
//        intent.putExtra("popupbtn", "CONFIRMAR")
//        intent.putExtra("darkstatusbar", false)
//        startActivity(intent)
    }

    fun loginSubmit(view: View) {
        val email = findViewById<EditText>(R.id.inputEmail).text.toString()
        val password = findViewById<EditText>(R.id.inputPass).text.toString()
        val errorLogin = findViewById<TextView>(R.id.errorLogin)
        var idUser = ""
        errorLogin.setVisibility(View.INVISIBLE);
        if (email.isEmpty() || password.isEmpty()) {
            errorLogin.setVisibility(View.VISIBLE);
        } else {
            idUser = Connection.login(email, password, errorLogin)
            if (idUser!=""){
                val sharedPreferences: SharedPreferences =
                    getSharedPreferences("idUser", Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString("idUser", idUser)
                    .apply()
                val intent = Intent(this@MainActivity, AddBoleiaSemHist::class.java)
                startActivity(intent)
            }

        }
    }

    fun signUpPage(view: View) {
        val intent = Intent(this@MainActivity, SignUp::class.java)
        startActivity(intent)
    }
}