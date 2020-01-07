package com.ganesh.nearbyrestaurant.views.homepage

import com.ganesh.nearbyrestaurant.models.NewLocationModel
import com.ganesh.nearbyrestaurant.models.RestaurantModel

interface HomePageViewInterface {

    fun showProgress()

    fun hideProgress()

    fun setDataOnRecyclerView(arrayList: ArrayList<RestaurantModel>)

    fun onResponseFailure()

    fun showToast(string: String)

    fun showLocation(locationString: String)

    fun showLocationPopup(arrayList: ArrayList<NewLocationModel>)

    fun hideLocationPopup()

    fun setupTexxtWatcherLocation()
    fun setupTextWatcherRestaurant()
}