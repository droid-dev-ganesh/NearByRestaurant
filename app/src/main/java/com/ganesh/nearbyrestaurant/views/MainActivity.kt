package com.ganesh.nearbyrestaurant.views


import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.ganesh.nearbyrestaurant.R
import com.ganesh.nearbyrestaurant.views.homepage.HomePageActivity
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.widget.Toast

import androidx.core.app.ActivityCompat










class MainActivity : AppCompatActivity() {


    private val PERMISSION_REQUEST_CODE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkPermission()){
            callActivity()
        }else{
            requestPermission()
        }

    }

    private fun callActivity(){
        Handler().postDelayed({
            // This method will be executed once the timer is over
            val intentSample1 = Intent(this@MainActivity, HomePageActivity::class.java)
            startActivity(intentSample1)
            finish()
        }, 2000)
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermission() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_REQUEST_CODE ->{
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    callActivity()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"This App require location permission. Please enable to use the app service",Toast.LENGTH_SHORT).show()
                }
                return
            }
            else->{

            }
        }
    }
}
