package com.example.tpcm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
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
        var password = findViewById<EditText>(R.id.ccInputPasse).text
        var confPassword = findViewById<EditText>(R.id.ccInputConfirmPasse).text
        val user = hashMapOf(
            "email" to findViewById<EditText>(R.id.ccInputEmail).text,
            "foto" to "teste",
            "idUser" to UUID.randomUUID().toString(),
            "nome" to findViewById<EditText>(R.id.ccInputName).text,
            "password" to password,
            "pontos" to 0,
            //adicionar opçao sexo
            "sexo" to true,
        )
        Log.d("TAG", "aqui")


        //if (password.toString() == confPassword.toString()) {

            db.collection("utilizador")
                .add(user)
                .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                    Log.d(
                        "TAG",
                        "DocumentSnapshot added with ID: " + documentReference.id
                    )
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                })
        //}
    }

}