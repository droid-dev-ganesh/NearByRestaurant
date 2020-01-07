package com.ganesh.nearbyrestaurant.utils

// App Constant class
object AppConstants {
    private const val TOKEN_KEY = "54159cf053623a3aa9337924aa519883"

    fun getTokenKey():String{
        return TOKEN_KEY
    }

    const val REDIRECT_URL = "redirect_url"
}