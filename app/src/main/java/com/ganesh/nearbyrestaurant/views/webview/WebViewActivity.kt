package com.ganesh.nearbyrestaurant.views.webview

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.ganesh.nearbyrestaurant.R
import com.ganesh.nearbyrestaurant.utils.AppConstants

/**
 * activity to show restaurant detail page in webView
 */
class WebViewActivity : AppCompatActivity() {

    private lateinit var progressBar:ProgressBar
    private lateinit var webView:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val redirectUrl = try {
            intent.extras.getString(AppConstants.REDIRECT_URL)
        } catch (e: Exception) {
            "https://www.zomato.com/mumbai"
        }




        progressBar = findViewById(R.id.progressBar)
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                hideProgress()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showProgress()
            }
        }
        webView.loadUrl(redirectUrl)
    }


    private fun showProgress(){

        progressBar.visibility = View.VISIBLE
        webView.visibility = View.INVISIBLE
    }

    private fun hideProgress(){
        webView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }
}
