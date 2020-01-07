package com.ganesh.nearbyrestaurant.presenters.homepage

import android.app.Activity
import android.content.Context
import android.location.Location
import android.util.Log
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest




class GetCurrentLocationInterfaceImpl(mActivity:Activity) : GetCurrentLocationInterface
{


    /**
     * stop location updates
     */
    override fun onDestroyLocationService() {

        fusedLocationClient.removeLocationUpdates(locationCallback)
    }



    // class members
    private val TAG = "LocationImpl"
    private var activity: Activity = mActivity
    private lateinit var onLocationUpdates: GetCurrentLocationInterface.onLocationUpdates
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest

    /**
     * this function will start the location updates
     */
    override fun startLocationService(onLocationUpdates: GetCurrentLocationInterface.onLocationUpdates) {

        this.onLocationUpdates = onLocationUpdates

        // get the FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        // get the last known location // not in use
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                if (location != null){
                    Log.d(TAG, "startLocationService: ${location.latitude} : ${location.longitude}")
                }


            }


        // create location request object and set params
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setInterval(10 * 1000)
            .setFastestInterval(1 * 1000)


        // location callback to get location updates
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null){
                    val currentLocation = locationResult.lastLocation
                    Log.d(TAG, "onLocationResult: ${currentLocation.latitude} : ${currentLocation.longitude}")
                    onLocationUpdates.onLocationChanged(currentLocation.latitude,currentLocation.longitude)
                }
            }
        }

        // apply looper and request location updates
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
            locationCallback,
            Looper.getMainLooper())






    }










}