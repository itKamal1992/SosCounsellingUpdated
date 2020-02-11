package com.example.soscounsellingapp.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.example.soscounsellingapp.Generic.GenericPublicVariable
import com.example.soscounsellingapp.R
import dmax.dialog.SpotsDialog


class FullVideoViewActivity:AppCompatActivity() {
    private var videoView: VideoView?=null
    private var mediaController: MediaController?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_full_video_view)
        videoView = findViewById(R.id.videoView)
        val fullScreen = getIntent().getStringExtra("fullScreenInd")
        val videoUrl = getIntent().getStringExtra("videoUrl")
        if ("y" == fullScreen)
        {
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
//              supportActionBar!!.hide()
        }
        val videoUri = Uri.parse(videoUrl)
        videoView!!.setVideoURI(videoUri)
        mediaController = FullScreenMediaController(this,videoUrl,"ActivityScreen")
        mediaController!!.setAnchorView(videoView!!)
        videoView!!.setMediaController(mediaController)
        videoView!!.start()

//        GenericPublicVariable.CustDialog = Dialog(this)
//        GenericPublicVariable.CustDialog.setContentView(R.layout.video_loading_layout)
//        var progressBar: ProgressBar =
//            GenericPublicVariable.CustDialog.findViewById(R.id.progressBar) as ProgressBar
////        progressBar.
//        var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.txt_message) as TextView
//        tvMsg.text = "Please while while video is loading"
//        GenericPublicVariable.CustDialog.setCancelable(false)
////        btnOk.setOnClickListener {
////            GenericPublicVariable.CustDialog.dismiss()
////
////        }
////        ivPosClose1.setOnClickListener {
////            GenericPublicVariable.CustDialog.dismiss()
////
////        }
//
//        GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        GenericPublicVariable.CustDialog.show()
        val dialog: android.app.AlertDialog =
            SpotsDialog.Builder().setContext(this).build()
        dialog.setMessage("Please while while video is loading ")
        dialog.setCancelable(false)
        dialog.show()




        videoView!!.setOnPreparedListener {
//            Toast.makeText(this, "on prepared", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            videoView!!.visibility = View.VISIBLE
        }
    }


    override fun onBackPressed() {
        println("clicked >>>  ")
        super.onBackPressed()
    }
}