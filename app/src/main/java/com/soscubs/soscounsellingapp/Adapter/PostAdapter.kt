package com.soscubs.soscounsellingapp.Adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.DisplayMetrics
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soscubs.soscounsellingapp.DataClass.CommentParameter
import com.soscubs.soscounsellingapp.DataClass.PostParameter
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.mServices
import com.soscubs.soscounsellingapp.Generic.GenericUserFunction
import com.soscubs.soscounsellingapp.Generic.InternetConnection
import com.soscubs.soscounsellingapp.R
import com.soscubs.soscounsellingapp.activity.*
import com.soscubs.soscounsellingapp.common.Common
import com.soscubs.soscounsellingapp.model.APIResponse
import com.google.android.material.snackbar.Snackbar
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.layout_post_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PostAdapter(
    postParameter: ArrayList<PostParameter>,
    context: Context,
    PID: String,
    FROM_DATE: String,
    TO_DATE: String,
    S_ID: String,
    postfor: String,
    parentName: String,
    activity:Activity
//    ,
//    videoSurfaceContainer: FrameLayout
) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>(), View.OnClickListener {
    var Pid: String = ""
    var parentName: String = ""
    var cal = Calendar.getInstance()
    val myFormat = "dd-MM-yyyy" // mention the format you need
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    private var ctx: Context? = null
    private var postlist: MutableList<PostParameter> = postParameter
    var PID:String = ""
    var FROM_DATE:String = ""
    var TO_DATE:String = ""
    var S_ID:String = ""
    var postfor:String = ""
    var activity:Activity?=null

//    private var player: MediaPlayer?=null
//    private var controller: MyMediaController?=null
//
//    private var videoSurfaceContainer: FrameLayout?=null



    init {

        this.postlist = postlist

        this.ctx = context

        this.PID = PID
        this.FROM_DATE = FROM_DATE
        this.TO_DATE = TO_DATE
        this.S_ID = S_ID
        this.postfor = postfor

        this.parentName=parentName
        this.activity=activity




//        this.videoSurfaceContainer=videoSurfaceContainer

    }

//    override fun onViewDetachedFromWindow(holder: ViewHolder) {
//        println("view detached >>> ")
//        super.onViewDetachedFromWindow(holder)
//    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_item, parent, false)
//        activity!!.onBackPressed()
        return ViewHolder(view, ctx!!, postlist)
    }

    override fun getItemCount(): Int {
        return postlist.size
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val parameter: PostParameter = postlist[position]

//        controller = MyMediaController(holder.itemView.context )
//        holder.videoUrl="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"


//        holder.player?.setOnPreparedListener(holder.player)



        holder.txt_post_date?.text = parameter.POST_DATE_GEN
        holder.txt_schoolName?.text = parameter.SCHOOL_NAME
        holder.txt_from_user?.text = "Post by: Admin"
        holder.txt_post_title?.text = parameter.POST_TITLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txt_post_title.setText(
                Html.fromHtml(
                    "<u>${parameter.POST_TITLE}</u>",
                    Html.FROM_HTML_MODE_COMPACT
                )
            );
        } else {
            holder.txt_post_title.setText(Html.fromHtml("<html><h3><u>${parameter.POST_TITLE}</u></h3></html>"));
        }




        holder.txt_post_text?.text = parameter.POST_DESCRIPTION
        if (parameter.ATTACH_TYPE.contains("No Attachment", ignoreCase = true)) {
            holder.post_Image.visibility = View.GONE
//            holder.post_video.visibility = View.GONE
            holder.post_pdf_framelayout.visibility = View.GONE
            holder.post_video_framelayout.visibility = View.GONE

        } else {
            if (parameter.ATTACH_TYPE.contains("IMAGE", ignoreCase = true)) {
                Glide.with(ctx).load(parameter.POST_URL).into(holder.post_Image)
                holder.post_Image.visibility = View.VISIBLE
                holder.post_pdf_framelayout.visibility = View.GONE
//                holder.post_video.visibility = View.GONE
                holder.post_video_framelayout.visibility = View.GONE

            }
            holder.post_Image.setOnClickListener {
                var intent: Intent = Intent(ctx, Common_IMAGE_Viewer::class.java)
                intent.putExtra("url", parameter.POST_URL)
                intent.putExtra("actionTitle", "${parameter.POST_TITLE}")
                intent.putExtra("type", "Image")
                ctx!!.startActivity(intent)
            }

            if (parameter.ATTACH_TYPE.equals("pdf", ignoreCase = true)) {
                showPdf(holder.post_pdf, holder.post_pdf_progress, parameter.POST_URL, ctx!!)
                holder.post_pdf_framelayout.visibility = View.VISIBLE
                holder.post_Image.visibility = View.GONE
//                holder.post_video.visibility = View.GONE
                holder.post_video_framelayout.visibility = View.GONE
            }
            holder.post_pdf_layout.setOnClickListener {
                var intent: Intent = Intent(ctx, Common_PDF_Viewer::class.java)
                intent.putExtra("url", parameter.POST_URL)
                intent.putExtra("actionTitle", "${parameter.POST_TITLE}")
                intent.putExtra("type", "PDF")
                ctx!!.startActivity(intent)
            }

            if (parameter.ATTACH_TYPE.equals("mp4", ignoreCase = true)) {
                holder.post_video_framelayout.visibility = View.VISIBLE
                holder.post_video_progress.visibility=View.VISIBLE
//                holder.post_video.visibility = View.VISIBLE
                holder.post_Image.visibility = View.GONE
                holder.post_pdf_framelayout.visibility = View.GONE

//                var mediacontroller = MediaController(ctx);
                var mediacontroller=FullScreenMediaController(ctx!!,parameter.POST_URL,"AdaptorScreen")
                mediacontroller.setAnchorView(holder.post_video);

                holder.post_video.setMediaController(mediacontroller);
                holder.post_video.setVideoURI(Uri.parse(parameter.POST_URL));
                holder.post_video.requestFocus();

//                holder.post_video.setZOrderOnTop(true);
//                val dialog: android.app.AlertDialog =
//                    SpotsDialog.Builder().setContext(ctx).build()
//                dialog.setMessage("Please while while video is loading ")
//                dialog.setCancelable(false)
//                dialog.show()




                 holder.post_video!!.setOnPreparedListener {
//                    Toast.makeText(this, "on prepared", Toast.LENGTH_SHORT).show()
//                    dialog.dismiss()
                    holder.post_video_progress.visibility=View.GONE
//                    holder.post_video.seekTo( 1000 );
                    mediacontroller.hide()
//                    holder.post_video!!.visibility = View.VISIBLE
                }


//                val thumbnail = ThumbnailUtils.createVideoThumbnail(
//                    parameter.POST_URL,
//                    MediaStore.Images.Thumbnails.MINI_KIND
//                )


//                val bitmapDrawable = BitmapDrawable(thumbnail)
//                holder.post_video.setBackgroundResource(R.drawable.ic_inbox)
                var displaymetrics = DisplayMetrics()
//            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//            ctx!!.resources.displaymetrics
//            var h = displaymetrics.heightPixels;
//            var w = displaymetrics.widthPixels;
////
//            holder.post_video.getLayoutParams().width = w
//            holder.post_video.getLayoutParams().height = h

                holder.post_video.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                    Toast.makeText(ctx, "Thank you for Watching Video", Toast.LENGTH_SHORT).show();
//                if (index++ == arrayList.size) {
//                    index = 0;
//                    it.release();
//                    Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();
//                } else {
//                    holder.post_video.setVideoURI(Uri.parse(arrayList.get(index)));
//                    holder.post_video.start();
//                }
                })

