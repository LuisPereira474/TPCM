package com.example.tpcm.aplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.firebase.firestore.QueryDocumentSnapshot
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        getProfile()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this@Perfil, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@Perfil, HistBoleiasAceites::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@Perfil, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@Perfil, Perfil::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun getProfile() {

        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()
        val perfilName = findViewById<TextView>(R.id.perfilName)
        val perfilEmail = findViewById<TextView>(R.id.perfilEmail)
        val points = findViewById<TextView>(R.id.points)
        val perfilAvatar = findViewById<CircleImageView>(R.id.perfilAvatar)

        var profile: QueryDocumentSnapshot?
        GlobalScope.launch {
            profile = Connection.getProfileUser(idUser)
            runOnUiThread {
                perfilName.text = profile!!.data["nome"].toString()
                perfilEmail.text = profile!!.data["email"].toString()
                points.text = profile!!.data["pontos"].toString()
                if (profile!!.data["sexo"] == true) {
                    perfilAvatar.setImageResource(R.drawable.avatar_boy)
                } else {
                    perfilAvatar.setImageResource(R.drawable.avatar_girl)
                }
            }
        }
    }

    fun editProfile(view: View) {
        val intent = Intent(this@Perfil, EditarPerfil::class.java)
        startActivity(intent)
    }

    fun sendToWishlist(view: View) {
        val intent = Intent(this@Perfil, WishList::class.java)
        startActivity(intent)
    }

    fun makeMeDriver(view: View) {
        val intent = Intent(this@Perfil, MakeMeDriver::class.java)
        startActivity(intent)
    }

    private fun createDialog() {
        val dialog = Dialog(this@Perfil)
        dialog.setContentView(R.layout.dialog_list_awards)
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_background))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.show()

        dialog.findViewById<TextView>(R.id.iconCloseListAwards).setOnClickListener {
            dialog.dismiss()
        }

    }

    fun listAwards(view: View) {
        runOnUiThread {
            createDialog()
        }
    }
}