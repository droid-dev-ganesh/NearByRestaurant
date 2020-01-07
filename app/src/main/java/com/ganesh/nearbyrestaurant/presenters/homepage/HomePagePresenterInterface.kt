package com.ganesh.nearbyrestaurant.presenters.homepage

import com.ganesh.nearbyrestaurant.models.NewLocationModel

interface HomePagePresenterInterface {

    fun onDestroy()

    fun onPause()

    fun onResume()

    fun requestSearchFromServer(query: String)

    fun getUserCurrentLocation()

    fun requestNewLocationSearch(query:String)

    fun onNewLocationSelected(newLocationModel: NewLocationModel)
}