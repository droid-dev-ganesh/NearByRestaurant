package com.ganesh.nearbyrestaurant.presenters.homepage

import com.ganesh.nearbyrestaurant.models.GeoCodeModel
import com.ganesh.nearbyrestaurant.models.NewLocationModel
import com.ganesh.nearbyrestaurant.models.RestaurantModel

/**
 * HomePage Interactor for getting the restaurant and location updates
 */
interface HomePageInteractor {

    /**
     * this interface is for restaurant search on keyword
     */
     interface OnFinishedListener {
        fun onFinished(restaurantArrayList: ArrayList<RestaurantModel>)
        fun onFailure(error: String)
    }

    /**
     * this interface is to get location detail and near by restaurants using geocode
     */
    interface OnFirstNearByRestaurant{
        fun onFirstNearByRes(geoCodeModel: GeoCodeModel)
        fun onFailureGeoCode(error:String)
    }

    /**
     * this interface will be used to get location detail on keyword search
     */
    interface OnLocationChanged{
        fun onNewLocation(arrayList: ArrayList<NewLocationModel>)
        fun onNewLocationFailed(error:String)
    }

    // this function will be called to search near by restaurants
    fun getRestaurantByKeyword(onFinishedListener: OnFinishedListener,currLatitude: Double,
                               currLongitude: Double,query: String, entityId:String,entityType:String)

    // this function will be called to get geocode information from ZOMATO geocode API
    fun getRestaurantsByGeocode(currLatitude:Double,currLongitude:Double,onFirstNearByRestaurant: OnFirstNearByRestaurant)

    // this function will be called to search location on user entered keyword
    fun getNewLocation(onLocationChanged: OnLocationChanged,currLatitude: Double,currLongitude: Double,query:String)
}