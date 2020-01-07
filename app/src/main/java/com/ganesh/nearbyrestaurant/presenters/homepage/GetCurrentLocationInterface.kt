package com.ganesh.nearbyrestaurant.presenters.homepage

/**
 * interface to get location update of user using GPS
 */
interface GetCurrentLocationInterface {



    interface onLocationUpdates{
        fun onLocationChanged(latitude:Double,longitude:Double)

    }

    // this function will be called to start location updates
    fun startLocationService(onLocationUpdates: onLocationUpdates)


    // this will be called to stop the location update and to do GC
    fun onDestroyLocationService()

}