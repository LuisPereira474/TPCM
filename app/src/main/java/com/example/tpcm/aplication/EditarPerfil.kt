package com.example.tpcm.aplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.firebase.firestore.QueryDocumentSnapshot
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditarPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)
        getProfile()
    }

    private fun getProfile() {

        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()

        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPerfilAvatar = findViewById<CircleImageView>(R.id.editPerfilAvatar)

        var profile: QueryDocumentSnapshot?
        GlobalScope.launch {
            profile = Connection.getProfileUser(idUser)
            runOnUiThread {
                editName.setText(profile!!.data["nome"].toString(), TextView.BufferType.EDITABLE)
                editEmail.setText(profile!!.data["email"].toString(), TextView.BufferType.EDITABLE)

                if(profile!!.data["sexo"]==true){
                    editPerfilAvatar.setImageResource(R.drawable.avatar_boy)
                }else{
                    editPerfilAvatar.setImageResource(R.drawable.avatar_girl)
                }
            }
        }
    }

    fun goProfile(view: View) {
        val intent = Intent(this@EditarPerfil, Perfil::class.java)
        startActivity(intent)
    }

    fun editProfile(view: View) {
        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()

        val errorMissingFields = findViewById<TextView>(R.id.errorMissingFieldsEditarPerfil)

        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmail)

        errorMissingFields.visibility = View.INVISIBLE;

        if (editName.text.isEmpty() || editEmail.text.isEmpty()) {
            errorMissingFields.visibility = View.VISIBLE;
        } else {
            GlobalScope.launch {
                Connection.editProfile(idUser,editName.text.toString(),editEmail.text.toString())
                val intent = Intent(this@EditarPerfil, Perfil::class.java)
                startActivity(intent)
            }
        }
    }
}