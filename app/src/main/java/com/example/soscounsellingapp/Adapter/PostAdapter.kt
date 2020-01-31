package com.example.soscounsellingapp.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Html
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.stream.MediaStoreVideoThumbLoader
import com.example.soscounsellingapp.DataClass.PostParameter
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.mServices
import com.example.soscounsellingapp.Generic.GenericUserFunction
import com.example.soscounsellingapp.Generic.InternetConnection
import com.example.soscounsellingapp.R
import com.example.soscounsellingapp.activity.Common_PDF_Viewer
import com.example.soscounsellingapp.common.Common
import com.example.soscounsellingapp.model.APIResponse
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PostAdapter(postParameter: ArrayList<PostParameter>, context: Context) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>(), View.OnClickListener {
    var Pid: String = ""
    var parentName: String = ""
    var cal = Calendar.getInstance()
    val myFormat = "dd-MM-yyyy" // mention the format you need
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    private var ctx: Context? = null
    private var postlist: List<PostParameter> = postParameter


    init {

        this.postlist = postlist
        this.ctx = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_item, parent, false)
        return ViewHolder(view, ctx!!, postlist)
    }

    override fun getItemCount(): Int {
        return postlist.size
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {

        val parameter: PostParameter = postlist[position]
        holder.txt_post_date?.text = parameter.POST_DATE_GEN
        holder.txt_schoolName?.text = parameter.SCHOOL_NAME
        holder.txt_from_user?.text = "Post by: Admin"
        holder.txt_post_title?.text = parameter.POST_TITLE
        holder.txt_post_text?.text = parameter.POST_DESCRIPTION
        if (parameter.ATTACH_TYPE.contains("No Attachment", ignoreCase = true)) {
            holder.post_Image.visibility = View.GONE
            holder.post_video.visibility = View.GONE
            holder.post_pdf_framelayout.visibility = View.GONE

        } else {
            if (parameter.ATTACH_TYPE.contains("IMAGE", ignoreCase = true)) {
                Glide.with(ctx).load(parameter.POST_URL).into(holder.post_Image)
                holder.post_Image.visibility = View.VISIBLE
                holder.post_pdf_framelayout.visibility = View.GONE
                holder.post_video.visibility = View.GONE

            }

            if (parameter.ATTACH_TYPE.equals("pdf", ignoreCase = true)) {
                showPdf(holder.post_pdf, holder.post_pdf_progress, parameter.POST_URL, ctx!!)
                holder.post_pdf_framelayout.visibility = View.VISIBLE
                holder.post_Image.visibility = View.GONE
                holder.post_video.visibility = View.GONE
            }
            holder.post_pdf_layout.setOnClickListener {
                var intent: Intent = Intent(ctx, Common_PDF_Viewer::class.java)
                intent.putExtra("url", parameter.POST_URL)
                intent.putExtra("actionTitle", "${parameter.POST_TITLE} PDF")
                ctx!!.startActivity(intent)
            }

            if (parameter.ATTACH_TYPE.equals("mp4", ignoreCase = true)) {
                holder.post_video.visibility = View.VISIBLE
                holder.post_Image.visibility = View.GONE
                holder.post_pdf_framelayout.visibility = View.GONE

                var mediacontroller = MediaController(ctx);
                mediacontroller.setAnchorView(holder.post_video);


                holder.post_video.setMediaController(mediacontroller);
                holder.post_video.setVideoURI(Uri.parse(parameter.POST_URL));
                holder.post_video.requestFocus();


                val thumbnail = ThumbnailUtils.createVideoThumbnail(
                    parameter.POST_URL,
                    MediaStore.Images.Thumbnails.MINI_KIND
                )


                val bitmapDrawable = BitmapDrawable(thumbnail)
                holder.post_video.setBackgroundResource(R.drawable.ic_inbox)
                var displaymetrics = DisplayMetrics()
//            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//            ctx!!.resources.displaymetrics
//            var h = displaymetrics.heightPixels;
//            var w = displaymetrics.widthPixels;
//
//            holder.post_video.getLayoutParams().width = w;
//            holder.post_video.getLayoutParams().height = h;
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

                holder.post_video.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
                    // do something when an error is occur during the video playback
                    false
                })


            }
        }

        holder.img_post_like.setImageResource(R.drawable.ic_like)
        holder.txt_post_like?.text = "Like"
        holder.txt_post_like_count?.text = parameter.LIKES.toString()

        holder.img_post_comment.setImageResource(R.drawable.ic_comment)
        holder.txt_post_comment?.text = "Comment"
        holder.img_post_share.setImageResource(R.drawable.ic_share)
        holder.txt_post_share?.text = "Share"

        holder.img_post_like.setOnClickListener {
            //            Toast.makeText(ctx,"like hit $position",Toast.LENGTH_SHORT).show()
            getLikeResult(
                "" + parameter.PID,
                "",
                "" + parameter.S_ID,
                "" + parameter.ID,
                "" + sdf.format(cal.time).toString(),
                "" + parameter.ParentName
            )
        }

        holder.img_post_comment.setOnClickListener {
            Toast.makeText(ctx, "comment hit $position", Toast.LENGTH_SHORT).show()
        }

        holder.img_post_share.setOnClickListener {
            Toast.makeText(ctx, "share hit $position", Toast.LENGTH_SHORT).show()

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
            holder.txt_post_comments.setText(Html.fromHtml(parameter.COMMENTS[0].COMMENT_DESC, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.txt_post_comments.setText(Html.fromHtml(parameter.COMMENTS[0].COMMENT_DESC));
        }
//        holder.txt_post_comments=Html()
        holder.edit_add_comments
        holder.btn_send_comments
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


    class ViewHolder(itemView: View, ctx: Context, list: List<PostParameter>) :
        RecyclerView.ViewHolder(itemView) {

        val txt_post_date = itemView.findViewById<TextView>(R.id.txt_post_date)
        val txt_schoolName = itemView.findViewById<TextView>(R.id.txt_schoolName)
        val txt_from_user = itemView.findViewById<TextView>(R.id.txt_from_user)
        val txt_post_title = itemView.findViewById<TextView>(R.id.txt_post_title)
        val txt_post_text = itemView.findViewById<TextView>(R.id.txt_post_text)

        //Attachments
        val post_Image = itemView.findViewById<ImageView>(R.id.post_Image)
        val post_pdf = itemView.findViewById<WebView>(R.id.post_pdf)
        val post_video = itemView.findViewById<VideoView>(R.id.post_video)

        val post_pdf_framelayout = itemView.findViewById<FrameLayout>(R.id.post_pdf_framelayout)
        val post_pdf_layout = itemView.findViewById<LinearLayout>(R.id.post_pdf_layout)
        val post_pdf_progress = itemView.findViewById<ProgressBar>(R.id.post_pdf_progress)


        // Bottom part
        val img_post_like = itemView.findViewById<ImageView>(R.id.img_post_like)
        val txt_post_like = itemView.findViewById<TextView>(R.id.txt_post_like)
        val txt_post_like_count = itemView.findViewById<TextView>(R.id.txt_post_like_count)

        val img_post_comment = itemView.findViewById<ImageView>(R.id.img_post_comment)
        val txt_post_comment = itemView.findViewById<TextView>(R.id.txt_post_comment)

        val img_post_share = itemView.findViewById<ImageView>(R.id.img_post_share)
        val txt_post_share = itemView.findViewById<TextView>(R.id.txt_post_share)


        val txt_post_comments =itemView.findViewById<TextView>(R.id.txt_post_comments)
        val edit_add_comments =itemView.findViewById<EditText>(R.id.edit_add_comments)
        val btn_send_comments =itemView.findViewById<Button>(R.id.btn_send_comments)

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
        LNAME: String
    ) {
        if (InternetConnection.checkConnection(ctx!!)) {


            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(ctx!!).build()
            dialog.setMessage("Like ...")
            dialog.setCancelable(false)
            dialog.show()


            try {

                mServices = Common.getAPI()

                val data: MutableMap<String, String> = HashMap()
                data["PAR_UID"] = PAR_UID
//                    data["COU_UID"] = COU_UID
                data["SCH_UID"] = SCH_UID
                data["ID"] = ID
                data["LDATE"] = LDATE
                data["LNAME"] = LNAME

                mServices.getLikePosts(
                    data
                ).enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        dialog.dismiss()
                        GenericUserFunction.showNegativePopUp(
                            ctx!!,
                            t.message.toString()
                        )
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        dialog.dismiss()
                        if (response.code() == 200) {
                            var result = response.body()
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
                dialog.dismiss()
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
}
