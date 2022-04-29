package com.example.tpcm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class MainActivity : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun loginSubmit(view: View) {
        val email = findViewById<EditText>(R.id.inputEmail).text
        val password = findViewById<EditText>(R.id.inputPass).text
        db.collection("utilizador")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if ((document.data["email"] as String) == email.toString() && (document.data["password"] as String) == password.toString()){
                            Log.d("TAG", (document.data["idUser"] as String?).toString())
                        }

                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            })
    }

    fun signUpPage(view: View) {
        val intent = Intent(this@MainActivity, SignUp::class.java)
        startActivity(intent)
    }
}