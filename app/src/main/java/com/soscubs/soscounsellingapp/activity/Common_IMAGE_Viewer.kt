package com.soscubs.soscounsellingapp.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.model.FileLoader

import com.soscubs.soscounsellingapp.Generic.GenericUserFunction
import com.soscubs.soscounsellingapp.R

import kotlinx.android.synthetic.main.app_bar_dashboard_main.*
import kotlinx.android.synthetic.main.common_pdf_viewer_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.math.log

class Common_IMAGE_Viewer : AppCompatActivity() {
    private var actionTitle: String? = ""
    private var type: String? = ""
    //    var pdf: PDFView? = null
    var probar:ProgressBar? = null
    var txt_titile:TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_image_viewer_layout)
        setSupportActionBar(toolbar)

        probar = findViewById<ProgressBar>(R.id.progressBar7)
        txt_titile = findViewById<TextView>(R.id.txt_titile)
        probar!!.visibility = View.VISIBLE




try{

    var url:String=intent.getStringExtra("url")
    try {
        actionTitle = intent.getStringExtra("actionTitle")
        txt_titile!!.setText(actionTitle)

        type = intent.getStringExtra("type")
    }catch (ex:java.lang.Exception)
    {
        txt_titile!!.setText("")
    }

//    downloadpdf(url)
    showPdf(url,type!!)
    }catch (ex: Exception) {

        ex.printStackTrace()
        GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
    }
    }


    fun showPdf(url:String,type:String) {
        webView.webViewClient = WebViewClient()
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls=true


        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                println("progress >>  $progress")

                //Make the bar disappear after URL is loaded, and changes string to Loading...
                title = "Loading..."
                setProgress(progress * 100) //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if (progress == 100){
//                    setTitle(R.string.app_name)
                    probar!!.visibility=View.GONE
                }
            }
        })
//        webView.setWebViewClient(HelloWebViewClient())
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://www.google.com");


        val url = url
        if (type=="PDF") {
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        }else{
        webView.loadUrl(url)
        }
    }


}
