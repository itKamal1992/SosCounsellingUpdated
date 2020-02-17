package com.soscubs.soscounsellingapp.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soscubs.soscounsellingapp.Adapter.PostAdapter
import com.soscubs.soscounsellingapp.ApiCustomRequest.SosPostsRequest
import com.soscubs.soscounsellingapp.DataClass.CommentParameter
import com.soscubs.soscounsellingapp.DataClass.PostParameter
import com.soscubs.soscounsellingapp.Generic.GenericUserFunction
import com.soscubs.soscounsellingapp.Generic.InternetConnection
import com.soscubs.soscounsellingapp.Generic.SharedPreference
import com.soscubs.soscounsellingapp.R
import com.soscubs.soscounsellingapp.common.Common
import com.soscubs.soscounsellingapp.dashboard.DashboardUser
import com.soscubs.soscounsellingapp.remote.IMyAPI
import com.google.android.material.snackbar.Snackbar
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_inbox.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//${comments[i].SENDER_NAME}
//Sender Name/////
class InboxActivity : AppCompatActivity() {
    private var postParameterArray: ArrayList<PostParameter>? = null
    private var recyclerView: RecyclerView? = null
    var PID: String = ""
    var S_ID: String = ""

    var parentName: String = ""

    var cal = Calendar.getInstance()
    private lateinit var mServices: IMyAPI
    var adapter: PostAdapter? = null


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        this.supportActionBar!!.displayOptions=ActionBar.DISPLAY_SHOW_CUSTOM
//        supportActionBar!!.setDisplayShowCustomEnabled(true)
//        supportActionBar!!.setCustomView(R.layout.custom_action_bar_layout)

        setContentView(R.layout.activity_inbox)
//        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

//        val toolbar = findViewById(R.id.toolbar) as Toolbar?
//        setSupportActionBar(toolbar)
//        toolbar?.title = "Androidly"
//        toolbar?.subtitle = "Sub"

//        this.getSupportActionBar()!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        this.getSupportActionBar()!!.setDisplayShowCustomEnabled(true);
//        this.getSupportActionBar()!!.setCustomView(R.layout.custom_action_bar_layout);
//         var view :View=getSupportActionBar()!!.getCustomView();


//        var imageButton= view.findViewById<ImageButton>(R.id.action_bar_back);

        PID = intent.getStringExtra("PID")
        S_ID = intent.getStringExtra("S_ID")
        parentName = intent.getStringExtra("parentName")
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val oldDate = calendar.time
        println("newDate >>> " + sdf.format(oldDate))


//        rootlayout = findViewById<LinearLayout>(R.id.rootlayout);

        select_from_date!!.text = sdf.format(oldDate)
        select_to_date!!.text = sdf.format(cal.time).toString()

        getApiResult("Common")

