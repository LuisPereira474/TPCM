package com.example.tpcm.database

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import java.util.regex.Pattern

object Connection {
    @SuppressLint("StaticFieldLeak")
    private var db = FirebaseFirestore.getInstance()
    private var utilizadores = db.collection("utilizador").get()

    suspend fun login(email: String, password: String, errorLogin: TextView): String {
        var idUser = ""
        var confirmLogin = false
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("utilizador")
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            for (document in task.result!!) {
                                confirmLogin = true
                                idUser = (document.data["idUser"] as String?).toString()
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

    suspend fun createRide(
        from: String,
        to: String,
        meeting: String,
        car: String,
        date: String,
        price: String,
        seats: Int,
        obs: String,
        idUser: String
    ): Int {
        var result = 0
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
            "obs" to seats,
            "avaliacao" to 3
        )
        db.collection("boleia")
            .add(boleia)
            .addOnSuccessListener {
                result = 2
                Log.d(
                    "TAG",
                    "DocumentSnapshot successfully written!"
                )
            }
            .addOnFailureListener { e ->
                result = 1
                Log.w(
                    "TAG",
                    "Error writing document",
                    e
                )
            }
        while (result == 0) {
            delay(1)
        }
        return result
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

    }

    suspend fun acceptBoleia(idUser: String, idBoleia: String): Int {
        var canGo = false
        var successFail = 0
        var seatsAvailabel = -1

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("boleia")
                    .whereEqualTo("idBoleia", idBoleia)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                seatsAvailabel = Integer.parseInt(document.data["seats"].toString())
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                            canGo = true
                        }
                    }


            }
        }
        while (seatsAvailabel == -1) {
            delay(1)
        }
        if (seatsAvailabel > 0) {
            val data = hashMapOf(
                "idUser" to idUser,
                "idBoleia" to idBoleia
            )

            GlobalScope.launch {
                withContext(Dispatchers.Default) {
                    db.collection("boleia_utilizador")
                        .whereEqualTo("idBoleia", idBoleia)
                        .whereEqualTo("idUser", idUser)
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (task.result.size() > 0) {
                                    Log.d("TAG", "Erro.")
                                    successFail = 1
                                    canGo = true
                                } else {
                                    db.collection("boleia")
                                        .whereEqualTo("idBoleia", idBoleia)
                                        .get()
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                for (document in task.result!!) {
                                                    seatsAvailabel =
                                                        Integer.parseInt(document.data["seats"].toString())
                                                    if (seatsAvailabel > 0) {
                                                        db.collection("boleia")
                                                            .document(document.id)
                                                            .update("seats", seatsAvailabel - 1)
                                                    }
                                                }
                                            } else {
                                                Log.w(
                                                    "TAG",
                                                    "Error getting documents.",
                                                    task.exception
                                                )
                                                canGo = true
                                            }
                                        }
                                    db.collection("boleia_utilizador")
                                        .add(data)
                                        .addOnCompleteListener { task ->
                                            canGo = if (task.isSuccessful) {
                                                successFail = 2
                                                Log.d("TAG", "Success.")
                                                true
                                            } else {
                                                Log.w(
                                                    "TAG",
                                                    "Error getting documents.",
                                                    task.exception
                                                )
                                                true
                                            }
                                        }
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.exception)
                                canGo = true
                            }
                        }


                }
            }
            while (!canGo || successFail == 0) {
                delay(1)
            }
        } else {
            successFail = 1
        }
        return successFail
    }

    suspend fun getDadosBoleia(idBoleia: String): QueryDocumentSnapshot? {

        var boleia: QueryDocumentSnapshot? = null
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("boleia")
                    .whereEqualTo("idBoleia", idBoleia)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                boleia = document
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                        }
                    }
            }
        }
        while (boleia == null) {
            delay(1)
        }
        return boleia
    }

    @DelicateCoroutinesApi
    suspend fun historicoUserAceites(idUser: String): HashMap<Int, QueryDocumentSnapshot> {
        val boleia = HashMap<Int, QueryDocumentSnapshot>()
        var count = 0
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("boleia_utilizador")
                    .whereEqualTo("idUser", idUser)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                db.collection("boleia")
                                    .whereEqualTo("idCriador", idUser)
                                    .whereEqualTo("idBoleia", document.data["idBoleia"])
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

    suspend fun changePasswords(
        idUser: String,
        valueCurrentPass: String,
        valueNewPass: String
    ): Boolean {
        var success = false

        var user: QueryDocumentSnapshot? = null
        val data = hashMapOf(
            "password" to valueNewPass
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
                                if (document.data["password"] == valueCurrentPass) {
                                    db.collection("utilizador").document(user!!.id)
                                        .set(data, SetOptions.merge())
                                    success = true
                                }
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
        return success
    }

    suspend fun scanQrCodeResult(resultQr: String, idBoleia: String): Boolean {
        var result = false
        var canGo = false
        val resultArray = resultQr.split(",")

        val idBoleiaQr = resultArray[0]
        val idUser = resultArray[1]

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("boleia_utilizador")
                    .whereEqualTo("idBoleia", idBoleia)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                if (document.data["idBoleia"] == idBoleiaQr && document.data["idUser"] == idUser) {
                                    result = true
                                }
                            }
                            canGo = true
                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                            canGo = true
                        }
                    }

            }
        }
        while (!canGo) {
            delay(1)
        }

        return result
    }

    suspend fun addToWishList(idUser: String, idBoleia: String): Int {
        var successFail = -1
        val data = hashMapOf(
            "idUser" to idUser,
            "idBoleia" to idBoleia
        )
        db.collection("wishlist")
            .whereEqualTo("idBoleia", idBoleia)
            .whereEqualTo("idUser", idUser)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isEmpty) {

                        db.collection("wishlist")
                            .add(data)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    successFail = 0
                                    Log.d("TAG", "Success.")
                                } else {
                                    Log.w(
                                        "TAG",
                                        "Error getting documents.",
                                        task.exception
                                    )
                                    successFail = 1
                                }
                            }
                    } else {
                        successFail = 2
                    }
                } else {
                    successFail = 3
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }

        while (successFail == -1) {
            delay(1)
        }
        return successFail
    }


    @DelicateCoroutinesApi
    suspend fun getWishlist(idUser: String): HashMap<String, QueryDocumentSnapshot> {
        val wishlist = HashMap<String, QueryDocumentSnapshot>()
        var canContinue = false
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("wishlist")
                    .whereEqualTo("idUser", idUser)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                GlobalScope.launch {
                                    val boleia =
                                        getDadosBoleia(document.data["idBoleia"].toString())
                                    wishlist[(boleia!!.data["idCriador"] as String?).toString()] =
                                        boleia
                                    canContinue = true
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

        while (wishlist.isEmpty() && !canContinue) {
            delay(1)
        }

        return wishlist
    }

    suspend fun removeWishList(idUser: String, idBoleia: String): Int {
        var successFail = -1
        db.collection("wishlist")
            .whereEqualTo("idBoleia", idBoleia)
            .whereEqualTo("idUser", idUser)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result.isEmpty) {
                        for (document in task.result) {
                            db.collection("wishlist").document(document.id).delete()
                            successFail = 0
                        }
                    } else {
                        successFail = 1
                    }
                } else {
                    successFail = 2
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }

        while (successFail == -1) {
            delay(1)
        }
        return successFail
    }

    @DelicateCoroutinesApi
    suspend fun evaluateRide(idUser: String, idBoleia: String, evaluation: Float) {
        var ride: QueryDocumentSnapshot? = null
        var data = hashMapOf(
            "idBoleia" to idBoleia,
            "idUser" to idUser,
            "avaliacao" to evaluation
        )

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("boleia_utilizador")
                    .whereEqualTo("idUser", idUser)
                    .whereEqualTo("idBoleia", idBoleia)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                ride = document
                                db.collection("boleia_utilizador").document(ride!!.id)
                                    .set(data, SetOptions.merge())
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                        }
                    }

            }
        }

    }

    @DelicateCoroutinesApi
    suspend fun calculateRideEvaluation(idBoleia: String): Float {
        var sum = 0.0F
        var count = 0
        var rideEvaluation = 0.0F
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("boleia_utilizador")
                    .whereEqualTo("idBoleia", idBoleia)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                sum += document["avaliacao"].toString().toFloat()
                                count++
                            }


                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                        }
                    }
            }
        }
        while (sum == 0.0F) {
            delay(1)
        }
        rideEvaluation = sum / count
        return rideEvaluation


    }

    @DelicateCoroutinesApi
    suspend fun updateRideEvaluation(idBoleia: String, evaluation: Float) {

        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                db.collection("boleia")
                    .whereEqualTo("idBoleia", idBoleia)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                db.collection("boleia").document(document!!.id)
                                    .set(
                                        hashMapOf("avaliacao" to evaluation),
                                        SetOptions.merge()
                                    )
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.exception)
                        }
                    }
            }
        }

    }

}

