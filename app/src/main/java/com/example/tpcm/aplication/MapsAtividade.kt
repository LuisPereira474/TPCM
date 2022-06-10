package com.example.tpcm.aplication

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.tpcm.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import kotlin.math.log

const val PARAM_FROM_MAPAS = "from"
const val PARAM_TO_MAPAS = "to"

class MapsAtividade : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    private lateinit var location: String

    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest

    private var markerDestino: Marker? = null
    private var markerPartida: Marker? = null

    private var moradaPartida = ""
    private var moradaChegada = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps_atividade)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
            }
        }

        val edittextPartida = findViewById<View>(R.id.ccInputPartida) as EditText
        edittextPartida.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    searchPartida()
                    return true
                }
                return false
            }
        })

        val edittextChegada = findViewById<View>(R.id.ccInputChegada) as EditText
        edittextChegada.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    searchChegada()
                    return true
                }
                return false
            }
        })

        createLocationRequest()

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val portugal = LatLng(40.003295, -8.086014)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(portugal, 8f))
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }


    private fun createLocationRequest() {
        locationRequest = com.google.android.gms.location.LocationRequest()
// interval specifies the rate at which your app will like to receive updates.
        locationRequest.interval = 10000
        locationRequest.priority =
            com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat, lng, 1)
        val result = list[0].getAddressLine(0)
        return result
    }


    fun searchPartida() {

        val textInputLayout = findViewById<EditText>(R.id.ccInputPartida)

        val partida = textInputLayout.text.toString()
        val destino = ""
        /*searchLocation(partida)*/
        if (partida == null || partida == "") {
            Toast.makeText(
                applicationContext,
                getString(R.string.locationError),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            searchLocation(partida, destino)

        }
    }

    fun searchChegada() {
        val textInputLayout = findViewById<EditText>(R.id.ccInputChegada)
        val destino = textInputLayout.text.toString()
        val partida = ""
        if (destino == null || destino == "") {
            Toast.makeText(
                applicationContext,
                getString(R.string.locationError),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            searchLocation(partida, destino)
        }
    }

    fun searchLocation(partida: String, destino: String) {

        if (destino == "") {
            val location: String = partida
            val partidaList: List<Address>?

            val geoCoder = Geocoder(this)
            try {
                partidaList = geoCoder.getFromLocationName(location, 1)
                if (partidaList.isNotEmpty()) {
                    val addressPartida = partidaList!![0]
                    val latLng = LatLng(addressPartida.latitude, addressPartida.longitude)
                    if (markerPartida != null) {
                        markerPartida?.remove()
                    }
                    val morada = getAddress(addressPartida.latitude, addressPartida.longitude)
                    moradaPartida = morada + "-" + addressPartida.featureName
                    markerPartida = mMap!!.addMarker(MarkerOptions().position(latLng).title(morada))

                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.locationError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (partida == "") {
            val location: String = destino
            val destinoList: List<Address>?

            val geoCoder = Geocoder(this)
            try {
                destinoList = geoCoder.getFromLocationName(location, 1)
                if (destinoList.isNotEmpty()) {
                    val addressDestino = destinoList!![0]

                    val latLng = LatLng(addressDestino.latitude, addressDestino.longitude)
                    if (markerDestino != null) {
                        markerDestino?.remove()
                    }
                    val morada = getAddress(addressDestino.latitude, addressDestino.longitude)
                    moradaChegada = morada + "-" + addressDestino.featureName
                    markerDestino = mMap!!.addMarker(
                        MarkerOptions().position(latLng).title(morada)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )

                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.locationError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }
//FUNÇAO PARA MUDAR ICON DO MARKER
//
//    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
//        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
//        vectorDrawable!!.setBounds(
//            0,
//            0,
//            vectorDrawable.intrinsicWidth,
//            vectorDrawable.intrinsicHeight
//        )
//        val bitmap = Bitmap.createBitmap(
//            vectorDrawable.intrinsicWidth,
//            vectorDrawable.intrinsicHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        vectorDrawable.draw(canvas)
//        return BitmapDescriptorFactory.fromBitmap(bitmap)
//    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("**** TPCM", "onPause - removeLocationUpdates")
    }

    public override fun onResume() {
        super.onResume()
        startLocationUpdates()
        Log.d("**** TPCM", "onResume - startLocationUpdates")
    }

    fun sendToCreateRide(view: View) {
        if (moradaPartida.isNotEmpty() && moradaChegada.isNotEmpty()) {
            val intent = Intent(this@MapsAtividade, CriarBoleia::class.java).apply {
                putExtra(PARAM_FROM_MAPAS, moradaPartida)
                putExtra(PARAM_TO_MAPAS, moradaChegada)
            }
            startActivity(intent)
        }
    }


}

