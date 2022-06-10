package com.example.tpcm.database

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import java.util.regex.Pattern

object Connection {
    @SuppressLint("StaticFieldLeak")
    private var db = FirebaseFirestore.getInstance()
    private var utilizadores = db.collection("utilizador").get()


    fun login(email: String, password: String, errorLogin: TextView):String {
        var idUser = ""
        if (utilizadores.isSuccessful) {
            var confirmLogin = false
            for (document in utilizadores.result!!) {
                if ((document.data["email"] as String) == email && (document.data["password"] as String) == password) {
                    confirmLogin = true
                    idUser = (document.data["idUser"] as String?).toString()
                }
            }
            if (confirmLogin) {
                return idUser
            } else {
                errorLogin.visibility = View.VISIBLE;
            }
        } else {
            Log.w("TAG", "Error getting documents.", utilizadores.exception)
        }
        return idUser
    }

    fun singUp(
        email: String,
        nome: String,
        password: String,
        erroSignUpEmail: TextView,
        errorInvalidEmail: TextView
    ) {
        val user = hashMapOf(
            "email" to email,
            "foto" to "teste",
            "idUser" to UUID.randomUUID().toString(),
            "nome" to nome,
            "password" to password,
            "pontos" to 0,
            "sexo" to true
        )

        if (isValidString(email)) {
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
                                .addOnFailureListener { e ->
                                    Log.w(
                                        "TAG",
                                        "Error writing document",
                                        e
                                    )
                                }
                        } else {
                            Log.d("TAG", "Email já registado")
                            erroSignUpEmail.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.exception)
                    }
                }
        } else {
            Log.w("TAG", "Invalid Email")
            errorInvalidEmail.setVisibility(View.VISIBLE);
        }


    }

    private val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private fun isValidString(str: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }

    fun createRide(from:String,to:String,meeting:String,car:String,date:String,price:String,seats:String,obs:String,idUser:String){

        val boleia = hashMapOf(
            "idCriador" to idUser,
            "idBoleia" to UUID.randomUUID().toString(),
            "from" to from,
            "to" to "to",
            "meeting" to meeting,
            "car" to car,
            "date" to date,
            "price" to price,
            "seats" to seats,
            "obs" to seats,
            "avaliacao" to 3
        )
        db.collection("boleia")
            .add(boleia)
            .addOnSuccessListener {
                Log.d(
                    "TAG",
                    "DocumentSnapshot successfully written!"
                )
            }
            .addOnFailureListener { e ->
                Log.w(
                    "TAG",
                    "Error writing document",
                    e
                )
            }
    }

    //Obtém uma boleia
    fun getRide(id: String): QuerySnapshot? {
        val ride = db.collection("boleia").whereEqualTo("idBoleia", id).get()
            .addOnCompleteListener{
                Log.d(
                    "TAG",
                    "Ride obtained Successfully"
                )
            }
            .addOnFailureListener {
                Log.d(
                    "TAG",
                    "Error getting the ride"
                )
            }
        return ride.result;
    }

}