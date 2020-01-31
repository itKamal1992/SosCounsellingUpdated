package com.example.soscounsellingapp.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import com.example.soscounsellingapp.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.mypref
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.ID
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.Parent_NAME
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.Parent_Mobile
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.Parent_EMAIL
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.Child_NAME
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.Address
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.School_CLASS
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.School_NAME
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.Date
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.UserRole
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.permission
import com.example.soscounsellingapp.Generic.GenericPublicVariable.Companion.remainingPermissions
import com.example.soscounsellingapp.Generic.GenericUserFunction.Companion.showApiError
import com.example.soscounsellingapp.Generic.GenericUserFunction.Companion.showInternetNegativePopUpSplash
import com.example.soscounsellingapp.Generic.GenericUserFunction.Companion.showPerMissNegativePopUp
import com.example.soscounsellingapp.Generic.GenericUserFunction.Companion.showUpdateNotification
import com.example.soscounsellingapp.Generic.InternetConnection
import com.example.soscounsellingapp.dashboard.DashboardUser
import com.example.soscounsellingapp.model.APIResponse
import com.example.soscounsellingapp.remote.ApiClientPhp
import com.example.soscounsellingapp.remote.PhpApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashScreen : AppCompatActivity() {
    var runnable = Runnable {
        starActivity1()
    }

    lateinit var myCustomFont: Typeface
    lateinit var myCustomFont2: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        var handler = Handler()
//        handler.postDelayed(runnable, 2000)


        myCustomFont =
            Typeface.createFromAsset(assets, "candyroundbtnbold.ttf")

            if (InternetConnection.checkConnection(this@SplashScreen)) {
            try {

                var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                    PhpApiInterface::class.java
                )
                /*   var call3: Call<ApiVersion> = phpApiInterface.api_version()*/

                var call3: Call<APIResponse> = phpApiInterface.GetAndroidVersion()

                call3.enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        if(!isFinishing) {
                            showInternetNegativePopUpSplash(
                                this@SplashScreen,
                                getString(R.string.failureSSApiVerErr)
                            )
                        }
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        try {
                            val result: APIResponse? = response.body()
                            if (result!!.NEW_VERSION.isNotEmpty()) {
                                var pinfo = packageManager.getPackageInfo(packageName, 0)
                                var versionNumber = pinfo.versionCode
                                println("versionNumber heere: "+versionNumber)
                                println("API Version : "+result.NEW_VERSION)
                                var testAPI=result.NEW_VERSION
//                                if (versionNumber.toString() == result.NEW_VERSION) {
                                    if (testAPI == result.NEW_VERSION) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (arePermissionsEnabled()) {
                                            var handler = Handler()
                                            handler.postDelayed(runnable, 1300)
                                        } else {
                                            requestMultiplePermissions()
                                        }
                                    } else {
                                        var handler = Handler()
                                        handler.postDelayed(runnable, 1300)
                                    }
                                } else {
                                    /*     showInternetNegativePopUpSplash(
                                             this@SplashScreen,
                                             getString(R.string.failureUpdateApiVerErr)
                                         )*/

                                    showUpdateNotification(  this@SplashScreen,
                                        response.body()!!.DESCRIPTION)}

                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            showApiError(
                                this@SplashScreen,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    }
                })

//                mServices.AppVersion()
//                    .enqueue(object : Callback<APIResponse> {
//                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//                            showInternetNegativePopUp(
//                                this@SplashScreen,
//                                getString(R.string.failureSSApiVerErr)
//                            )
//                        }
//
//                        override fun onResponse(
//                            call: Call<APIResponse>,
//                            response: Response<APIResponse>
//                        ) {
//                            try {
//                                val result: APIResponse? = response.body()
//                                if (result!!.Responsecode == 200 && result.Status == "ok") {
//                                    var pinfo = packageManager.getPackageInfo(packageName, 0)
//                                    var versionNumber = pinfo.versionCode
//                                    if (versionNumber.toString() == result.Data15!!.NEW_VERSION) {
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                            if (arePermissionsEnabled()) {
//                                                var handler = Handler()
//                                                handler.postDelayed(runnable, 1300)
//                                            } else {
//                                                requestMultiplePermissions()
//                                            }
//                                        } else {
//                                            var handler = Handler()
//                                            handler.postDelayed(runnable, 1300)
//                                        }
//                                    } else {
//                                        showSplashNegativePopUp(
//                                            this@SplashScreen,
//                                            getString(R.string.failureUpdateApiVerErr)
//                                        )
//                                    }
//                                } else {
//                                    showInternetNegativePopUp(
//                                        this@SplashScreen,
//                                        getString(R.string.failureServerApiVerErr)
//                                    )
//                                }
//                            } catch (ex: Exception) {
//                                ex.printStackTrace()
//                                showApiError(
//                                    this@SplashScreen,
//                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
//                                )
//                            }
//                        }
//
//
//                    })
            } catch (ex: Exception) {
                ex.printStackTrace()
                showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else
        {
            showInternetNegativePopUpSplash(
                this@SplashScreen,
                getString(R.string.failureNoInternetErr)
            )
        }


    }

    private fun starActivity1() {

        mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        ID =
            mypref!!.getString("ID", null)
        Parent_NAME =
            mypref!!.getString("Parent_NAME", null)
        Parent_Mobile =
            mypref!!.getString("Parent_Mobile", null)
        Parent_EMAIL =
            mypref!!.getString("Parent_EMAIL", null)
        Child_NAME =
            mypref!!.getString("Child_NAME", null)
        School_NAME =
            mypref!!.getString("School_NAME", null)
        Date =
            mypref!!.getString("RegistrationDate", null)
        UserRole =
            mypref!!.getString("UserRole", null)

        println("mypref " + mypref.toString())

        if (UserRole.equals("Parent")) {
            var intent = Intent(
                this@SplashScreen,
                DashboardUser::class.java
            )
            startActivity(intent)

        } else {
            var intent = Intent(this@SplashScreen,MainActivity::class.java)
//            var intent = Intent(this@SplashScreen,ActivityContacts::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun arePermissionsEnabled(): Boolean {
        for (permission: String in permission) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestMultiplePermissions() {
        for (permission: String in permission) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                remainingPermissions.add(permission)
        }
        requestPermissions(
            remainingPermissions.toArray(arrayOf("" + remainingPermissions.size)),
            101
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            for (i in 0 until grantResults.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        showPerMissNegativePopUp(
                            this@SplashScreen,
                            getString(R.string.failureStorageErr)
                        )
                    }
                    return
                } else {
                    var handler = Handler()
                    handler.postDelayed(runnable, 1300)
                }
            }

        }
    }
}
