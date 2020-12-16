package com.pointlessapps.st_lab

import android.Manifest
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 123
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var rotateAnim: AnimatorSet

    private var trackingEnabled = false

    private val locationRequest: LocationRequest
        get() = LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    private val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            if (trackingEnabled) {
                textview_location.text = applicationContext.getString(
                    R.string.location_text,
                    result?.lastLocation?.latitude,
                    result?.lastLocation?.longitude,
                    result?.lastLocation?.time
                )
                GlobalScope.launch(Dispatchers.IO) {
                    Geocoder(applicationContext, Locale.getDefault()).runCatching {
                        val addresses = getFromLocation(result?.lastLocation?.latitude ?: 0.0, result?.lastLocation?.longitude ?: 0.0, 1)
                        if (addresses.isNullOrEmpty()) {
                            throw Exception("No address found")
                        }

                        (0..addresses.first().maxAddressLineIndex).joinToString(
                            "\n",
                            transform = addresses.first()::getAddressLine
                        )
                    }.onFailure {
                        Log.e("LOG!", "Error", it)
                    }.onSuccess {
                        textview_location.text = applicationContext.getString(
                            R.string.address_text,
                            it,
                            System.currentTimeMillis()
                        )
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        rotateAnim =
            AnimatorInflater.loadAnimator(applicationContext, R.animator.rotate) as AnimatorSet
        rotateAnim.setTarget(imageview_android)

        button_location.setOnClickListener {
            if (!trackingEnabled) {
                startTrackingLocation()
            } else {
                stopTrackingLocation()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            startTrackingLocation()

            return
        }

        Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT).show()
    }

    private fun startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )

            return
        }

        trackingEnabled = true
        button_location.setText(R.string.stop_tracking_location)
        rotateAnim.start()

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, null)
    }

    private fun stopTrackingLocation() {
        trackingEnabled = false
        button_location.setText(R.string.start_tracking_location)
        rotateAnim.end()

        fusedLocationProviderClient.removeLocationUpdates(callback)
    }

    override fun onResume() {
        super.onResume()
        if (trackingEnabled) {
            startTrackingLocation()
        }
    }

    override fun onPause() {
        super.onPause()
        if (trackingEnabled) {
            stopTrackingLocation()
            trackingEnabled = true
        }
    }
}
