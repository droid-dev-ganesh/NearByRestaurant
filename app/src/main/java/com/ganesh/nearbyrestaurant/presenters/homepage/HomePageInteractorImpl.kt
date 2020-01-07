package com.ganesh.nearbyrestaurant.presenters.homepage

import com.androidnetworking.error.ANError
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.ganesh.nearbyrestaurant.models.GeoCodeModel
import com.ganesh.nearbyrestaurant.models.NewLocationModel
import com.ganesh.nearbyrestaurant.models.RestaurantModel
import com.ganesh.nearbyrestaurant.utils.AppConstants
import org.json.JSONObject


class HomePageInteractorImpl : HomePageInteractor{


    /**
     * this overriden function will get all the near by restaurants with search query and will call
     * onFinishedListener.onFinished() method
     */
    override fun getRestaurantByKeyword(
        onFinishedListener: HomePageInteractor.OnFinishedListener,
        currLatitude: Double,
        currLongitude: Double,
        query: String,
        entityId:String,
        entityType:String
    ) {
        AndroidNetworking.get("https://developers.zomato.com/api/v2.1/search?")
            .addQueryParameter("lat", currLatitude.toString())
            .addQueryParameter("lon", currLongitude.toString())
            .addQueryParameter("entity_id", entityId)
            .addQueryParameter("entity_type", entityType)
            .addQueryParameter("sort", "real_distance")
            .addQueryParameter("order", "asc")
            .addQueryParameter("radius", "5000")
            .addQueryParameter("q", query)
            .addQueryParameter("count", "50")
            .addHeaders("user-key", AppConstants.getTokenKey())
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response != null){


                        val restaurantArray = response.getJSONArray("restaurants")
                        val arrayList: ArrayList<RestaurantModel> = ArrayList()
                        for (index in 0 until restaurantArray.length()){
                            val restaurantObject = restaurantArray.getJSONObject(index).getJSONObject("restaurant")

                            // basic restaurant detail
                            val resId = restaurantObject.getString("id")
                            val resName = restaurantObject.getString("name")
                            val cuisines = restaurantObject.getString("cuisines")
                            val avgCostForTwo = restaurantObject.getString("average_cost_for_two")
                            val currency = restaurantObject.getString("currency")

                            // user rating
                            val userRatingObject = restaurantObject.getJSONObject("user_rating")
                            val aggregateRating = userRatingObject.getString("aggregate_rating")
                            val ratingText = userRatingObject.getString("rating_text")
                            val ratingColor = try {
                                userRatingObject.getString("ratingColor")
                            } catch (e: Exception) {
                                ""
                            }

                            val phoneNumber = try {
                                restaurantObject.getString("phone_numbers")
                            } catch (e: Exception) {
                                ""
                            }
                            val votes = userRatingObject.getString("votes")

                            // urls
                            val redirectUrl = restaurantObject.getString("url")
                            val thumbImageUrl = restaurantObject.getString("thumb")


                            // address details
                            val addressObject = restaurantObject.getJSONObject("location")

                            val addresString = addressObject.getString("address")
                            val locality = addressObject.getString("locality")
                            val city = addressObject.getString("city")
                            val zipcode = addressObject.getString("zipcode")
                            val completeAddress = "$addresString $locality\n$city $zipcode"

                            val restaurantModel = RestaurantModel(resId,name = resName)
                            restaurantModel.cuisines = cuisines
                            restaurantModel.avgCostForTwo = avgCostForTwo
                            restaurantModel.currency = currency
                            restaurantModel.aggregateRating = aggregateRating
                            restaurantModel.ratingText = ratingText
                            restaurantModel.ratingColor = ratingColor
                            restaurantModel.votes = votes
                            restaurantModel.redirectUrl = redirectUrl
                            restaurantModel.address = completeAddress
                            restaurantModel.thumbImageUrl = thumbImageUrl
                            restaurantModel.phoneNumbers = phoneNumber
                            arrayList.add(restaurantModel)

                        }



                        if (arrayList.size>0){
                            onFinishedListener.onFinished(arrayList)
                        }




                    }else{
                        onFinishedListener.onFailure("Null Response")
                    }
                }

                override fun onError(anError: ANError?) {
                    onFinishedListener.onFailure(anError.toString())
                }
            })
    }


    /**
     * this overriden method will get the location details using lat lon and it will call
     * onFirstNearByRestaurant.onFirstNearByRes() on a successful fetch
     */
    override fun getRestaurantsByGeocode(currLatitude: Double, currLongitude: Double,onFirstNearByRestaurant: HomePageInteractor.OnFirstNearByRestaurant) {

        AndroidNetworking.get("https://developers.zomato.com/api/v2.1/geocode?")
            .addQueryParameter("lat", currLatitude.toString())
            .addQueryParameter("lon", currLongitude.toString())
            .addHeaders("user-key", AppConstants.getTokenKey())
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response != null){
                        val locationObject = response.getJSONObject("location")
                        val nearByRestaurant = response.getJSONArray("nearby_restaurants")
                        val arrayList : ArrayList<RestaurantModel> = ArrayList()

                        for (index in 0 until nearByRestaurant.length()){
                            val restaurantObject = nearByRestaurant.getJSONObject(index).getJSONObject("restaurant")

                            // basic restaurant detail
                            val resId = restaurantObject.getString("id")
                            val resName = restaurantObject.getString("name")
                            val cuisines = restaurantObject.getString("cuisines")
                            val avgCostForTwo = restaurantObject.getString("average_cost_for_two")
                            val currency = restaurantObject.getString("currency")

                            // user rating
                            val userRatingObject = restaurantObject.getJSONObject("user_rating")
                            val aggregateRating = userRatingObject.getString("aggregate_rating")
                            val ratingText = userRatingObject.getString("rating_text")
                            val ratingColor = try {
                                userRatingObject.getString("ratingColor")
                            } catch (e: Exception) {
                                ""
                            }


                            val votes = userRatingObject.getString("votes")

                            // urls
                            val redirectUrl = restaurantObject.getString("url")
                            val thumbImageUrl = restaurantObject.getString("thumb")


                            // address details
                            val addressObject = restaurantObject.getJSONObject("location")

                            val addresString = addressObject.getString("address")
                            val locality = addressObject.getString("locality")
                            val city = addressObject.getString("city")
                            val zipcode = addressObject.getString("zipcode")
                            val completeAddress = "$addresString $locality\n$city $zipcode"

                            val restaurantModel = RestaurantModel(resId,name = resName)
                            restaurantModel.cuisines = cuisines
                            restaurantModel.avgCostForTwo = avgCostForTwo
                            restaurantModel.currency = currency
                            restaurantModel.aggregateRating = aggregateRating
                            restaurantModel.ratingText = ratingText
                            restaurantModel.ratingColor = ratingColor
                            restaurantModel.votes = votes
                            restaurantModel.redirectUrl = redirectUrl
                            restaurantModel.address = completeAddress
                            restaurantModel.thumbImageUrl = thumbImageUrl
                            arrayList.add(restaurantModel)


                        }


                        if (arrayList.size>0){
                            val cityId = locationObject.getString("city_id")
                            val entityID = locationObject.getString("entity_id")
                            val entityType = locationObject.getString("entity_type")
                            val title = locationObject.getString("title")
                            val cityName = locationObject.getString("city_name")
                            val countryName = locationObject.getString("country_name")
                            val geoCodeModel = GeoCodeModel(cityID = cityId,
                                entityID = entityID,
                                entityType = entityType,
                                title = title,
                                cityName = cityName,
                                countryName = countryName,
                                arrayList = arrayList
                            )
                            onFirstNearByRestaurant.onFirstNearByRes(geoCodeModel)
                        }else{
                            val cityId = locationObject.getString("city_id")
                            val entityID = locationObject.getString("entity_id")
                            val entityType = locationObject.getString("entity_type")
                            val title = locationObject.getString("title")
                            val cityName = locationObject.getString("city_name")
                            val countryName = locationObject.getString("country_name")
                            val geoCodeModel = GeoCodeModel(cityID = cityId,
                                entityID = entityID,
                                entityType = entityType,
                                title = title,
                                cityName = cityName,
                                countryName = countryName,
                                arrayList = arrayList
                            )

                            onFirstNearByRestaurant.onFirstNearByRes(geoCodeModel)
                        }


                    }else{
                        onFirstNearByRestaurant.onFailureGeoCode("Null Response")
                    }
                }

                override fun onError(anError: ANError?) {
                    onFirstNearByRestaurant.onFailureGeoCode(anError.toString())
                }
            })

    }


    /**
     * this function will get the location detail on query keyword
     */
    override fun getNewLocation(
        onLocationChanged: HomePageInteractor.OnLocationChanged,
        currLatitude: Double,
        currLongitude: Double,
        query: String
    ) {

        AndroidNetworking.get("https://developers.zomato.com/api/v2.1/locations?")
            .addQueryParameter("lat", currLatitude.toString())
            .addQueryParameter("lon", currLongitude.toString())
            .addQueryParameter("query", query)
            .addQueryParameter("count", "5")
            .addHeaders("user-key", AppConstants.getTokenKey())
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response != null){

                        if (response.getString("status") == "success"){
                            val locationObject = response.getJSONArray("location_suggestions")
                            val arrayList:ArrayList<NewLocationModel> = ArrayList()
                            for (index in 0 until locationObject.length()){
                                try {
                                    val jsonObject = locationObject.getJSONObject(index)
                                    val cityId = jsonObject.getString("city_id")
                                    val entityID = jsonObject.getString("entity_id")
                                    val entityType = jsonObject.getString("entity_type")
                                    val title = jsonObject.getString("title")
                                    val cityName = jsonObject.getString("city_name")
                                    val countryName = jsonObject.getString("country_name")

                                    val newLocationModel = NewLocationModel(cityID = cityId,
                                        entityID = entityID,
                                        entityType = entityType,
                                        title = title,
                                        cityName = cityName,
                                        countryName = countryName
                                    )
                                    arrayList.add(newLocationModel)
                                } catch (e: Exception) {
                                    continue
                                }
                            }

                            if (arrayList.size>0){
                                onLocationChanged.onNewLocation(arrayList)
                            }

                        }






                    }else{
                        onLocationChanged.onNewLocationFailed("Null Response")
                    }
                }

                override fun onError(anError: ANError?) {
                    onLocationChanged.onNewLocationFailed(anError.toString())
                }
            })

    }

}