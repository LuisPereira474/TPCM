package com.example.tpcm.aplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcm.R
import com.example.tpcm.database.Connection
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScanQrCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr_code)

        val qrButton: Button = findViewById(R.id.qr_button)
        qrButton.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.captureActivity = CaptureActivityPortrait::class.java
            intentIntegrator.setDesiredBarcodeFormats(listOf(IntentIntegrator.QR_CODE))
            intentIntegrator.initiateScan()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_search -> {
                val intent = Intent(this@ScanQrCode, SearchBoleia::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_rides -> {
                val intent = Intent(this@ScanQrCode, HistBoleiasAceites::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_services -> {
                val intent = Intent(this@ScanQrCode, HistoricoUser::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_profile -> {
                val intent = Intent(this@ScanQrCode, Perfil::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val dialog = Dialog(this@ScanQrCode)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                GlobalScope.launch {
                    val idBoleia = intent.getStringExtra(PARAM_ID_BOLEIA)
                    val result = Connection.scanQrCodeResult(result.contents, idBoleia.toString())
                    runOnUiThread {
                        if (result) {
                            dialog.setContentView(R.layout.dialog_right_qrcode)
                            dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_background))
                            dialog.window?.setLayout(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            dialog.setCancelable(false)
                            dialog.show()
                            dialog.findViewById<TextView>(R.id.iconClosePopUpRightQr)
                                .setOnClickListener {
                                    dialog.dismiss()
                                }

                        } else {
                            dialog.setContentView(R.layout.dialog_wrong_qrcode)
                            dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_background))
                            dialog.window?.setLayout(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            dialog.setCancelable(false)
                            dialog.show()
                            dialog.findViewById<TextView>(R.id.iconClosePopUpWrongQr)
                                .setOnClickListener {
                                    dialog.dismiss()
                                }
                        }
                    }
                }
            }
        }
    }

    fun backScanPage(view: View) {
        val intent = Intent(this@ScanQrCode, HistoricoUser::class.java)
        startActivity(intent)
    }
}