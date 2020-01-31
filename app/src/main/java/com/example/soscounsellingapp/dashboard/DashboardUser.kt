package com.example.soscounsellingapp.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.example.soscounsellingapp.Adapter.ViewPagerAdapter
import com.example.soscounsellingapp.R
import com.example.soscounsellingapp.activity.InboxActivity
import com.example.soscounsellingapp.activity.MainActivity
import com.example.soscounsellingapp.activity.SplashScreen
import com.example.soscounsellingapp.model.APIResponse
import com.example.soscounsellingapp.remote.ApiClientPhp
import com.example.soscounsellingapp.remote.PhpApiInterface
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_dashboard_user.*
import kotlinx.android.synthetic.main.content_dashboard_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


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

    var auth: FirebaseAuth? = null
    private var TOKEN_ID1: String = ""
    private var TOKEN_ID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_user)

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
        //ViewPager
        val viewPagerAdapter = ViewPagerAdapter(this)
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


        val navigationView = findViewById(R.id.nav_view) as NavigationView
        val headerView: View = navigationView.getHeaderView(0)
        val tv_parentName = headerView.findViewById(R.id.tv_parentName) as TextView
        val tv_Email = headerView.findViewById(R.id.tv_Email) as TextView

        val tv_Mobile = headerView.findViewById(R.id.tv_Mobile) as TextView
        val tv_WhatsApp = headerView.findViewById(R.id.tv_WhatsApp) as TextView
        val header_linearlayout_line = headerView.findViewById(R.id.header_linearlayout_line) as LinearLayout

        val tv_ChildName = headerView.findViewById(R.id.tv_ChildName) as TextView
        val tv_School = headerView.findViewById(R.id.tv_School) as TextView
        val tv_Class = headerView.findViewById(R.id.tv_Class) as TextView


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

        tv_parentName.text="Parent Name : $parentName"
        tv_Email.setText("Email : $parentEmail")

        tv_ChildName.setText("Child Name : $childName")
        tv_School.setText("School : $schoolName")
        tv_Class.setText("Class : $schoolClass")

        if (parentMobile == whatsApptMobile){
            tv_Mobile.visibility=View.GONE
            header_linearlayout_line.visibility=View.GONE
            tv_WhatsApp.text="What's App : $whatsApptMobile"
        }else
        {
            tv_Mobile.text="Mobile : $parentMobile"
            tv_WhatsApp.text="What's App : $whatsApptMobile"
        }

        inboxGrid.setOnClickListener {
            var intent = Intent(this, InboxActivity::class.java)
            intent.putExtra("PID",PID)
            intent.putExtra("parentName",parentName)
            intent.putExtra("S_ID",SID)

            startActivity(intent)

        }

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
            R.id.nav_home -> {

                val intent = Intent(this@DashboardUser, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_logout -> {

                var sharepref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                var editor = sharepref.edit()
                editor.clear()
                editor.commit()
                val intentlogout = Intent(
                    this@DashboardUser,
                    SplashScreen::class.java
                )
                startActivity(intentlogout)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
