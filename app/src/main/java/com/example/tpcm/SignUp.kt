package com.example.tpcm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


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
        val user = hashMapOf(
            "email" to email,
            "foto" to "teste",
            "idUser" to UUID.randomUUID().toString(),
            "nome" to nome,
            "password" to password,
            "pontos" to 0,
            "sexo" to true
        )

        Log.d("TAG", user.toString())

        if (password.toString() == confPassword.toString()) {

            db.collection("utilizador")
                .add(user)
                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
        }
    }

}