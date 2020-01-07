package com.ganesh.nearbyrestaurant.presenters.homepage

import com.ganesh.nearbyrestaurant.models.GeoCodeModel
import com.ganesh.nearbyrestaurant.models.NewLocationModel
import com.ganesh.nearbyrestaurant.models.RestaurantModel
import com.ganesh.nearbyrestaurant.views.homepage.HomePageViewInterface

class HomePagePresenter(
    homePageViewInterface: HomePageViewInterface,
    getCurrentLocationInterface: GetCurrentLocationInterface,
    homePageInteractor: HomePageInteractor
):HomePagePresenterInterface,
    HomePageInteractor.OnLocationChanged,
    HomePageInteractor.OnFinishedListener,
    HomePageInteractor.OnFirstNearByRestaurant,
    GetCurrentLocationInterface.onLocationUpdates{



    // homePageViewInterface to call HomePageActivity's methods
    private var homePageViewInterface:HomePageViewInterface? = homePageViewInterface

    // getCurrentLocationInterface to start and stop location updates
    private var getCurrentLocationInterface:GetCurrentLocationInterface? = getCurrentLocationInterface

    // homePageInteractor to perform search operations
    private var homePageInteractor:HomePageInteractor? = homePageInteractor


    // values to pass for restaurant search
    private var currLatitude:Double = 0.0
    private var currLongitude:Double = 0.0
    private lateinit var entityId:String
    private lateinit var entityType:String

    override fun onPause() {

    }

    /**
     * this will called from activity's onResume()
     */
    override fun onResume() {
        try {
            // start location updates
            getCurrentLocationInterface!!.startLocationService(this)
        } catch (e: Exception) {
        }
    }


    override fun onDestroy() {
        try {
            // break the connectivity with the view
            homePageViewInterface = null
        } catch (e: Exception) {
        }

        try {
            // stop location updates
            getCurrentLocationInterface!!.onDestroyLocationService()
            getCurrentLocationInterface = null
        } catch (e: Exception) {
        }

        try {
            homePageInteractor = null
        } catch (e: Exception) {
        }
    }



    override fun getUserCurrentLocation() {

        try {
            getCurrentLocationInterface!!.startLocationService(this)
        } catch (e: Exception) {
        }
    }

    override fun onFinished(restaurantArrayList: ArrayList<RestaurantModel>) {
        try {
            if (restaurantArrayList.size>0)
                homePageViewInterface!!.setDataOnRecyclerView(restaurantArrayList)
        } catch (e: Exception) {
        }

    }


    override fun onLocationChanged(latitude: Double, longitude: Double) {

        // call the getRestaurantsByGeocode() only for first time
        if (currLatitude==0.0 && currLongitude==0.0){
            currLatitude = latitude
            currLongitude = longitude

            homePageInteractor!!.getRestaurantsByGeocode(currLatitude,currLongitude,this)
        }else{
            // update only lat lon on each location update
            currLatitude = latitude
            currLongitude = longitude
        }

    }


    /**
     * this will called by getRestaurantsByGeocode()
     */
    override fun onFirstNearByRes(geoCodeModel: GeoCodeModel) {

        try {
            val cityName = geoCodeModel.cityName
            val countryName = geoCodeModel.countryName
            val title = geoCodeModel.title

            // update entityId and entityType for first time
            entityId = geoCodeModel.entityID
            entityType = geoCodeModel.entityType

            val completeAddress = " $title $cityName $countryName"
            // show location on view
            homePageViewInterface!!.showLocation(completeAddress)
            // start the text watcher only when location is set
            homePageViewInterface!!.setupTexxtWatcherLocation()
            homePageViewInterface!!.setupTextWatcherRestaurant()
        } catch (e: Exception) {
        }

        try {
            if (geoCodeModel.arrayList.size>0){
                // set nearby restaurant on current location
                homePageViewInterface!!.setDataOnRecyclerView(geoCodeModel.arrayList)
            }
        } catch (e: Exception) {
        }

    }

    override fun onFailureGeoCode(error: String) {

    }


    override fun onNewLocation(arrayList: ArrayList<NewLocationModel>) {

        try {
            if (arrayList.size>0) {
                homePageViewInterface!!.showLocationPopup(arrayList)

                // set entityId && entityType temporarily if user do not select any suggested location
                val firstLocationItem = arrayList[0]
                entityId = firstLocationItem.entityID
                entityType = firstLocationItem.entityType
            }
            else
                homePageViewInterface!!.hideLocationPopup()
        } catch (e: Exception) {
        }

    }

    override fun onNewLocationFailed(error: String) {

        try {
            homePageViewInterface!!.hideLocationPopup()
        } catch (e: Exception) {
        }

    }

    override fun requestNewLocationSearch(query:String) {
        try {
            // request new location search
            homePageInteractor!!.getNewLocation(this,currLatitude,currLongitude,query)
        } catch (e: Exception) {
        }
    }

    override fun onNewLocationSelected(newLocationModel: NewLocationModel) {
        // update entityId & entityType when new location is set
        entityId = newLocationModel.entityID
        entityType = newLocationModel.entityType
    }

    override fun requestSearchFromServer(query: String) {
        try {
            homePageInteractor!!.getRestaurantByKeyword(this,currLatitude,currLongitude,query,entityId,entityType)
        } catch (e: Exception) {
        }
    }

    override fun onFailure(error: String) {

    }

}