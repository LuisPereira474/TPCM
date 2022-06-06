package com.example.tpcm.aplication

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
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


class MapsAtividade : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    private lateinit var location: String

    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest

    private var x=0


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
                /*Log.d("**** SARA", p0.toString())
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                Log.d("**** SARA", lastLocation.toString())
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
                findViewById<TextView>(R.id.txtcoordenadas).setText("Lat: " + loc.latitude + " - Long: " + loc.longitude)
                Log.d("**** SARA", "new location received - " + loc.latitude + " -" + loc.longitude)
                val address = getAddress(lastLocation.latitude, lastLocation.longitude)
                findViewById<TextView>(R.id.txtmorada).setText("Morada: " + address)
                findViewById<TextView>(R.id.txtdistancia).setText("Distância: " + calculateDistance(
                    lastLocation.latitude, lastLocation.longitude,
                    continenteLat, continenteLong).toString())*/
            }
        }

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
        return list[0].getAddressLine(0)
    }


    fun searchPartida(view: View) {

        val textInputLayout = findViewById<EditText>(R.id.ccInputPartida)

        val partida = textInputLayout.text.toString()
        /*searchLocation(partida)*/
        if (partida == null || partida == "") {
            Toast.makeText(applicationContext, "Provide location!", Toast.LENGTH_SHORT).show()
        } else {
            searchLocation(partida)

        }
    }

    fun searchChegada(view: View) {
        val textInputLayout = findViewById<EditText>(R.id.ccInputChegada)

        val destino = textInputLayout.text.toString()
        /*searchLocation(partida)*/
        if (destino == null || destino == "") {
            Toast.makeText(applicationContext, "Provide location!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "$destino", Toast.LENGTH_SHORT).show()

        }
    }


    fun searchLocation(partida: String) {

        lateinit var location: String

        var markerDestino: Marker
        x++

        location = partida
        var addressList: List<Address>? = null

        val geoCoder = Geocoder(this)
        try {

            addressList = geoCoder.getFromLocationName(location, 1)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        val address = addressList!![0]
        val latLng = LatLng(address.latitude, address.longitude)
        if(x!=0){
            markerDestino.remove()
            markerDestino != null
            Toast.makeText(applicationContext, "Provide location!", Toast.LENGTH_SHORT).show()
        }
            markerDestino =  mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            Toast.makeText(applicationContext, "$markerDestino", Toast.LENGTH_SHORT).show()


}
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

}