//                holder.post_video.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
//                    // do something when an error is occur during the video playback
//                    false
//                })

//                holder.post_video.setOnPreparedListener{
//                    holder.post_video.setZOrderOnTop(false);
//                    holder.post_video.setBackgroundColor(Color.TRANSPARENT);
//                }









//        val videoController = MediaController(this)
//                holder.player?.setDataSource(ctx, Uri.parse(parameter.POST_URL))
//
//                holder.videoUrl=parameter.POST_URL
//                holder.videoSurfaceContainer.setOnClickListener {
//                    holder.controller?.show()
//                }
            }
        }



        holder.img_post_like.setImageResource(R.drawable.ic_like)
        holder.txt_post_like?.text = "Like"
        holder.txt_post_like_count?.text = parameter.LIKES.toString()

        holder.txt_post_comment_count?.text = parameter.comment_count

        holder.txt_titleComments?.text = "Comment's"
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.txt_titleComments.setText(
//                Html.fromHtml(
//                    "<html><h3><u>Comments</u></h3></html>",
//                    Html.FROM_HTML_MODE_COMPACT
//                )
//            );
//        } else {
//            holder.txt_titleComments.setText(Html.fromHtml("<html><h3><u>Comments</u></h3></html>"));
//        }


        holder.img_post_share.setImageResource(R.drawable.ic_share)
        holder.txt_post_share?.text = "Share"

        holder.ln_layout_like.setOnClickListener {
            //            Toast.makeText(ctx,"like hit $position",Toast.LENGTH_SHORT).show()
            getLikeResult(
                "" + parameter.PID,
                "0",
                "" + S_ID,
                "" + parameter.ID,
                "" + sdf.format(cal.time).toString(),
                "" + parameter.ParentName,
                position
            )
        }

//        holder.img_post_comment.setOnClickListener {
//            Toast.makeText(ctx, "comment hit $position", Toast.LENGTH_SHORT).show()
//        }

        holder.ln_layout_share.setOnClickListener {
//            Toast.makeText(ctx, "share hit $position", Toast.LENGTH_SHORT).show()

//            var sendWhat=""
            var sendData = ""
            if (parameter.ATTACH_TYPE.contains("No Attachment", ignoreCase = true)) {
//                sendWhat="Text"
                sendData = parameter.POST_TITLE + "\n" + parameter.POST_DESCRIPTION
            } else {
//                sendWhat="Url"
                sendData = parameter.POST_TITLE + "\n" + parameter.POST_URL
            }
//            var sendData=parameter.POST_URL


            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, sendData)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            ctx!!.startActivity(shareIntent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txt_post_comments.setText(
                Html.fromHtml(
                    parameter.COMMENTS[0].COMMENT_DESC,
                    Html.FROM_HTML_MODE_COMPACT
                )
            );
        } else {
            holder.txt_post_comments.setText(Html.fromHtml(parameter.COMMENTS[0].COMMENT_DESC));
        }
//        var data=holder.edit_add_comments.text
//        var byteStream:ByteArray=holder.edit_add_comments.text
//        var charset = Charsets.UTF_8
//        var byteArray = holder.edit_add_comments.text.toString().toByteArray(charset)
//        byteArray.toString(charset)

//        holder.txt_post_comments=Html()
        holder.edit_add_comments.text
        holder.btn_send_comments.setOnClickListener {
            if (holder.edit_add_comments.text.toString().length > 0) {
                giveComment(
                    "" + parameter.PID,
                    "" + S_ID,
                    "0",
                    "" + parameter.ID,
                    "" + holder.edit_add_comments.text.toString(),
                    "" + sdf.format(cal.time).toString(),
                    "" + parameter.ParentName,
                    holder,
                    position
                )
            } else {
                val snackbar = Snackbar
                    .make(
                        holder.post_pdf_layout,
                        "Please provide comment to enter",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.show()
            }
        }

        holder.txt_com_against.setOnClickListener {
            CommentedBy(
                parameter.commentAgainst,
                holder.txt_com_against,
                holder
            )
        }
//        p0.camera_image.setImageResource(parameter.)
//        holder.bottom_navigation_menu.setOnNavigationItemSelectedListener {
//
//
//            when (it.itemId) {
//                R.id.img_post_like -> {
//                }
//                R.id.img_post_comment -> {
//                }
//                R.id.img_post_share -> {
//                }
//            }
//            return true
//        }
//        var badge = holder.bottom_navigation_view.getOrCreateBadge();
//        badge.setVisible(true);
    }


    override fun onClick(v: View?) {
    }

//,SurfaceHolder.Callback,
//        MyMediaController.MediaPlayerControl,
//        MediaPlayer.OnPreparedListener
    class ViewHolder(itemView: View, ctx: Context, list: List<PostParameter>) :
        RecyclerView.ViewHolder(itemView){

        val txt_post_date = itemView.findViewById<TextView>(R.id.txt_post_date)
        val txt_schoolName = itemView.findViewById<TextView>(R.id.txt_schoolName)
        val txt_from_user = itemView.findViewById<TextView>(R.id.txt_from_user)
        val txt_post_title = itemView.findViewById<TextView>(R.id.txt_post_title)
        val txt_post_text = itemView.findViewById<TextView>(R.id.txt_post_text)

        //Attachments
        val post_Image = itemView.findViewById<ImageView>(R.id.post_Image)
        val post_pdf = itemView.findViewById<WebView>(R.id.post_pdf)
        val post_video = itemView.findViewById<VideoView>(R.id.post_video)

        val post_video_framelayout = itemView.findViewById<FrameLayout>(R.id.post_video_framelayout)
        val post_video_progress = itemView.findViewById<ProgressBar>(R.id.post_video_progress)

        val post_pdf_framelayout = itemView.findViewById<FrameLayout>(R.id.post_pdf_framelayout)
        val post_pdf_layout = itemView.findViewById<LinearLayout>(R.id.post_pdf_layout)
        val post_pdf_progress = itemView.findViewById<ProgressBar>(R.id.post_pdf_progress)



        // Bottom part
        val img_post_like = itemView.findViewById<ImageView>(R.id.img_post_like)
        val txt_post_like = itemView.findViewById<TextView>(R.id.txt_post_like)
        val txt_post_like_count = itemView.findViewById<TextView>(R.id.txt_post_like_count)
        val ln_layout_like = itemView.findViewById<LinearLayout>(R.id.ln_layout_like)
        val ln_layout_share = itemView.findViewById<LinearLayout>(R.id.ln_layout_share)


        val txt_titleComments = itemView.findViewById<TextView>(R.id.txt_titleComments)
        val txt_post_comment_count= itemView.findViewById<TextView>(R.id.txt_post_comment_count)

        val img_post_share = itemView.findViewById<ImageView>(R.id.img_post_share)
        val txt_post_share = itemView.findViewById<TextView>(R.id.txt_post_share)


        val txt_post_comments = itemView.findViewById<TextView>(R.id.txt_post_comments)
        val edit_add_comments = itemView.findViewById<EditText>(R.id.edit_add_comments)
        val btn_send_comments = itemView.findViewById<Button>(R.id.btn_send_comments)

        val txt_com_against = itemView.findViewById<TextView>(R.id.txt_com_against)

        var com_against:String="Reply to post"

//        val videoSurface = itemView.videoSurface.holder
//        val videoSurfaceContainer =itemView.findViewById<FrameLayout>(R.id.videoSurfaceContainer)

//        val videoHolder = holder.videoSurface

//        videoHolder.addCallback(c)
//
//        videoSurfaceContainer
//        videoSurface
//        val bottom_navigation_view =
//            itemView.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
//        val bottom_navigation_menu =
//            itemView.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

//        init {
//            img_post_like.setOnClickListener {
//                Toast.makeText(ctx,"like hit",Toast.LENGTH_SHORT).show()
//            }
//
//            img_post_comment.setOnClickListener {
//                Toast.makeText(ctx,"comment hit",Toast.LENGTH_SHORT).show()
//            }
//
//            img_post_share.setOnClickListener {
//                Toast.makeText(ctx,"share hit",Toast.LENGTH_SHORT).show()
//            }
//
//        }
//

//        var ctx:Context?=null
//        var videoUrl=""
//        val player = MediaPlayer()
////        holder.player?OnMediaTimeDiscontinuityListener
//        val controller = MyMediaController(itemView.videoSurfaceContainer.context )
////        videoSurfaceContainer=holder.videoSurfaceContainer
//        init  {
//            this.ctx=ctx
//
//            videoSurface.addCallback(this)
////            this.videoSurfaceContainer=holder.videoSurfaceContainer
//
//            try {
//                player?.setAudioStreamType(AudioManager.STREAM_MUSIC)
////                player?.setDataSource(ctx, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"))
//
//                player?.setOnPreparedListener(this)
//                player?.setOnCompletionListener{
//                    Toast.makeText(ctx, "Thankyou for Watching Video", Toast.LENGTH_SHORT).show();
//                    player?.start()
//                }
//            } catch (e: IllegalArgumentException) {
//                e.printStackTrace()
//            } catch (e: SecurityException) {
//                e.printStackTrace()
//            } catch (e: IllegalStateException) {
//                e.printStackTrace()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        }


//        player?.setDataSource(ctx, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"))
//        player?.setOnPreparedListener(this)



//        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
//
//        }
//
//        override fun surfaceDestroyed(holder: SurfaceHolder?) {
//
//        }
//
//        override fun surfaceCreated(holder: SurfaceHolder?) {
//            player!!.setDisplay(holder);
//            player!!.prepareAsync();
//        }
//
//        override fun onPrepared(mp: MediaPlayer?) {
//            controller?.setMediaPlayer(this);
//            controller?.setAnchorView(videoSurfaceContainer);
////        player?.start();
//        }
//
//        override fun start() {
//            if (player!=null) {
//                return player?.start()!!;
//            }
//        }
//
//        override fun pause() {
//            if (player!=null) {
//                return player?.pause()!!;
//            }
//        }
//
//        override fun getDuration(): Int {
//            if (player!=null) {
//                return player?.duration!!;
//            }else{
//                return 0
//            }
//        }
//
//        override fun getCurrentPosition(): Int {
//            if (player!=null) {
//                return player?.getCurrentPosition()!!;
//            }else{
//                return 0
//            }
//        }
//
//        override fun seekTo(pos: Int) {
//            if (player!=null) {
//                return player?.seekTo(pos)!!;
//            }
//        }
//
//        override fun isPlaying(): Boolean {
//            if (player!=null) {
//                return player?.isPlaying()!!;
//            }else{
//                return false
//            }
//        }
//
//        override fun getBufferPercentage(): Int {
//            return 0;
//        }
//
//
//
//        override fun canPause(): Boolean {
//            return true
//        }
//
//        override fun canSeekBackward(): Boolean {
//            return true
//        }
//
//        override fun canSeekForward(): Boolean {
//            return true
//        }
//
//
//        override fun toggleFullScreen() {
////            var intent = Intent(ctx, TestVideoActivity::class.java)
////            ctx!!.startActivity(intent)
//
//            player.pause()
//            var intent = Intent(ctx, TestVideoActivity::class.java)
//            intent.putExtra("videoUrl", videoUrl)
//
//            ctx!!.startActivity(intent)
////        Toast.makeText(ctx,"Toggle Full",Toast.LENGTH_SHORT).show()
//        }
//
//        override fun isFullScreen(): Boolean {
////            if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//            if (0 == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//                return true
//            }else {
//                return false
//            }
//        }


    }


    fun showPdf(post_pdf: WebView, post_pdf_progress: ProgressBar, url: String, context: Context) {
        post_pdf_progress.visibility = View.VISIBLE
        post_pdf.webViewClient = WebViewClient()
        post_pdf.settings.setSupportZoom(true)
        post_pdf.settings.javaScriptEnabled = true


        post_pdf.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                println("progress >>  $progress")

                //Make the bar disappear after URL is loaded, and changes string to Loading...
//                title = "Loading..."

//               setProgress(progress * 100) //Make the bar disappear after URL is loaded
//
//                // Return the app name after finish loading
                if (progress == 100) {
                    post_pdf_progress.visibility = View.GONE
                }
            }


        })
