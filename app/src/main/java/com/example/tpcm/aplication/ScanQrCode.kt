package com.example.tpcm.aplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val dialog = Dialog(this@ScanQrCode)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                GlobalScope.launch {

                    val result = Connection.scanQrCodeResult(result.contents)
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
        val intent = Intent(this@ScanQrCode, BoleiaCondutor::class.java)
        startActivity(intent)
    }
}