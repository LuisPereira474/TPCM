package com.example.tpcm.aplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcm.MainActivity
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        checkboxMasculino.isChecked = false
        checkboxFeminino.isChecked = false
        checkboxOther.isChecked = false

        checkboxMasculino.setOnClickListener {
            checkboxFeminino.isChecked = false
            checkboxOther.isChecked = false
        }
        checkboxFeminino.setOnClickListener {
            checkboxMasculino.isChecked = false
            checkboxOther.isChecked = false
        }
        checkboxOther.setOnClickListener {
            checkboxMasculino.isChecked = false
            checkboxFeminino.isChecked = false
        }
    }

    fun loginPage(view: View) {
        val intent = Intent(this@SignUp, MainActivity::class.java)
        startActivity(intent)
    }

    fun signUpSubmit(view: View) {
        val password = findViewById<EditText>(R.id.ccInputPasse).text.toString()
        val confPassword = findViewById<EditText>(R.id.ccInputConfirmPasse).text.toString()
        val email = findViewById<EditText>(R.id.ccInputEmail).text.toString()
        val nome = findViewById<EditText>(R.id.ccInputName).text.toString()
        val erroSignUpPass = findViewById<TextView>(R.id.erroSignUpPass)
        val erroSignUpEmail = findViewById<TextView>(R.id.erroSignUpEmail)
        val errorMissingFields = findViewById<TextView>(R.id.errorMissingFields)
        val errorInvalidEmail = findViewById<TextView>(R.id.errorInvalidEmail)
        erroSignUpPass.visibility = View.INVISIBLE;
        erroSignUpEmail.visibility = View.INVISIBLE;
        errorMissingFields.visibility = View.INVISIBLE;
        errorInvalidEmail.visibility = View.INVISIBLE;
        errorNoCheckSex.visibility = View.INVISIBLE;

        if (email.isEmpty() || password.isEmpty() || nome.isEmpty()) {
            errorMissingFields.visibility = View.VISIBLE;
        } else if (!checkboxMasculino.isChecked && !checkboxFeminino.isChecked && !checkboxOther.isChecked) {
            errorNoCheckSex.visibility = View.VISIBLE
        } else if (password != confPassword) {
            erroSignUpPass.visibility = View.VISIBLE;
        } else {
            GlobalScope.launch {
                var gender = 0
                when {
                    checkboxMasculino.isChecked -> {
                        gender = 1
                    }
                    checkboxFeminino.isChecked -> {
                        gender = 2
                    }
                    checkboxOther.isChecked -> {
                        gender = 3
                    }
                }
                val idUser =
                    Connection.singUp(
                        email,
                        nome,
                        password,
                        erroSignUpEmail,
                        errorInvalidEmail,
                        gender
                    )
                if (idUser != "") {
                    val sharedPreferences: SharedPreferences =
                        getSharedPreferences("idUser", Context.MODE_PRIVATE)
                    sharedPreferences.edit()
                        .clear()
                        .apply()
                    sharedPreferences.edit()
                        .putString("idUser", idUser)
                        .apply()
                    val intent = Intent(this@SignUp, SearchBoleia::class.java)
                    startActivity(intent)
                }else{
                    runOnUiThread {
                        errorInvalidEmail.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}