        select_from_date.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                compareDate(select_from_date!!.text.toString(), select_to_date!!.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        select_to_date.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                compareDate(select_from_date!!.text.toString(), select_to_date!!.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        recyclerView = findViewById<RecyclerView>(R.id.recycle_post_list)
        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        search_linearLayout.setOnClickListener {
            txt_postType.text = "Common Posts"
            getApiResult("Common")

        }
        btn_selfPosts.setOnClickListener {
            txt_postType.text = "Your's  Posts"
            getApiResult("Your")

        }
        btn_commonPosts.setOnClickListener {
            txt_postType.text = "Common Posts"
            getApiResult("Common")

        }


//        val postParameterArray = ArrayList<PostParameter>()
//        postParameterArray.add(
//            PostParameter(
//                "01",
//                "1001",
//                "02",
//                "03",
//                "title 1",
//                "On admission in 5th and above every student will get IPAD with 30% off to study our latest customized course material.Be ready to grab this opportunity and make your child to enjoy learning with technology.",
//                "Date : 22/01/2020",
//                "School : SOS-Atrey Layout",
//                "http://dmimsdu.in/web/cubs_image/login_screen_img.png",
//                "jpg",
//                "-",
//                "-",
//                "",
//                0
//
//            )
//        )
//
//        postParameterArray.add(
//            PostParameter(
//                "01",
//                "1001",
//                "02",
//                "03",
//                "title 1",
//                "On admission in 5th and above every student will get IPAD with 30% off to study our latest customized course material.Be ready to grab this opportunity and make your child to enjoy learning with technology.",
//                "Date : 22/01/2020",
//                "School : SOS-Atrey Layout",
//                "https://smartschoolbustracking.com/ssisapi/uploadedimg/Notice_17_0_17012020102413.pdf",
//                "pdf",
//                "-",
//                "-",
//                "",
//                0
//
//            )
//        )
//
//        postParameterArray.add(
//            PostParameter(
//                "01",
//                "1001",
//                "02",
//                "03",
//                "title 1",
//                "On admission in 5th and above every student will get IPAD with 30% off to study our latest customized course material.Be ready to grab this opportunity and make your child to enjoy learning with technology.",
//                "Date : 22/01/2020",
//                "School : SOS-Atrey Layout",
//                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
//                "mp4",
//                "-",
//                "-",
//                "-",
//                0
//            )
//        )
//
//        postParameterArray.add(
//            PostParameter(
//                "01",
//                "1001",
//                "02",
//                "03",
//                "title 1",
//                "On admission in 5th and above every student will get IPAD with 30% off to study our latest customized course material.Be ready to grab this opportunity and make your child to enjoy learning with technology.",
//                "Date : 22/01/2020",
//                "School : SOS-Atrey Layout",
//                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
//                "No Attachment",
//                "-",
//                "-",
//                "-",
//                0
//
//            )
//        )


//        if (postParameterArray.isEmpty()) {
//            GenericUserFunction.showOopsError(
//                this,
//                "No Grievance found for the current request"
//            )
//        } else {
//
//            val adapter =
//                PostAdapter(postParameterArray, this)
//            recyclerView.adapter = adapter
//        }
    }

    fun clickFromDataPicker(view: View) {
        println(view)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            R.style.Theme_AppCompat_DayNight_Dialog,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                println(view)
                println(year)
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                select_from_date!!.text = sdf.format(date).toString()
            },
            year,
            month,
            day
        )
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        if(!isFinishing){
        dpd.show()}
    }

    fun clickToDataPicker(view: View) {
        println(view)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(

            this,
            R.style.Theme_AppCompat_DayNight_Dialog,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                println(view)
                println(year)
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                select_to_date!!.text = sdf.format(date).toString()
            },
            year,
            month,
            day
        )
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        if(!isFinishing){
        dpd.show()}
    }

    fun getApiResult(postfor: String) {
        if (InternetConnection.checkConnection(this)) {
            if (compareDate(select_from_date!!.text.toString(), select_to_date!!.text.toString())) {
                if(!isFinishing){


                val dialog: android.app.AlertDialog =
                    SpotsDialog.Builder().setContext(this).build()
                dialog.setMessage("Please Wait!!! \nwhile we are Getting Posts ")
                dialog.setCancelable(false)
                dialog.show()


                try {
                    var sosPostsRequest = SosPostsRequest(
                        "" + PID,
                        "" + select_to_date!!.text,
                        "" + select_from_date!!.text

                    )

                    mServices = Common.getAPI()

                    val data: MutableMap<String, String> = HashMap()
                    data["PID"] = PID
                    data["FROM_DATE"] = select_from_date!!.text.toString()
                    data["TO_DATE"] = select_to_date!!.text.toString()
                    data["S_ID"] = S_ID

                    mServices.getPostsData(
                        data
                    ).enqueue(object :
                        Callback<ArrayList<PostParameter>> {
                        override fun onFailure(call: Call<ArrayList<PostParameter>>, t: Throwable) {
                            if (!isFinishing) {
                                dialog.dismiss()
                                GenericUserFunction.showNegativePopUp(
                                    this@InboxActivity,
                                    t.message.toString()
                                )
                            }

                        }

                        override fun onResponse(
                            call: Call<ArrayList<PostParameter>>,
                            response: Response<ArrayList<PostParameter>>
                        ) {

                            if (response.code() == 200) {
                                var result = response.body()
                                if (result!!.size > 0) {

                                    postParameterArray = ArrayList<PostParameter>()
                                    for (i in 0..result!!.size - 1) {
                                        if (postfor == "Your") {
                                            val commentArray = ArrayList<CommentParameter>()

                                            var parentsNam = mutableSetOf<String>()
                                            parentsNam.add("Reply to post")
                                            var commentAgainst = ArrayList<String>()

                                            var comments = result[i].COMMENTS
                                            val com = StringBuilder()
                                            if (comments.size > 0) {
                                                for (j in 0..comments.size - 1) {
//                                                comments[i].SENDER_NAME + comments[i].COMMENT_DESC
                                                    if (j == 0) {
                                                        if (comments[j].CAGN != "Reply to post") {
                                                            com!!.insert(
                                                                0,
                                                                "<html><h4>${comments[j].SENDER_NAME} replied to <font color=\"#bebebe\" face = \"Comic sans MS\">${comments[j].CAGN}</font></h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>"
                                                            )
                                                        } else {
                                                            com!!.insert(
                                                                0,
                                                                "<html><h4>${comments[j].SENDER_NAME}</h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>"
                                                            )
                                                        }
                                                    } else {
                                                        if (comments[j].CAGN != "Reply to post") {
                                                            com!!.insert(
                                                                0,
                                                                "<html><hr><h4>${comments[j].SENDER_NAME} replied to <font color=\"#bebebe\" face = \"Comic sans MS\">${comments[j].CAGN}</font></h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>"
                                                            )
                                                        } else {
                                                            com!!.insert(
                                                                0,
                                                                "<html><hr><h4>${comments[j].SENDER_NAME}</h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>"
                                                            )
                                                        }
                                                    }
                                                    if (comments[j].SENDER_NAME == parentName) {

                                                    } else {
                                                        parentsNam.add(comments[j].SENDER_NAME)
                                                    }
                                                }
                                            } else {
                                                com!!.append("No Comments")
                                            }
                                            commentArray.add(
                                                0, CommentParameter(
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
                                            for (element in parentsNam) {
                                                println(element)
                                                commentAgainst.add(element)
                                            }
                                            if (result[i].PID == "SPE") {
                                                postParameterArray!!.add(
                                                    PostParameter(
                                                        "" + result[i].ID,

                                                        "" + PID,// getting user PID not from Response
                                                        "" + result[i].COUNS_ID,
                                                        "" + result[i].S_ID,
                                                        "" + result[i].POST_TITLE,
                                                        "" + result[i].POST_DESCRIPTION,
                                                        "" + result[i].POST_DATE_GEN,
                                                        "" + result[i].SCHOOL_NAME,
                                                        "" + result[i].POST_URL,
                                                        "" + result[i].ATTACH_TYPE,
                                                        "" + result[i].SENDER_ID,
                                                        "" + result[i].SENDER_NAME,
                                                        "" + result[i].SENDER,
                                                        +result[i].LIKES,
                                                        parentName,
                                                        commentArray,
                                                        "" + result[i].LIKES_STATUS_PAR,
                                                        "" + result[i].LIKES_STATUS_COUS,
                                                        "" + comments.size,
                                                        commentAgainst
                                                    )
                                                )
                                            }

                                        } else
                                            if (postfor == "Common") {
                                                val commentArray = ArrayList<CommentParameter>()

                                                var parentsNam = mutableSetOf<String>()
                                                parentsNam.add("Reply to post")
                                                var commentAgainst = ArrayList<String>()

                                                var comments = result[i].COMMENTS
                                                val com = StringBuilder()
                                                if (comments.size > 0) {
                                                    for (j in 0..comments.size - 1) {
//                                                comments[i].SENDER_NAME + comments[i].COMMENT_DESC
                                                        if (j == 0) {
                                                            if (comments[j].CAGN != "Reply to post") {
                                                                com!!.insert(
                                                                    0,
                                                                    "<html><h4>${comments[j].SENDER_NAME} replied to <font color=\"#bebebe\" face = \"Comic sans MS\">${comments[j].CAGN}</font></h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>"
                                                                )
                                                            } else {
                                                                com!!.insert(
                                                                    0,
                                                                    "<html><h4>${comments[j].SENDER_NAME}</h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>"
                                                                )
                                                            }
                                                            com.insert(0, "")
                                                        } else {
                                                            if (comments[j].CAGN != "Reply to post") {
                                                                com!!.insert(
                                                                    0,
                                                                    "<html><hr><h4>${comments[j].SENDER_NAME} replied to <font color=\"#bebebe\" face = \"Comic sans MS\">${comments[j].CAGN}</font></h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>"
                                                                )
                                                            } else {
                                                                com!!.insert(
                                                                    0,
                                                                    "<html><hr><h4>${comments[j].SENDER_NAME}</h4>&nbsp;&nbsp;${comments[j].COMMENT_DESC}</html>"
                                                                )
                                                            }
                                                        }
                                                        if (comments[j].SENDER_NAME == parentName) {

                                                        } else {
                                                            parentsNam.add(comments[j].SENDER_NAME)
                                                        }
                                                    }
                                                } else {
                                                    com!!.append("No Comments")
                                                }
                                                commentArray.add(
                                                    0, CommentParameter(
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
                                                for (element in parentsNam) {
                                                    println(element)
                                                    commentAgainst.add(element)
                                                }
                                                if (result[i].PID != "SPE") {
                                                    postParameterArray!!.add(
                                                        PostParameter(
                                                            "" + result[i].ID,
                                                            "" + PID,// getting user PID not from Response
                                                            "" + result[i].COUNS_ID,
                                                            "" + result[i].S_ID,
                                                            "" + result[i].POST_TITLE,
                                                            "" + result[i].POST_DESCRIPTION,
                                                            "" + result[i].POST_DATE_GEN,
                                                            "" + result[i].SCHOOL_NAME,
                                                            "" + result[i].POST_URL,
                                                            "" + result[i].ATTACH_TYPE,
                                                            "" + result[i].SENDER_ID,
                                                            "" + result[i].SENDER_NAME,
                                                            "" + result[i].SENDER,
                                                            +result[i].LIKES,
                                                            parentName,
                                                            commentArray,
                                                            "" + result[i].LIKES_STATUS_PAR,
                                                            "" + result[i].LIKES_STATUS_COUS,
                                                            "" + comments.size,
                                                            commentAgainst
                                                        )
                                                    )
                                                }
                                            }
                                    }
                                    if(!isFinishing){
                                    dialog.dismiss()}
                                    adapter =
                                        PostAdapter(
                                            postParameterArray!!,
                                            this@InboxActivity,
                                            PID,
                                            select_from_date!!.text.toString(),
                                            select_to_date!!.text.toString(),
                                            S_ID,
                                            postfor,
                                            parentName,
                                            this@InboxActivity
                                        )
                                    if (postParameterArray!!.isEmpty()) {
//                                    if (!isFinishing){ postParameterArray.clear()
                                        postParameterArray!!.clear()
                                        adapter!!.notifyDataSetChanged()
                                        recyclerView!!.adapter = adapter
                                        GenericUserFunction.showOopsError(
                                            this@InboxActivity,
                                            "Posts not delivered for you"
                                        )
//                                    adapter.notifyDataSetChanged()
//                                    }
                                    } else {
//                                    var mDividerItemDecoration = DividerItemDecoration(
//                                        recyclerView!!.context,
//                                    RecyclerView.LayoutManager.get
//                                    )
//                                    recyclerView!!.addItemDecoration(mDividerItemDecoration)

                                        recyclerView!!.adapter = adapter!!

                                    }

                                }
                                println(result)
                            } else {
                                if(!isFinishing){
                                dialog.dismiss()}

                            }
                            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                } catch (ex: Exception) {
                    if(!isFinishing){
                    dialog.dismiss()
                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )}
                }
            }}
        } else {
            if(!isFinishing){
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )}
        }

    }

    fun compareDate(toDate: String, fromDate: String): Boolean {


        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val date1: Date = sdf.parse(toDate)
        val date2: Date = sdf.parse(fromDate)
        println("Date compare >>> " + date1.compareTo(date2))
        if (date1.compareTo(date2) <= 0) {
            return true
        } else {
            val snackbar = Snackbar
                .make(rootlayout, "Please select proper Date Range", Snackbar.LENGTH_LONG)
            snackbar.show()
            return false
        }
    }


}
