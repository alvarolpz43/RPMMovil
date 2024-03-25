package com.rpm.rpmmovil.Routes

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationService {

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun getUserLocation(context: Context): Location? {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val isUserLocaionPermissionGranted = true
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGpsEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )


        if (!isGpsEnabled || !isUserLocaionPermissionGranted) {
            return null
        }

        return suspendCancellableCoroutine { cont ->
            fusedLocationProviderClient.lastLocation.apply {
                if(isComplete){
                    if (isSuccessful){
                        cont.resume(result){}
                    }
                    else{
                        cont.resume(null){}
                    }

                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it){}

                }
                addOnFailureListener {
                    cont.resume(null){}

                }
                addOnCanceledListener {
                    cont.resume(null){}

                }
            }

        }


    }
}