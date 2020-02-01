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
import com.example.soscounsellingapp.activity.MainActivity
import com.example.soscounsellingapp.activity.SplashScreen
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_dashboard_user.*
import kotlinx.android.synthetic.main.content_dashboard_main.*
import java.util.*


class CounsellorDashboard : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var dotsCount: Int = 0
    private var dots: Array<ImageView?>? = null

    var parentName:String=""
    var parentMobile:String=""
    var parentEmail:String=""
    var childName:String=""
    var schoolName:String=""
    var schoolClass:String=""
    var Uid:String=""


    private val urls = arrayOf(
        "https://www.dmimsdu.in/web/api_cubs/viewpager_image/common_img.jpg",
        "https://demonuts.com/Demonuts/SampleImages/W-08.JPG",
        "https://demonuts.com/Demonuts/SampleImages/W-10.JPG",
        "https://demonuts.com/Demonuts/SampleImages/W-13.JPG",
        "https://demonuts.com/Demonuts/SampleImages/W-17.JPG",
        "https://demonuts.com/Demonuts/SampleImages/W-21.JPG"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counsellor_dashboard)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar

        val drawerToggle: ActionBarDrawerToggle =object : ActionBarDrawerToggle(
            this,drawer_layout,toolbar,
            (R.string.navigation_drawer_open),
            (R.string.navigation_drawer_close))
        {

        }
        drawerToggle.isDrawerIndicatorEnabled=true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()



        nav_view.setNavigationItemSelectedListener(this)
        //ViewPager
        val viewPagerAdapter = ViewPagerAdapter(this,urls)
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
                            this@CounsellorDashboard,
                            R.drawable.nonactive_dots
                        )
                    )
                }

                dots!![position]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@CounsellorDashboard,
                        R.drawable.active_dots
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int)
            {

            }
        })

        val timer = Timer()
        timer.scheduleAtFixedRate(MyTimerTask(), 2000, 4000)

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        val headerView: View = navigationView.getHeaderView(0)
        val tv_parentName = headerView.findViewById(R.id.tv_parentName) as TextView
        val tv_Email = headerView.findViewById(R.id.tv_Email) as TextView
        val tv_Mobile = headerView.findViewById(R.id.tv_Mobile) as TextView
        val tv_ChildName = headerView.findViewById(R.id.tv_ChildName) as TextView
        val tv_School = headerView.findViewById(R.id.tv_School) as TextView
        val tv_Class = headerView.findViewById(R.id.tv_Class) as TextView

//        Code for Setting Details
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        Uid          = mypref.getString("ID", null)
        parentName   = mypref.getString("Parent_NAME", null)
        parentMobile = mypref.getString("Parent_Mobile", null)
        parentEmail  = mypref.getString("Parent_EMAIL", null)
        childName    = mypref.getString("Child_NAME", null)
        schoolClass  = mypref.getString("School_CLASS", null)
        schoolName   = mypref.getString("School_NAME", null)



        tv_parentName.text=parentName
        tv_Email.setText(parentEmail)
        tv_Mobile.setText(parentMobile)
        tv_ChildName.setText(childName)
        tv_School.setText(schoolClass)
        tv_Class.setText(schoolName)


//        inboxGrid.setOnClickListener {
//
//
//        }


    }

    inner class MyTimerTask : TimerTask() {

        override fun run() {
            this@CounsellorDashboard.runOnUiThread(Runnable {
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
        when(menuitem.itemId)
        {
            R.id.nav_home->
            {

                val intent=Intent(this@CounsellorDashboard,MainActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_logout->
            {

                var sharepref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                var editor = sharepref.edit()
                editor.clear()
                editor.commit()
                val intentlogout = Intent(
                    this@CounsellorDashboard,
                    SplashScreen::class.java
                )
                startActivity(intentlogout)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
