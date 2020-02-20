package com.soscubs.soscounsellingapp.dashboard

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.soscubs.soscounsellingapp.Adapter.ViewPagerAdapter
import com.soscubs.soscounsellingapp.Fcm.NotificationForground
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable
import com.soscubs.soscounsellingapp.R
import com.soscubs.soscounsellingapp.activity.InboxActivity
import com.soscubs.soscounsellingapp.activity.MainActivity
import com.soscubs.soscounsellingapp.activity.SplashScreen
import com.soscubs.soscounsellingapp.model.APIResponse
import com.soscubs.soscounsellingapp.remote.ApiClientPhp
import com.soscubs.soscounsellingapp.remote.PhpApiInterface
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.soscubs.soscounsellingapp.DataClass.CounsellerData
import com.soscubs.soscounsellingapp.Generic.GenericUserFunction
import com.soscubs.soscounsellingapp.Generic.InternetConnection
import com.soscubs.soscounsellingapp.activity.AddChildActivity
import com.soscubs.soscounsellingapp.common.Common
import com.soscubs.soscounsellingapp.remote.IMyAPI
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_dashboard_user.*
import kotlinx.android.synthetic.main.content_dashboard_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.system.exitProcess


class DashboardUser : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var dotsCount: Int = 0
    private var dots: Array<ImageView?>? = null
    var parentName: String = ""
    var parentMobile: String = ""
    var whatsApptMobile: String = ""
    var parentEmail: String = ""
    var childName: String = ""
    var schoolName: String = ""
    var schoolClass: String = ""
    var Address: String = ""
    var UserRole: String = ""
    var SID: String = ""
    var PID: String = ""
    var enquiryDate: String = ""
    var coMobile: String = ""
    var couns_id: String = ""

    var childName1: String = ""
    var schoolClass1: String = ""

    var auth: FirebaseAuth? = null
    private var TOKEN_ID1: String = ""
    private var TOKEN_ID: String = ""


    var img1: String = ""
    var img2: String = ""
    var img3: String = ""
    var img4: String = ""
    var img5: String = ""

    var sidurl: Int = 0

    private lateinit var mServices: IMyAPI


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_user)
        mServices = Common.getAPI()
        auth = FirebaseAuth.getInstance()
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result!!.token
                //saveToken(token)
                TOKEN_ID1 = token

                println("Token :--> $token")
                //textView.setText("Token : "+token);*/
            } else {

            }
        }
        var notificationForground: NotificationForground


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            (R.string.navigation_drawer_open),
            (R.string.navigation_drawer_close)
        ) {

        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()





        nav_view.setNavigationItemSelectedListener(this)


        val navigationView = findViewById(R.id.nav_view) as NavigationView
        val headerView: View = navigationView.getHeaderView(0)
        val tv_parentName = headerView.findViewById(R.id.tv_parentName) as TextView
        val tv_Email = headerView.findViewById(R.id.tv_Email) as TextView

        val tv_Mobile = headerView.findViewById(R.id.tv_Mobile) as TextView
        val tv_WhatsApp = headerView.findViewById(R.id.tv_WhatsApp) as TextView
        val header_linearlayout_line =
            headerView.findViewById(R.id.header_linearlayout_line) as LinearLayout

        val tv_ChildName = headerView.findViewById(R.id.tv_ChildName) as TextView
        val tv_School = headerView.findViewById(R.id.tv_School) as TextView
        val tv_Class = headerView.findViewById(R.id.tv_Class) as TextView

        val linearLayout_child1=headerView.findViewById(R.id.linearLayout_child1) as LinearLayout
        val tv_ChildName1 = headerView.findViewById(R.id.tv_ChildName1) as TextView
        val tv_Class1 = headerView.findViewById(R.id.tv_Class1) as TextView



        helpGrid
        ////Code for Setting Details

//        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
//        Uid          = mypref.getString("ID", null)
//        parentName   = mypref.getString("Parent_NAME", null)
//        parentMobile = mypref.getString("Parent_Mobile", null)
//        parentEmail  = mypref.getString("Parent_EMAIL", null)
//        childName    = mypref.getString("Child_NAME", null)
//        schoolClass  = mypref.getString("School_CLASS", null)
//        schoolName   = mypref.getString("School_NAME", null)
//
//
//


        ////Code for Setting Details

        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        PID = mypref.getString("PID", null)
        parentName = mypref.getString("Parent_NAME", null)
        parentMobile = mypref.getString("Parent_Mobile", null)
        parentEmail = mypref.getString("Parent_EMAIL", null)
        childName = mypref.getString("Child_NAME", null)
        Address = mypref.getString("Address", null)
        schoolClass = mypref.getString("School_CLASS", null)
        schoolName = mypref.getString("School_NAME", null)
        enquiryDate = mypref.getString("RegistrationDate", null)

        UserRole = mypref.getString("UserRole", null)
        SID = mypref.getString("SID", null)
        whatsApptMobile = mypref.getString("Whatsapp_mno", null)
        coMobile = mypref.getString("Co_mobile", null)
        couns_id = mypref.getString("couns_id", null)
        TOKEN_ID = mypref.getString("TOKEN_ID", null)

        sidurl = mypref.getString("SID", null).toInt()

        childName1 = mypref.getString("Child_NAME1", null)
        schoolClass1 = mypref.getString("School_CLASS1", null)
        println("sidurl   " + sidurl)

        GetSliderImage()

        InsertToken(
            PID,
            TOKEN_ID,
            parentName,
            schoolName,
            SID,
            couns_id,
            schoolClass,
            parentMobile,
            parentEmail,
            whatsApptMobile,
            UserRole,
            enquiryDate,
            "",
            ""
        )

        tv_parentName.text = "Parent Name : $parentName"
        tv_Email.setText("Email : $parentEmail")

        tv_ChildName.setText("Child Name : $childName")
        tv_School.setText("School : $schoolName")
        tv_Class.setText("Class : $schoolClass")

        if (parentMobile == whatsApptMobile) {
            tv_Mobile.visibility = View.GONE
            header_linearlayout_line.visibility = View.GONE
            tv_WhatsApp.text = "What's App : $whatsApptMobile"
        } else {
            tv_Mobile.text = "Mobile : $parentMobile"
            tv_WhatsApp.text = "What's App : $whatsApptMobile"
        }

        if (childName1.equals(""))
        {
            linearLayout_child1.visibility=View.GONE
            addChildGrid.visibility=View.VISIBLE
        }else
        {
            linearLayout_child1.visibility=View.VISIBLE
            tv_ChildName1.setText("Child Name : $childName1")
            tv_Class1.setText("Class : $schoolClass1")
            addChildGrid.visibility=View.GONE
            hideItem()
        }

        inboxGrid.setOnClickListener {
            var intent = Intent(this, InboxActivity::class.java)
            intent.putExtra("PID", PID)
            intent.putExtra("parentName", parentName)
            intent.putExtra("S_ID", SID)
            startActivity(intent)

        }
        addChildGrid.setOnClickListener {
            var intent = Intent(this, AddChildActivity::class.java)
            intent.putExtra("PID", PID)
            intent.putExtra("parentName", parentName)
            intent.putExtra("S_ID", SID)
            intent.putExtra("schoolName", schoolName)

            startActivity(intent)

        }

        helpGrid.setOnClickListener {
            helpDialog()
        }
    }

    private fun hideItem() {
        var navigationView = findViewById(R.id.nav_view) as NavigationView
        val nav_Menu = navigationView.getMenu()
        nav_Menu.findItem(R.id.nav_add_child).setVisible(false)
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBackPressed() {
        exitDialog()
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun exitDialog() {
//        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
//        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_exit, null)
//        dialogBuilder.setView(dialogView)
//            .setCancelable(false)
//            .setPositiveButton("Yes") { dialog, id ->
//
//                finishAffinity()
//                exitProcess(0)
//            }
//            // negative button text and action
//            .setNegativeButton("No") { dialog, id ->
//                dialog.cancel()
//            }
//
//        // create dialog box
//        val alert = dialogBuilder.create()
//        // set title for alert dialog box
//
//        // show alert dialog
//        alert.show()

        if(!isFinishing) {
            //////////Start///////////////
            GenericPublicVariable.CustDialog = Dialog(this)
            GenericPublicVariable.CustDialog.setContentView(R.layout.custom_dialog_exit)
            var ivNegClose1: ImageView =
                GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
            var btnYes: Button =
                GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
            var btnNo: Button =
                GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogReject) as Button
//        var btnCancel: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
//        var tvMsg: TextView =
//            GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                            var txtcontact: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.txtcontact) as TextView
//                            txtcontact.visibility=View.GONE
//                            tvMsg.text = "Congratulation!!! Your password han been reset successfully."
//        GenericPublicVariable.CustDialog.setCancelable(false)
            btnYes.setOnClickListener {
                GenericPublicVariable.CustDialog.dismiss()
                finishAffinity()
                exitProcess(0)
            }
            btnNo.setOnClickListener {
                GenericPublicVariable.CustDialog.dismiss()

            }
//        ivNegClose1.setOnClickListener {
//            GenericPublicVariable.CustDialog.dismiss()
////            callDashboard()
//        }
            GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    Color.TRANSPARENT
                )
            )
            GenericPublicVariable.CustDialog.show()
            //////////End//////////////
        }
    }

    private fun helpDialog() {
        if (InternetConnection.checkConnection(this)) {

            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are Counsellor Info ")
            dialog.setCancelable(false)
            dialog.show()
            try {
                val data: MutableMap<String, String> = HashMap()
                data["S_ID"] = SID
                mServices.getCounseller(data).enqueue(object :
                    Callback<CounsellerData> {
                    override fun onFailure(call: Call<CounsellerData>, t: Throwable) {
                        if (!isFinishing) {
                            dialog.dismiss()
                            GenericUserFunction.DisplayToast(
                                this@DashboardUser,
                                "Server seems to be busy,\nPlease try after some time"
                            )
//                        val snackbar = Snackbar
//                            .make(
//                                this@DashboardUser.currentFocus,
//                                "Sorry for inconvenience\n" +
//                                        "Server seems to be busy,\n" +
//                                        "Please try after some time.",
//                                Snackbar.LENGTH_LONG
//                            )
//                        snackbar.show()
                        }
                    }

                    override fun onResponse(
                        call: Call<CounsellerData>,
                        response: Response<CounsellerData>
                    ) {
                        if (response.code() == 200) {
                            dialog.dismiss()
                            var result: CounsellerData = response.body()!!
                            result.COUNS_NAME
                            result.EMAIL
                            result.MOBNO


                            //////////Start///////////////
                            GenericPublicVariable.CustDialog = Dialog(this@DashboardUser)
                            GenericPublicVariable.CustDialog.setContentView(R.layout.custom_dialog_help)
//                            var ivNegClose1: ImageView =
//                                GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                            var btnOk: Button =
                                GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//        var btnCancel: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                            var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                            tvMsg.text = "${result.COUNS_NAME} : ${result.MOBNO}"
                            var txtcontact: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.txtcontact) as TextView
                            txtcontact.text="${result.COUNS_NAME}\nMobile No : ${result.MOBNO.toString()}\nEmail : ${result.EMAIL.toString()}"
//        GenericPublicVariable.CustDialog.setCancelable(false)
                            btnOk.setOnClickListener {
                                if (!isFinishing) {
                                    GenericPublicVariable.CustDialog.dismiss()
                                }
                            }
//        btnCancel.setOnClickListener {
//            GenericPublicVariable.CustDialog.dismiss()
//
//        }
//                            ivNegClose1.setOnClickListener {
//                                if (!isFinishing) {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                }
//                            }
                            if (!isFinishing) {
                                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
                                    ColorDrawable(
                                        Color.TRANSPARENT
                                    )
                                )
                                GenericPublicVariable.CustDialog.show()
                                //////////End//////////////
                            }

                        }


//                        val snackbar = Snackbar
//                            .make(
//                                drawer_layout,
//                                "Sorry for inconvenience\nPlease try after some time.",
//                                Snackbar.LENGTH_LONG
//                            )
//                        snackbar.show()


                    }

                })
            }catch (ex:java.lang.Exception){
                if(!isFinishing){
                dialog.dismiss()
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )}
            }

        }else {
            if(!isFinishing){
                GenericUserFunction.showInternetNegativePopUp(
                    this,
                    getString(R.string.failureNoInternetErr)
                )}
        }



    }


    private fun GetSliderImage() {


        try {


            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                PhpApiInterface::class.java
            )

            var call: Call<APIResponse> = phpApiInterface.GetSliderImage(SID.toInt())

            call.enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    println("faile  ")
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {

                    var result = response.body()

                    var intsizeresult = response.body()!!.data1!!.size


                    println("intsizeresult  " + result!!.data1!!.size)



                    result!!.data1!!.size


                    println("success  " + result!!.data1!![0].Url)

                    if (result!!.data1!!.size > 1) {
                        img1 = result!!.data1!![0].Url
                        img2 = result!!.data1!![1].Url
                        img3 = result!!.data1!![2].Url
                        img4 = result!!.data1!![3].Url
                        img5 = result!!.data1!![4].Url

                        viewPagerMethod(img1, img2, img3, img4, img5)
                    } else {
                        println("not greater")

                        viewPagerMethod(
                            "http://dmimsdu.in/web/api_cubs/viewpager_image/common_img.jpg",
                            "http://dmimsdu.in/web/api_cubs/viewpager_image/sos-atrey__1.jpg",
                            "http://dmimsdu.in/web/api_cubs/viewpager_image/sos-atrey__2.jpg",
                            "http://dmimsdu.in/web/api_cubs/viewpager_image/sos-atrey__3.jpg",
                            "http://dmimsdu.in/web/api_cubs/viewpager_image/sos-atrey__4.jpg"
                        )
                    }


                    // println(img1+","+img2+","+img3+","+img4+","+img5)


                    //     viewPagerMethod(img1,img2,img3,img4,img5)
                }

            })


        } catch (ex: Exception) {
            ex.printStackTrace()

        }


    }

    private fun viewPagerMethod(
        imgdata1: String,
        imgdata2: String,
        imgdata3: String,
        imgdata4: String,
        imgdata5: String
    ) {


        val urls = arrayOf(
            imgdata1, imgdata2, imgdata3, imgdata4, imgdata5
        )


        println("data showing" + imgdata1)
        println("getting urls " + urls)
        //ViewPager
        val viewPagerAdapter = ViewPagerAdapter(this, urls)
        viewPager.adapter = viewPagerAdapter
        dotsCount = viewPagerAdapter.count
        dots = arrayOfNulls<ImageView>(dotsCount)


        for (i in 0 until dotsCount) {
            dots!![i] = ImageView(this)
            dots!![i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.nonactive_dots
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            SliderDots.addView(dots!![i], params)
            dots!![i]?.setOnClickListener { viewPager.currentItem = i }

        }

        dots!![0]?.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.active_dots
            )
        )

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotsCount) {
                    dots!![i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@DashboardUser,
                            R.drawable.nonactive_dots
                        )
                    )
                }

                dots!![position]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DashboardUser,
                        R.drawable.active_dots
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        val timer = Timer()
        timer.scheduleAtFixedRate(MyTimerTask(), 2000, 4000)

    }

    private fun InsertToken(
        pid: String,
        tokenId: String?,
        parentName: String?,
        schoolName: String?,
        sid: String?,
        cid: String?,
        schoolClass: String?,
        parentMobile: String?,
        parentEmail: String?,
        whatsApptMobile: String?,
        userRole: String?,
        enquiryDate: String?,
        s11: String,
        s12: String
    ) {

        try {
            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                PhpApiInterface::class.java
            )

            var call: Call<APIResponse> = phpApiInterface.InsertToken(
                pid, tokenId!!, parentName!!, schoolName!!, sid!!, cid!!, schoolClass!!,
                parentMobile!!, parentEmail!!, whatsApptMobile!!,
                userRole!!, enquiryDate!!, s11, s12
            )

            call.enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    println("faile  ")
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {

                    println("success  " + response)

                }

            })


        } catch (ex: Exception) {
            ex.printStackTrace()

        }


    }


    inner class MyTimerTask : TimerTask() {

        override fun run() {
            this@DashboardUser.runOnUiThread(Runnable {
                if (viewPager.currentItem == 0) {
                    viewPager.currentItem = 1
                } else if (viewPager.currentItem == 1) {
                    viewPager.currentItem = 2
                } else if (viewPager.currentItem == 2) {
                    viewPager.currentItem = 3
                } else if (viewPager.currentItem == 3) {
                    viewPager.currentItem = 4
                } else if (viewPager.currentItem == 4) {
                    viewPager.currentItem = 5
                    viewPager.currentItem = 0
                } else {
                    viewPager.currentItem = 0
                }
            })

        }
    }

    override fun onNavigationItemSelected(menuitem: MenuItem): Boolean {
        when (menuitem.itemId) {

            R.id.nav_inbox -> {

                var intent = Intent(this, InboxActivity::class.java)
                intent.putExtra("PID", PID)
                intent.putExtra("parentName", parentName)
                intent.putExtra("S_ID", SID)

                startActivity(intent)
            }

            R.id.nav_add_child -> {
                var intent = Intent(this, AddChildActivity::class.java)
                intent.putExtra("PID", PID)
                intent.putExtra("parentName", parentName)
                intent.putExtra("S_ID", SID)
                intent.putExtra("schoolName", schoolName)

                startActivity(intent)

            }

            R.id.nav_help -> {

                helpDialog()
            }

            R.id.nav_logout -> {

                var sharepref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                var editor = sharepref.edit()
                editor.clear()
                editor.commit()
                val intentlogout = Intent(
                    this@DashboardUser,
                    MainActivity::class.java
                )
                startActivity(intentlogout)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
