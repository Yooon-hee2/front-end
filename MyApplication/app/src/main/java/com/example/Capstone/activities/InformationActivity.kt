package com.example.Capstone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.example.Capstone.R
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.toolbar_with_trashbin.*

class InformationActivity : AppCompatActivity() {

    private val url = "https://wikidocs.net/16040"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

//        val myWebView: WebView = findViewById(R.id.web_url)

        //get web view settings instance
        val settings = web_url.settings

        //enable java script in web view
        settings.javaScriptEnabled = true

        //enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.setAppCachePath(cacheDir.path)

        //enable zoom
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true
        settings.textZoom = 125

        //enable disable images
        settings.blockNetworkImage = false
        settings.loadsImagesAutomatically = true

        web_url.webViewClient = object:WebViewClient(){

        }

//        class WebViewClientClass : WebViewClient() {
//            //페이지 이동
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                Log.d("check URL", url)
//                view.loadUrl(url)
//                return true
//            }
//        }
//
//
//        myWebView.webViewClient = WebViewClientClass()
//        myWebView.loadUrl(url)

        btn_back.setOnClickListener {
            finish()
        }

    }
}
