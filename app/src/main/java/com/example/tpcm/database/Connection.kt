package com.example.tpcm.database

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

object Connection {
    @SuppressLint("StaticFieldLeak")
    private var db = FirebaseFirestore.getInstance()

    fun login(email: String, password: String) {
        db.collection("utilizador")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if ((document.data["email"] as String) == email && (document.data["password"] as String) == password) {
                            Log.d("TAG", (document.data["idUser"] as String?).toString())
                        }

                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    fun singUp(email: String, nome: String, password: String) {
        val user = hashMapOf(
            "email" to email,
            "foto" to "teste",
            "idUser" to UUID.randomUUID().toString(),
            "nome" to nome,
            "password" to password,
            "pontos" to 0,
            "sexo" to true
        )

        db.collection("utilizador")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var checkEmail = true
                    for (document in task.result!!) {
                        if ((document.data["email"] as String) == email) {
                            checkEmail = false
                            break
                        }
                    }
                    if (checkEmail) {
                        db.collection("utilizador")
                            .add(user)
                            .addOnSuccessListener {
                                Log.d(
                                    "TAG",
                                    "DocumentSnapshot successfully written!"
                                )
                            }
                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                    } else {
                        Log.d("TAG", "Email já registado")
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }

    }

}