package com.soscubs.soscounsellingapp.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.Child_NAME
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.Date
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.ID
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.Parent_EMAIL
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.Parent_Mobile
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.Parent_NAME
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.School_NAME
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.UserRole
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.mypref
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.permission
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable.Companion.remainingPermissions
import com.soscubs.soscounsellingapp.Generic.GenericUserFunction.Companion.showApiError
import com.soscubs.soscounsellingapp.Generic.GenericUserFunction.Companion.showInternetNegativePopUpSplash
import com.soscubs.soscounsellingapp.Generic.GenericUserFunction.Companion.showPerMissNegativePopUp
import com.soscubs.soscounsellingapp.Generic.InternetConnection
import com.soscubs.soscounsellingapp.R
import com.soscubs.soscounsellingapp.dashboard.DashboardUser
import com.soscubs.soscounsellingapp.remote.ApiClientPhp
import com.soscubs.soscounsellingapp.remote.PhpApiInterface
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashScreen : AppCompatActivity() {
    var runnable = Runnable {
        starActivity1()
    }

    private lateinit var myCustomFont: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        myCustomFont =
            Typeface.createFromAsset(assets, "candyroundbtnbold.ttf")

        if (InternetConnection.checkConnection(this@SplashScreen)) {
            try {
                val phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                    PhpApiInterface::class.java
                )
                val call3: Call<JsonObject> =
                    phpApiInterface.GetAndroidVersion("SosCubs", "CheckVersion")

                call3.enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        if (!isFinishing) {
                            showInternetNegativePopUpSplash(
                                this@SplashScreen,
                                getString(R.string.failureSSApiVerErr)
                            )
                        }
                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        try {
                            val result: JsonObject? = response.body()
                            val apiStatus = result!!.get("status")
                            val responseData = result.get("response")
                            if (apiStatus.toString() == "200") {
                                val appVersion =
                                    responseData.asJsonObject.get("app_version").asString
                                val pinfo = packageManager.getPackageInfo(packageName, 0)
                                val versionNumber = pinfo.versionCode
                                if (versionNumber.toString() == appVersion.toString()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (arePermissionsEnabled()) {
                                            val handler = Handler()
                                            handler.postDelayed(runnable, 800)
                                        } else {
                                            requestMultiplePermissions()
                                        }
                                    } else {
                                        val handler = Handler()
                                        handler.postDelayed(runnable, 1300)
                                    }
                                } else {
                                    showInternetNegativePopUpSplash(
                                        this@SplashScreen,
                                        getString(R.string.failureUpdateApiVerErr)
                                    )
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            if (!isFinishing) {
                                showApiError(
                                    this@SplashScreen,
                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                )
                            }
                        }
                    }
                })
            } catch (ex: Exception) {
                if (!isFinishing) {
                    ex.printStackTrace()
                    showApiError(
                        this,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
            }
        } else {
            if (!isFinishing) {
                showInternetNegativePopUpSplash(
                    this@SplashScreen,
                    getString(R.string.failureNoInternetErr)
                )
            }
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
            val intent = Intent(
                this@SplashScreen,
                DashboardUser::class.java
            )
            startActivity(intent)

        } else {
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
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
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        if (!isFinishing) {
                            showPerMissNegativePopUp(
                                this@SplashScreen,
                                getString(R.string.failureStorageErr)
                            )
                        }
                    }
                    return
                } else {
                    val handler = Handler()
                    handler.postDelayed(runnable, 1300)
                }
            }

        }
    }
}
