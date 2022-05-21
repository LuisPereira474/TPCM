package com.example.tpcm.database

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import java.util.*
import java.util.regex.Pattern

object Connection {
    @SuppressLint("StaticFieldLeak")
    private var db = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String, errorLogin: TextView): String {
        var idUser = ""
        var confirmLogin = false
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("utilizador")
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            for (document in task.result!!) {
                                if ((document.data["email"] as String) == email && (document.data["password"] as String) == password) {
                                    confirmLogin = true
                                    idUser = (document.data["idUser"] as String?).toString()
                                }
                            }
                            if (!confirmLogin) {
                                errorLogin.visibility = View.VISIBLE;
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                        }
                    }
            }
        }
        while (!confirmLogin) {
            delay(1)
        }
        return idUser
    }

    suspend fun singUp(
        email: String,
        nome: String,
        password: String,
        erroSignUpEmail: TextView,
        errorInvalidEmail: TextView
    ): String {
        val user = hashMapOf(
            "email" to email,
            "foto" to "teste",
            "idUser" to UUID.randomUUID().toString(),
            "nome" to nome,
            "password" to password,
            "pontos" to 0,
            "sexo" to true
        )
        var idUser = ""
        var canContinue = false
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                if (isValidString(email)) {
                    db.collection("utilizador")
                        .whereEqualTo("nome", nome)
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (task.result.isEmpty) {
                                    db.collection("utilizador")
                                        .whereEqualTo("email", email)
                                        .get()
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                if (task.result.isEmpty) {
                                                    db.collection("utilizador")
                                                        .add(user)
                                                        .addOnSuccessListener {
                                                            db.collection("utilizador")
                                                                .get()
                                                                .addOnCompleteListener { task ->
                                                                    if (task.isSuccessful) {
                                                                        for (document in task.result!!) {
                                                                            if ((document.data["email"] as String) == email) {
                                                                                idUser =
                                                                                    (document.data["idUser"] as String?).toString()
                                                                            }
                                                                        }
                                                                        canContinue = true
                                                                    } else {
                                                                        canContinue = true
                                                                        Log.w(
                                                                            "TAG",
                                                                            "Error getting documents.",
                                                                            task.exception
                                                                        )
                                                                    }
                                                                }
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
                                                    erroSignUpEmail.visibility = View.VISIBLE;
                                                    canContinue = true
                                                }
                                            } else {
                                                Log.w(
                                                    "TAG",
                                                    "Error getting documents.",
                                                    task.exception
                                                )
                                            }
                                        }
                                } else {
                                    Log.d("TAG", "Email já registado")
                                    erroSignUpEmail.visibility = View.VISIBLE;
                                    canContinue = true
                                }
                            }
                        }
                } else {
                    Log.w("TAG", "Invalid Email")
                    errorInvalidEmail.visibility = View.VISIBLE;
                }
            }
        }
        while (!canContinue) {
            delay(1)
        }
        return idUser
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

    fun createRide(
        from: String,
        to: String,
        meeting: String,
        car: String,
        date: String,
        price: String,
        seats: String,
        obs: String,
        idUser: String
    ) {

        val boleia = hashMapOf(
            "idCriador" to idUser,
            "idBoleia" to UUID.randomUUID().toString(),
            "from" to from,
            "to" to to,
            "meeting" to meeting,
            "car" to car,
            "date" to date,
            "price" to price,
            "seats" to seats,
            "obs" to obs
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

    @DelicateCoroutinesApi
    suspend fun historicoUser(idUser: String): HashMap<Int, QueryDocumentSnapshot> {
        val boleia = HashMap<Int, QueryDocumentSnapshot>()
        var count = 0
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("boleia")
                    .whereEqualTo("idCriador", idUser)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                count++
                                boleia[count] = document
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                        }
                    }
            }
        }
        while (boleia.isEmpty()) {
            delay(1)
        }
        return boleia
    }

    @DelicateCoroutinesApi
    suspend fun getBoleias(
        from: String,
        to: String,
        date: String
    ): HashMap<String, QueryDocumentSnapshot> {
        val boleia = HashMap<String, QueryDocumentSnapshot>()
        var canContinue = false
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("boleia")
                    .whereEqualTo("from", from)
                    .whereEqualTo("to", to)
                    .whereEqualTo("date", date)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                db.collection("utilizador")
                                    .get()
                                    .addOnCompleteListener { task2 ->
                                        if (task2.isSuccessful) {
                                            for (users in task2.result!!) {
                                                if ((users.data["idUser"] as String) == document.data["idCriador"]) {
                                                    boleia[(users.data["nome"] as String?).toString()] =
                                                        document
                                                }
                                            }
                                            canContinue = true
                                        } else {
                                            Log.w(
                                                "TAG",
                                                "Error getting documents.",
                                                task2.exception
                                            )
                                            canContinue = true
                                        }
                                    }
                            }
                            if (task.result.isEmpty) {
                                canContinue = true
                            }
                        } else {
                            canContinue = true
                            Log.w("TAG", "Error getting documents.", task.exception)
                        }
                    }
            }
        }

        while (boleia.isEmpty() && !canContinue) {
            delay(1)
        }

        return boleia
    }

    @DelicateCoroutinesApi
    suspend fun getProfileUser(idUser: String): QueryDocumentSnapshot? {
        var user: QueryDocumentSnapshot? = null
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("utilizador")
                    .whereEqualTo("idUser", idUser)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                user = document
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                        }
                    }
            }
        }
        while (user == null) {
            delay(1)
        }
        return user
    }

    @DelicateCoroutinesApi
    suspend fun editProfile(idUser: String, editName: String, editEmail: String) {
        var user: QueryDocumentSnapshot? = null
        val data = hashMapOf(
            "nome" to editName,
            "email" to editEmail
        )

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("utilizador")
                    .whereEqualTo("idUser", idUser)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                user = document
                                db.collection("utilizador").document(user!!.id)
                                    .set(data, SetOptions.merge())
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                        }
                    }

            }
        }
        while (user == null) {
            delay(1)
        }

//        return user
    }

}