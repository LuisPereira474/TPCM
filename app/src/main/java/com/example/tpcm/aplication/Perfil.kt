package com.example.tpcm.aplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.firebase.firestore.QueryDocumentSnapshot
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

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
                when {
                    profile!!.data["sexo"].toString().toInt() == 1 -> {
                        perfilAvatar.setImageResource(R.drawable.avatar_boy)
                    }
                    profile!!.data["sexo"].toString().toInt() == 2 -> {
                        perfilAvatar.setImageResource(R.drawable.avatar_girl)
                    }
                    else -> {
                        perfilAvatar.setImageResource(R.drawable.avatar_other)
                    }
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

        val shared = getSharedPreferences("idUser", MODE_PRIVATE)
        val idUser = shared.getString("idUser", "").toString()

        val dialog = Dialog(this@Perfil)
        dialog.setContentView(R.layout.dialog_list_awards)
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_background))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)

        dialog.findViewById<CheckBox>(R.id.checkFirst).isChecked = false
        dialog.findViewById<CheckBox>(R.id.checkSecond).isChecked = false
        dialog.findViewById<CheckBox>(R.id.checkThird).isChecked = false
        dialog.findViewById<CheckBox>(R.id.checkFourth).isChecked = false

        dialog.show()
        dialog.findViewById<CheckBox>(R.id.checkFirst).setOnClickListener {
            dialog.findViewById<CheckBox>(R.id.checkSecond).isChecked = false
            dialog.findViewById<CheckBox>(R.id.checkThird).isChecked = false
            dialog.findViewById<CheckBox>(R.id.checkFourth).isChecked = false
        }
        dialog.findViewById<CheckBox>(R.id.checkSecond).setOnClickListener {
            dialog.findViewById<CheckBox>(R.id.checkFirst).isChecked = false
            dialog.findViewById<CheckBox>(R.id.checkThird).isChecked = false
            dialog.findViewById<CheckBox>(R.id.checkFourth).isChecked = false
        }
        dialog.findViewById<CheckBox>(R.id.checkThird).setOnClickListener {
            dialog.findViewById<CheckBox>(R.id.checkFirst).isChecked = false
            dialog.findViewById<CheckBox>(R.id.checkSecond).isChecked = false
            dialog.findViewById<CheckBox>(R.id.checkFourth).isChecked = false
        }
        dialog.findViewById<CheckBox>(R.id.checkFourth).setOnClickListener {
            dialog.findViewById<CheckBox>(R.id.checkFirst).isChecked = false
            dialog.findViewById<CheckBox>(R.id.checkSecond).isChecked = false
            dialog.findViewById<CheckBox>(R.id.checkThird).isChecked = false
        }

        dialog.findViewById<TextView>(R.id.iconCloseListAwards).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.listAwardsConfirmBtt).setOnClickListener {
            if (!dialog.findViewById<CheckBox>(R.id.checkFirst).isChecked
                && !dialog.findViewById<CheckBox>(R.id.checkSecond).isChecked
                && !dialog.findViewById<CheckBox>(R.id.checkThird).isChecked
                && !dialog.findViewById<CheckBox>(R.id.checkFourth).isChecked
            ) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.noPrizesSelected),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (dialog.findViewById<CheckBox>(R.id.checkFirst).isChecked) {
                GlobalScope.launch {
                    when (Connection.spendPoints(idUser, 20)) {
                        1 -> {
                            runOnUiThread {
                                dialog.dismiss()
                                createDialogCodigo()
                            }
                        }
                        2 -> {
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.erroPointsPrize),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else -> {
                            runOnUiThread {
                                Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else if (dialog.findViewById<CheckBox>(R.id.checkSecond).isChecked) {
                GlobalScope.launch {
                    when (Connection.spendPoints(idUser, 50)) {
                        1 -> {
                            runOnUiThread {
                                dialog.dismiss()
                                createDialogCodigo()
                            }
                        }
                        2 -> {
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.erroPointsPrize),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else -> {
                            runOnUiThread {
                                Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else if (dialog.findViewById<CheckBox>(R.id.checkThird).isChecked) {
                GlobalScope.launch {
                    when (Connection.spendPoints(idUser, 100)) {
                        1 -> {
                            runOnUiThread {
                                dialog.dismiss()
                                createDialogCodigo()
                            }
                        }
                        2 -> {
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.erroPointsPrize),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else -> {
                            runOnUiThread {
                                Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else if (dialog.findViewById<CheckBox>(R.id.checkFourth).isChecked) {
                GlobalScope.launch {
                    when (Connection.spendPoints(idUser, 300)) {
                        1 -> {
                            runOnUiThread {
                                dialog.dismiss()
                                createDialogCodigo()
                            }
                        }
                        2 -> {
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.erroPointsPrize),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else -> {
                            runOnUiThread {
                                Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

    }

    private fun createDialogCodigo() {
        val dialog = Dialog(this@Perfil)
        dialog.setContentView(R.layout.diolog_code_award)
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_background))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.findViewById<TextView>(R.id.titleDescription).text = getString(R.string.prizeCode)
        dialog.findViewById<TextView>(R.id.subtitleDescription).text = getString(R.string.prizeCodeRedeem)
        dialog.findViewById<TextView>(R.id.code).text = UUID.randomUUID().toString().take(26)
        dialog.setCancelable(false)
        dialog.show()

        dialog.findViewById<TextView>(R.id.iconCloseCodeAward).setOnClickListener {
            dialog.dismiss()
            finish();
            startActivity(intent);
        }

    }

    fun listAwards(view: View) {
        runOnUiThread {
            createDialog()
        }
    }
}