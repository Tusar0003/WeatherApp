package com.ahoy.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class LocationUtils(private val context: Context) {

    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationRequest: LocationRequest = LocationRequest.create()

    private var fusedLocationClient: FusedLocationProviderClient

    fun trackLocation(gpsListener: GpsListener){
        turnGPSOn(gpsListener)
    }

    private fun turnGPSOn(gpsListener: GpsListener) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation(gpsListener)
        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(
                    (context as Activity),
                    OnSuccessListener {
                        getLocation(gpsListener)
                    })
                .addOnFailureListener(context, OnFailureListener { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val intentSenderRequest = IntentSenderRequest.Builder((e as ResolvableApiException).resolution).build()
                            gpsListener.onRequestGPSSetting(intentSenderRequest)
                        } catch (sie: SendIntentException) {
                            gpsListener.onGPSNotAvailable(sie)
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            gpsListener.onGPSNotAvailable(null)
                        }
                        else -> {
                            gpsListener.onGPSNotAvailable(null)
                        }
                    }
                })
        }
    }

    private fun getLocation(gpsListener: GpsListener){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            gpsListener.onLocationPermissionNotAvailable()
            return
        }

        gpsListener.onLocationTrackingInProgress()

        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
            .addOnFailureListener {
                gpsListener.onLocationTrackingFailed(it)
            }
            .addOnCanceledListener {
                gpsListener.onLocationTrackingFailed(null)
            }
            .addOnSuccessListener { location ->
                if(location != null){
                    gpsListener.onLocationFound(location)
                }else{
                    gpsListener.onLocationTrackingFailed(null)
                }
            }
    }

    interface GpsListener {
        fun onRequestGPSSetting(intentSenderRequest: IntentSenderRequest)
        fun onGPSNotAvailable(exception: Exception?)
        fun onLocationTrackingInProgress()
        fun onLocationPermissionNotAvailable()
        fun onLocationTrackingFailed(exception: Exception?)
        fun onLocationFound(location: Location)
    }

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }
}