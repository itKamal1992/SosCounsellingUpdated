package com.example.soscounsellingapp.activity

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import com.example.soscounsellingapp.R

class FullScreenMediaController(context:Context,videoUrl:String,screenCall:String):MediaController(context) {
    private var fullScreen:ImageButton?=null
    private var isFullScreen:String?=null
    var videoUrl:String?=null
    var screenCall:String?=null
    init {
        this.screenCall=screenCall
        this.videoUrl=videoUrl
    }
    override fun setAnchorView(view:View) {
        super.setAnchorView(view)
        //image button for full screen to be added to media controller

        if (screenCall.equals("AdaptorScreen")) {
            fullScreen = ImageButton(super.getContext())
            fullScreen!!.setBackgroundResource(R.drawable.screen_background_dark_transparent)

            val params = FrameLayout.LayoutParams(
                50,
                50
            )
            params.gravity = Gravity.RIGHT
            params.rightMargin = 80
            params.topMargin=30
            addView(fullScreen, params)
            //fullscreen indicator from intent
            isFullScreen = (getContext() as Activity).getIntent().getStringExtra("fullScreenInd")
            if ("y" == isFullScreen) {
                fullScreen!!.setImageResource(R.drawable.ic_fullscreen_exit_black_24dp)
            } else {
                fullScreen!!.setImageResource(R.drawable.ic_fullscreen_black_24dp)
            }


        //add listener to image button to handle full screen and exit full screen events
        fullScreen!!.setOnClickListener(object:OnClickListener {
            override fun onClick(v:View) {
                var intent:Intent?=null
                if (screenCall.equals("AdaptorScreen")){
                    intent= Intent(getContext(), FullVideoViewActivity::class.java)
                    intent.putExtra("videoUrl",videoUrl)
                    (getContext() as Activity).startActivity(intent)
                }else
                {
//                    if ("y" == isFullScreen)
//                    {
//                        intent!!.putExtra("fullScreenInd", "")
//                    }
//                    else
//                    {
//                        intent!!.putExtra("fullScreenInd", "y")
//                    }

                }



            }
        })
        }
    }
}