//        webView.setWebViewClient(HelloWebViewClient())
        post_pdf.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://www.google.com");


        val url = url
        post_pdf.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
    }

    fun getLikeResult(
        PAR_UID: String,
        COU_UID: String,
        SCH_UID: String,
        ID: String,
        LDATE: String,
        LNAME: String,
        itemPosition:Int
    ) {
        if (InternetConnection.checkConnection(ctx!!)) {


//            val dialog: android.app.AlertDialog =
//                SpotsDialog.Builder().setContext(ctx!!).build()
//            dialog.setMessage("Like ...")
//            dialog.setCancelable(false)
//            dialog.show()


            try {

                mServices = Common.getAPI()

                val data: MutableMap<String, String> = HashMap()
                data["PAR_UID"] = PAR_UID
                data["COU_UID"] = COU_UID
                data["SCH_UID"] = SCH_UID
                data["ID"] = ID
                data["LDATE"] = LDATE
                data["LNAME"] = LNAME

                mServices.getLikePosts(
                    data
                ).enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//                        dialog.dismiss()
                        GenericUserFunction.showNegativePopUp(
                            ctx!!,
                            t.message.toString()
                        )
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
//                        dialog.dismiss()
                        if (response.code() == 200) {
                            var result = response.body()
                            getApiResult(postfor,itemPosition )
                            println(result)
                        } else {
                            GenericUserFunction.showApiError(
                                ctx!!,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )

                        }
                        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
            } catch (ex: Exception) {
//                dialog.dismiss()
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    ctx!!,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }

        } else {
            GenericUserFunction.showInternetNegativePopUp(
                ctx!!, ctx!!.getString(R.string.failureNoInternetErr)
            )
        }

    }

    fun giveComment(
        PAR_UID: String,
        SCH_UID: String,
        COU_UID: String,
        ID: String,
        COMMENT_DESC: String,
        CDATE: String,
        SENDER_NAME: String,
        holder: ViewHolder,
        itemPosition:Int
    ) {
        if (InternetConnection.checkConnection(ctx!!)) {


//            val dialog: android.app.AlertDialog =
//                SpotsDialog.Builder().setContext(ctx!!).build()
//            dialog.setMessage("Comment ...")
//            dialog.setCancelable(false)
//            dialog.show()


            try {

                mServices = Common.getAPI()


                mServices.giveComments(
                    "" + PAR_UID,
                    "" + SCH_UID,
                    "" + COU_UID,
                    "" + ID,
                    "" + COMMENT_DESC,
                    "" + CDATE,
                    "" + SENDER_NAME,
                    holder.com_against.toString()
                ).enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//                        dialog.dismiss()
                        GenericUserFunction.showNegativePopUp(
                            ctx!!,
                            t.message.toString()
                        )
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
//                        dialog.dismiss()
                        if (response.code() == 200) {
                            holder.edit_add_comments.text.clear()
                            var result = response.body()
                            getApiResult(postfor,itemPosition)

                            println(result)
                        } else {
                            GenericUserFunction.showApiError(
                                ctx!!,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )

                        }
                        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
            } catch (ex: Exception) {
//                dialog.dismiss()
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    ctx!!,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }

        } else {
            GenericUserFunction.showInternetNegativePopUp(
                ctx!!, ctx!!.getString(R.string.failureNoInternetErr)
            )
        }

    }

    fun CommentedBy(
        item: ArrayList<String>,
        txtComAgainst: TextView,
        holder: ViewHolder
    )
    {
        GenericPublicVariable.CustDialog = Dialog(ctx)
        GenericPublicVariable.CustDialog.setContentView(R.layout.commentsby_popup)
        var ivNegClose1: ImageView =
            GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//        var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
        var list_commentsBy: ListView =
            GenericPublicVariable.CustDialog.findViewById(R.id.list_commentsBy) as ListView

//        var item=ArrayList<String>()
//        item.add("parent 1")
//        item.add("parent 2")
//        item.add("parent 3")
//        item.add("parent 4")
//        item.add("parent 5")
        var itemsAdapter = ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, item);
        list_commentsBy.adapter = itemsAdapter
        list_commentsBy.setOnItemClickListener { parent, view, position, id ->


            var selectedparent = list_commentsBy.adapter.getItem(position)
            GenericPublicVariable.CustDialog.dismiss()
            if (selectedparent != null && selectedparent != "Reply to post") {
                println("Selected item >> " + list_commentsBy.adapter.getItem(position))
                txtComAgainst.setTextColor(Color.GREEN)
                holder.com_against=list_commentsBy.adapter.getItem(position).toString()

            } else {
                txtComAgainst.setTextColor(Color.BLACK)
                holder.com_against="Reply to post"
            }
        }



        GenericPublicVariable.CustDialog.setCancelable(false)
