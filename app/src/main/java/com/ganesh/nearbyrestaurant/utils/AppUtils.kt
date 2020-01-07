package com.ganesh.nearbyrestaurant.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


object AppUtils{

    fun checkInternetConnection(context:Context):Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}