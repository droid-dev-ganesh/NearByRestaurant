package com.ganesh.nearbyrestaurant.views.homepage

import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.ganesh.nearbyrestaurant.R
import com.ganesh.nearbyrestaurant.models.RestaurantModel
import com.ganesh.nearbyrestaurant.presenters.homepage.*
import com.ganesh.nearbyrestaurant.utils.AppUtils
import android.location.LocationManager
import android.content.Context
import android.content.Intent
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import android.widget.PopupWindow

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ganesh.nearbyrestaurant.adapters.RestaurantAdapter
import com.ganesh.nearbyrestaurant.models.NewLocationModel
import com.ganesh.nearbyrestaurant.utils.AppConstants
import com.ganesh.nearbyrestaurant.views.webview.WebViewActivity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ganesh.nearbyrestaurant.adapters.LocationAdapter
import android.graphics.drawable.BitmapDrawable


class HomePageActivity : AppCompatActivity(),HomePageViewInterface {


    private lateinit var edtLocation:EditText
    private lateinit var edtRestaurant:EditText
    private lateinit var recyclerViewRestaurant:RecyclerView
    private lateinit var parentConstraintLay:ConstraintLayout
    private lateinit var homePagePresenterInterface: HomePagePresenterInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // bind views
        bindViews()

        homePagePresenterInterface =HomePagePresenter(this,
            GetCurrentLocationInterfaceImpl(this),
            HomePageInteractorImpl()
        )