//        btnOk.setOnClickListener {
//            GenericPublicVariable.CustDialog.dismiss()
//
//        }
        ivNegClose1.setOnClickListener {
            GenericPublicVariable.CustDialog.dismiss()
        }
        GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        GenericPublicVariable.CustDialog.show()
    }

    fun getApiResult(postfor:String,itemPosition: Int){
        if (InternetConnection.checkConnection(ctx!!)) {
//            if (compareDate(select_from_date!!.text.toString(), select_to_date!!.text.toString())) {


                val dialog: android.app.AlertDialog =
                    SpotsDialog.Builder().setContext(ctx!!).build()
                dialog.setMessage("Please Wait!!! \nwhile we are Getting Posts ")
                dialog.setCancelable(false)
                dialog.show()


                try {

                    mServices = Common.getAPI()

                    val data: MutableMap<String, String> = java.util.HashMap()
                    data["PID"] = PID
                    data["FROM_DATE"] = FROM_DATE
                    data["TO_DATE"] = TO_DATE
                    data["S_ID"] = S_ID

                    mServices.getPostsData(
                        data
                    ).enqueue(object :
                        Callback<ArrayList<PostParameter>> {
                        override fun onFailure(call: Call<ArrayList<PostParameter>>,t: Throwable) {

                                dialog.dismiss()
                                GenericUserFunction.showNegativePopUp(
                                    ctx!!,
                                    t.message.toString()
                                )


                        }

                        override fun onResponse(call: Call<ArrayList<PostParameter>>,response: Response<ArrayList<PostParameter>>) {
                            dialog.dismiss()
                            if (response.code() == 200) {
                                var result = response.body()
                                if (result!!.size>0){
//                                    val postParameterArray = ArrayList<PostParameter>()
                                    postlist.clear()
                                    for (i in 0..result.size - 1 ){
                                        if (postfor=="Your")
                                        {
                                            val commentArray = ArrayList<CommentParameter>()

                                            var parentsNam=mutableSetOf<String>()
                                            parentsNam.add("Reply to post")
                                            var commentAgainst=ArrayList<String>()

                                            var comments=result[i].COMMENTS
                                            val com = StringBuilder()
                                            if (comments.size>0){
                                                for (j in 0..comments.size - 1 ){
//                                                comments[i].SENDER_NAME + comments[i].COMMENT_DESC
                                                    if (j==0) {
                                                        if (comments[j].CAGN!="Reply to post"){
                                                            com.insert(0,"<html><h4>${comments[j].SENDER_NAME} replied to <font color=\"#bebebe\" face = \"Comic sans MS\">${comments[j].CAGN}</font></h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>")
                                                        }else {
                                                            com.insert(0,"<html><h4>${comments[j].SENDER_NAME}</h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>")
                                                        }
                                                    }else
                                                    {
                                                        if (comments[j].CAGN!="Reply to post"){
                                                            com.insert(0,"<html><hr><h4>${comments[j].SENDER_NAME} replied to <font color=\"#bebebe\" face = \"Comic sans MS\">${comments[j].CAGN}</font></h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>")
                                                        }
                                                        else {
                                                            com.insert(0,"<html><hr><h4>${comments[j].SENDER_NAME}</h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>")
                                                        }
                                                    }
                                                    if(comments[j].SENDER_NAME==parentName){

                                                    }  else {
                                                        parentsNam.add(comments[j].SENDER_NAME)
                                                    }
                                                }
                                            }else
                                            {
                                                com.append("No Comments")
                                            }
                                            commentArray.add(0, CommentParameter(
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                com.toString(),
                                                "",
                                                "",
                                                ""
                                            )
                                            )
                                            for(element in parentsNam){
                                                println(element)
                                                commentAgainst.add(element)
                                            }

                                            if (result[i].PID=="SPE"){
                                                postlist.add(
                                                    PostParameter(
                                                        ""+result[i].ID,

                                                        ""+PID,// getting user PID not from Response
                                                        ""+result[i].COUNS_ID,
                                                        ""+result[i].S_ID,
                                                        ""+result[i].POST_TITLE,
                                                        ""+result[i].POST_DESCRIPTION,
                                                        ""+result[i].POST_DATE_GEN,
                                                        ""+result[i].SCHOOL_NAME,
                                                        ""+result[i].POST_URL,
                                                        ""+result[i].ATTACH_TYPE,
                                                        ""+result[i].SENDER_ID,
                                                        ""+result[i].SENDER_NAME,
                                                        ""+result[i].SENDER,
                                                        +result[i].LIKES,
                                                        ""+parentName,
                                                        commentArray,
                                                        ""+result[i].LIKES_STATUS_PAR,
                                                        ""+result[i].LIKES_STATUS_COUS,
                                                        ""+comments.size,
                                                        commentAgainst


                                                    )
                                                )
                                            }

                                        }else
                                            if (postfor=="Common"){
                                                val commentArray = ArrayList<CommentParameter>()

                                                var parentsNam=mutableSetOf<String>()
                                                parentsNam.add("Reply to post")
                                                var commentAgainst=ArrayList<String>()

                                                var comments=result[i].COMMENTS
                                                val com = StringBuilder()
                                                if (comments.size>0){
                                                    for (j in 0..comments.size - 1 ){
//                                                comments[i].SENDER_NAME + comments[i].COMMENT_DESC
                                                        if (j==0) {
                                                            if (comments[j].CAGN!="Reply to post"){
                                                                com.insert(0,"<html><h4>${comments[j].SENDER_NAME} replied to <font color=\"#bebebe\" face = \"Comic sans MS\">${comments[j].CAGN}</font></h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>")
                                                            }else {
                                                                com.insert(0,"<html><h4>${comments[j].SENDER_NAME}</h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>")
                                                            }
                                                        }else
                                                        {
                                                            if (comments[j].CAGN!="Reply to post"){
                                                                com.insert(0,"<html><hr><h4>${comments[j].SENDER_NAME} replied to <font color=\"#bebebe\" face = \"Comic sans MS\">${comments[j].CAGN}</font></h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>")
                                                            }
                                                            else {
                                                                com.insert(0,"<html><hr><h4>${comments[j].SENDER_NAME}</h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>")
                                                            }
                                                        }
                                                        if(comments[j].SENDER_NAME==parentName){

                                                        }  else {
                                                            parentsNam.add(comments[j].SENDER_NAME)
                                                        }
                                                    }
                                                }else
                                                {
                                                    com.append("No Comments")
                                                }
                                                commentArray.add(0, CommentParameter(
                                                    "",
                                                    "",
                                                    "",
                                                    "",
                                                    "",
                                                    com.toString(),
                                                    "",
                                                    "",
                                                    ""
                                                )
                                                )
                                                for(element in parentsNam){
                                                    println(element)
                                                    commentAgainst.add(element)
                                                }
                                                if (result[i].PID!="SPE"){
                                                    postlist.add(
                                                        PostParameter(
                                                            ""+result[i].ID,
                                                            ""+PID,// getting user PID not from Response
                                                            ""+result[i].COUNS_ID,
                                                            ""+result[i].S_ID,
                                                            ""+result[i].POST_TITLE,
                                                            ""+result[i].POST_DESCRIPTION,
                                                            ""+result[i].POST_DATE_GEN,
                                                            ""+result[i].SCHOOL_NAME,
                                                            ""+result[i].POST_URL,
                                                            ""+result[i].ATTACH_TYPE,
                                                            ""+result[i].SENDER_ID,
                                                            ""+result[i].SENDER_NAME,
                                                            ""+result[i].SENDER,
                                                            +result[i].LIKES,
                                                            parentName,
                                                            commentArray,
                                                            ""+result[i].LIKES_STATUS_PAR,
                                                            ""+result[i].LIKES_STATUS_COUS,
                                                            ""+comments.size,
                                                            commentAgainst


                                                        )
                                                    )
                                                }
                                            }



                                    }

                                    postlist

                                    notifyItemChanged(itemPosition)

//                                    val adapter =
//                                        PostAdapter(postParameterArray, this@InboxActivity,PID,select_from_date!!.text.toString(),
//                                            select_to_date!!.text.toString(),S_ID,postfor)
//                                    if (postParameterArray.isEmpty()) {
////                                    if (!isFinishing){ postParameterArray.clear()
//
//                                        GenericUserFunction.showOopsError(
//                                            this@InboxActivity,
//                                            "Posts not delivered for you"
//                                        )
////                                    adapter.notifyDataSetChanged()
////                                    }
//                                    } else {
////                                    var mDividerItemDecoration = DividerItemDecoration(
////                                        recyclerView!!.context,
////                                    RecyclerView.LayoutManager.get
////                                    )
////                                    recyclerView!!.addItemDecoration(mDividerItemDecoration)
//
//                                        recyclerView!!.adapter = adapter
//
//                                    }

                                }
                                println(result)
                            } else {

                            }
                            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                } catch (ex: Exception) {
                    dialog.dismiss()
                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        ctx!!,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
//            }
        }else
        {
            GenericUserFunction.showInternetNegativePopUp(
                ctx!!,
                ctx!!.getString(R.string.failureNoInternetErr)
            )
        }

    }




}
