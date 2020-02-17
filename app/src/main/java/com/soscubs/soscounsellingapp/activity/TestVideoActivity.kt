package com.soscubs.soscounsellingapp.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.soscubs.soscounsellingapp.R
import kotlinx.android.synthetic.main.activity_test_video.*
import java.io.IOException
import java.lang.Exception

class TestVideoActivity : AppCompatActivity(), SurfaceHolder.Callback,
    MediaPlayer.OnPreparedListener, MyMediaController.MediaPlayerControl {

    private var player: MediaPlayer? = null
    private var controller: MyMediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_video)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
        var videoUrl = intent.getStringExtra("videoUrl")

        val videoHolder = videoSurface.holder
        videoHolder.addCallback(this)

        player = MediaPlayer()
        controller = MyMediaController(this)
//        val videoController = MediaController(this)


        try {
            player?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player?.setDataSource(this, Uri.parse(videoUrl))
            player?.setOnPreparedListener(this)
            player?.setOnCompletionListener {
                Toast.makeText(this, "Thankyou for Watching Video", Toast.LENGTH_SHORT).show();
                player?.start()
            }

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    override fun onBackPressed() {
//        controller!!.updatePausePlay()
        pause()
        super.onBackPressed()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        controller?.show()
        return false
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        player?.setDisplay(holder);
        player?.prepareAsync();
    }

    override fun onPrepared(mp: MediaPlayer?) {
        controller?.setMediaPlayer(this);
        controller?.setAnchorView(findViewById<FrameLayout>(R.id.videoSurfaceContainer));
        player?.start();
    }

    override fun start() {
        if (player != null) {
            return player?.start()!!;
        }
    }

    override fun pause() {
        if (player != null) {
            return player?.pause()!!;
        }
    }

    override fun getDuration(): Int {
        if (player != null) {
            return player?.duration!!;
        } else {
            return 0
        }
    }

    override fun getCurrentPosition(): Int {
        if (player != null) {
            return player?.getCurrentPosition()!!;
        } else {
            return 0
        }
    }

    override fun seekTo(pos: Int) {
        if (player != null) {
            return player?.seekTo(pos)!!;
        }
    }

    override fun isPlaying(): Boolean {
        if (player != null) {
            return player?.isPlaying()!!;
        } else {
            return false
        }
    }

    override fun getBufferPercentage(): Int {
        return 0;
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun toggleFullScreen() {
        if (isFullScreen()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        Toast.makeText(applicationContext, "Toggle Full", Toast.LENGTH_SHORT).show()
        //controller?.updateFullScreen()
    }

    override fun isFullScreen(): Boolean {
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            return true
        } else {
            return false
        }


    }





//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        if(newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(applicationContext,"Portrait ",Toast.LENGTH_SHORT).show()
//        }
//        if(newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE){
//            Toast.makeText(applicationContext,"Landscape ",Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(applicationContext, "Portrait ", Toast.LENGTH_SHORT).show()
        }
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(applicationContext, "Landscape ", Toast.LENGTH_SHORT).show()
        }
    }

}