        if (AppUtils.checkInternetConnection(this)){
            statusCheck()
        }
        else
            Toast.makeText(this,"Please check your internet connection",Toast.LENGTH_SHORT).show()


    }


    private var handlerThreadLocation:HandlerThread? = null
    private lateinit var runnableLocation:Runnable
    /**
     * this function will work similar to debounce() method of rxJava which will wait for 1000ms
     * before making any API call
     */
    private fun onEnterLocation(s:CharSequence){
        try {
            if (handlerThreadLocation != null && handlerThreadLocation!!.isAlive){
                handlerThreadLocation!!.quit()
            }
            handlerThreadLocation = HandlerThread("location_search")
            handlerThreadLocation!!.start()
            val handler = Handler(handlerThreadLocation!!.looper)
            runnableLocation = Runnable {

                homePagePresenterInterface.requestNewLocationSearch(s.toString())
                handlerThreadLocation!!.quit()
                handlerThreadLocation = null
            }

            handler.postDelayed(runnableLocation,1000)
        } catch (e: Exception) {
        }

    }

    private fun bindViews(){
        edtLocation = findViewById(R.id.edtLocation)
        edtRestaurant = findViewById(R.id.edtRestaurant)
        recyclerViewRestaurant = findViewById(R.id.recyclerViewRestaurant)
        parentConstraintLay = findViewById(R.id.parentConstraintLay)
    }

    override fun onResume() {
        super.onResume()

        homePagePresenterInterface.onResume()
    }


    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun setDataOnRecyclerView(arrayList: ArrayList<RestaurantModel>) {

        recyclerViewRestaurant.apply {

            layoutManager = LinearLayoutManager(this@HomePageActivity)
            recyclerViewRestaurant.isNestedScrollingEnabled = false
            recyclerViewRestaurant.setItemViewCacheSize(8)
            recyclerViewRestaurant.setHasFixedSize(true)
            adapter = RestaurantAdapter(arrayList,this@HomePageActivity)

        }

    }

    override fun onResponseFailure() {

    }


    /**
     * check if GPS is enabled
     * if enabled start location updates
     */
    private fun statusCheck() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()

        }else{
            homePagePresenterInterface.getUserCurrentLocation()
        }
    }


    /**
     * function to show alert when GPS is not in active state
     */
    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes"
            ) { dialog, id -> startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("No"
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }


    // global textWatcher variable to use when we want to remove the textChangedListener
    private lateinit var textWatcher: TextWatcher
    override fun setupTexxtWatcherLocation() {
        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length>1){

                    onEnterLocation(s)
                }else{
                    try {
                        handlerThreadLocation!!.quit()
                    } catch (e: Exception) {
                    }
                }
            }
        }
        edtLocation.addTextChangedListener(textWatcher)
    }

    /**
     * set current location on edtLocation
     */
    override fun showLocation(locationString: String) {

        edtLocation.setText(locationString)

    }

    /**
     * call WebViewActivity when click on any recyclerView's item
     */
    fun callWebViewActivity(urlString: String){
        val bundle = Bundle()
        bundle.putString(AppConstants.REDIRECT_URL,urlString)
        val webViewIntent = Intent(this@HomePageActivity,WebViewActivity::class.java)
        webViewIntent.putExtras(bundle)
        startActivity(webViewIntent)
    }

    private var popupWindow:PopupWindow? = null

    override fun showLocationPopup(arrayList: ArrayList<NewLocationModel>) {

        // Initialize a new instance of LayoutInflater service
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate the custom layout/view
        val customView = inflater.inflate(R.layout.layout_popup_location, null)


        popupWindow = PopupWindow(customView, ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT)

        popupWindow!!.setBackgroundDrawable(BitmapDrawable())
        popupWindow!!.isOutsideTouchable = true
        //popupWindow!!.isFocusable = true

        if(Build.VERSION.SDK_INT>=21){
            popupWindow!!.elevation = 10.0f
        }

        val recyclerViewLocation  = customView.findViewById<RecyclerView>(R.id.recyclerViewLocation)

        recyclerViewLocation.apply {


            layoutManager = LinearLayoutManager(this@HomePageActivity)
            recyclerViewLocation.isNestedScrollingEnabled = false
            recyclerViewLocation.setItemViewCacheSize(8)
            recyclerViewLocation.setHasFixedSize(true)
            adapter = LocationAdapter(arrayList,this@HomePageActivity)

        }

        //popupWindow!!.showAsDropDown(parentConstraintLay, 0,edtLocation.layoutParams.height, Gravity.START)
        popupWindow!!.showAtLocation(edtLocation, Gravity.TOP,0,edtLocation.height)
    }

    override fun hideLocationPopup() {
        try {
            if (popupWindow != null && popupWindow!!.isShowing){
                popupWindow!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }

    fun onLocationClicked(newLocationModel: NewLocationModel){

        edtLocation.removeTextChangedListener(textWatcher)
        val title = newLocationModel.title
        val countryName = newLocationModel.countryName
        showLocation("$title $countryName")
        homePagePresenterInterface.onNewLocationSelected(newLocationModel)
        hideLocationPopup()
        setupTexxtWatcherLocation()

    }


    override fun setupTextWatcherRestaurant(){
        edtRestaurant.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length>1){

                    onEnterRestaurant(s)
                }else{
                    try {
                        handlerThreadRestaurant!!.quit()
                    } catch (e: Exception) {
                    }
                }
            }
        })
    }


    private var handlerThreadRestaurant:HandlerThread? = null
    private lateinit var runnableRestaurant:Runnable
    private fun onEnterRestaurant(s:CharSequence){
        try {
            if (handlerThreadRestaurant != null && handlerThreadRestaurant!!.isAlive){
                handlerThreadRestaurant!!.quit()
            }
            handlerThreadRestaurant = HandlerThread("restaurant_search")
            handlerThreadRestaurant!!.start()
            val handler = Handler(handlerThreadRestaurant!!.looper)
            runnableRestaurant = Runnable {

                homePagePresenterInterface.requestSearchFromServer(s.toString())
                handlerThreadRestaurant!!.quit()
                handlerThreadRestaurant = null
            }

            handler.postDelayed(runnableRestaurant,1000)
        } catch (e: Exception) {
        }

    }


    override fun showToast(string: String) {
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }

}
