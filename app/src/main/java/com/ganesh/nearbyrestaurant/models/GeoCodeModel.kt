package com.ganesh.nearbyrestaurant.models


data class GeoCodeModel(val cityID:String,val entityID:String,val entityType:String
                        , val title:String, val cityName:String, val countryName:String
                        ,val arrayList: ArrayList<RestaurantModel>)