package com.example.soscounsellingapp.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.soscounsellingapp.R
import kotlinx.android.synthetic.main.activity_test_video.*

class TestVideoActivity : AppCompatActivity() {
    var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_video)

        var videoSurface = findViewById<View>(R.id.videoSurface) as SurfaceView
//        video_container
        loading

        val videoHolder: SurfaceHolder = videoSurface.holder
//        videoHolder.addCallback(this)
        mMediaPlayer= MediaPlayer()
//        mMediaPlayer!!.setOnVideoSizeChangedListener (this)

    }
}
