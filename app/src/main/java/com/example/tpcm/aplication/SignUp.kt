package com.example.tpcm.aplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcm.MainActivity
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.firebase.firestore.FirebaseFirestore


class SignUp : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun loginPage(view: View) {
        val intent = Intent(this@SignUp, MainActivity::class.java)
        startActivity(intent)
    }

    fun signUpSubmit(view: View) {
        var password = findViewById<EditText>(R.id.ccInputPasse).text.toString()
        var confPassword = findViewById<EditText>(R.id.ccInputConfirmPasse).text.toString()
        var email = findViewById<EditText>(R.id.ccInputEmail).text.toString()
        var nome = findViewById<EditText>(R.id.ccInputName).text.toString()

        if (password == confPassword && email.isNotEmpty() && password.isNotEmpty() && nome.isNotEmpty()) {
            Connection.singUp(email,nome,password)
        }else{
            Log.d("TAG", "Passwords não correspondem")
        }
    }

}