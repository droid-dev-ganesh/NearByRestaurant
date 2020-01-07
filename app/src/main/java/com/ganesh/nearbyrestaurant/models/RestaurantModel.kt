package com.ganesh.nearbyrestaurant.models

data class RestaurantModel(val resId:String, val name:String){

    var cuisines:String = ""
    var avgCostForTwo:String = ""
    var currency:String = ""
    var aggregateRating:String = ""
    var ratingText:String = ""
    var ratingColor:String = ""
    var votes:String = ""
    var phoneNumbers:String = ""
    var redirectUrl:String = ""
    var address:String = ""
    var thumbImageUrl:String = ""
}