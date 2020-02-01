package com.example.soscounsellingapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.soscounsellingapp.R
import com.example.soscounsellingapp.common.Common
import com.example.soscounsellingapp.dashboard.DashboardUser
import com.example.soscounsellingapp.model.APIResponse
import com.example.soscounsellingapp.model.GetLoginData
import com.example.soscounsellingapp.remote.ApiClientPhp
import com.example.soscounsellingapp.remote.IMyAPI
import com.example.soscounsellingapp.remote.PhpApiInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    private var TOKEN_ID: String = ""

    private lateinit var mServices: IMyAPI


    lateinit var et_username: EditText
    lateinit var et_password: EditText


    var userName: String = ""
    var password: String = ""

    var email: String = ""
    var mobile: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        mServices = Common.getAPI()

        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = mypref.edit()


        btn_newRegistration.setOnClickListener {
            val intent = Intent(this@MainActivity, NewRegistration::class.java)
            startActivity(intent)

//         getClg()
//            getVersion()

        }


        auth = FirebaseAuth.getInstance()
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result!!.token
                //saveToken(token)
                TOKEN_ID = token
                editor.putString("TOKEN_ID",TOKEN_ID)
                editor.apply()

                println("Token :--> $token")
                //textView.setText("Token : "+token);*/
            } else {

            }
        }

        et_username = findViewById(R.id.et_username)
        et_password = findViewById(R.id.et_password)



        btn_Login.setOnClickListener {

            userName = et_username.text.toString().toLowerCase()
            password = et_password.text.toString()

            if (userName.equals(""))
            {
                et_username.setError("Please enter username")
            }else if (password.length<8)
            {
                et_password.setError("Please enter valid password")
            }else
            {
                userLogin()
            }


        }

        //  startActivity(getIntent())
    }
    private fun userLogin() {

        try {
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are checking credential")
            dialog.setCancelable(false)
            dialog.show()

            mServices.UserLogin(
                userName,
                password
            )
                .enqueue(object : Callback<GetLoginData> {
                    override fun onFailure(call: Call<GetLoginData>, t: Throwable) {

                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        val toast = Toast(applicationContext)
                        toast.duration = Toast.LENGTH_LONG

                        val inflater =
                            applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val view: View = inflater.inflate(R.layout.toast1, null)
                        toast.view = view
                        toast.show()
                        dialog.dismiss()


                    }


                    override fun onResponse(
                        call: Call<GetLoginData>,
                        response: Response<GetLoginData>
                    ) {



//                        val result: APIResponse? = response.body()
                        var result = response.body()
                        var parent_name = response.body()!!.parent_name
                        println("response.body()!!.ResponseCode  " + result!!.address)
                        println("result  " + response.body()!!)
                        if (   result!!.pid==null)
                        {

                            val toast = Toast(applicationContext)
                            toast.duration = Toast.LENGTH_LONG

                            val inflater =
                                applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            val view: View = inflater.inflate(R.layout.toast1, null)
                            toast.view = view
                            toast.show()
                            dialog.dismiss()
                        }else
                        {
                            saveData(
                                result.pid,
                                result.parent_name,
                                result.mobno,
                                result.p_email,
                                result.child_name,
                                result.address,
                                result.school,
                                result.class_name,
                                result.enq_date ,
                                result.userrole,
                                result.s_id,
                                result.w_mobno,
                                result.Co_mobno,
                                result.couns_id


                            )

                            dialog.dismiss()







                            val intent = Intent(this@MainActivity, DashboardUser::class.java)
                            startActivity(intent)
                        }




                    }
                })


        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Exception catch 2")
        }
    }




    /* private fun userLogin() {


         try {

             val dialog: android.app.AlertDialog =
                 SpotsDialog.Builder().setContext(this).build()
             dialog.setMessage("Please Wait!!! \nwhile we are checking credential")
             dialog.setCancelable(false)
             dialog.show()

             var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                 PhpApiInterface::class.java
             )
             *//*   var call3: Call<ApiVersion> = phpApiInterface.api_version()*//*

            var call3: Call<APIResponse> =
                phpApiInterface.UserLogin("Login", userName, userName, password)

            call3.enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {

                    // Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    println(" t.message  "+ t.message)


                    val toast = Toast(applicationContext)
                    toast.duration = Toast.LENGTH_LONG

                    val inflater =
                        applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view: View = inflater.inflate(R.layout.toast1, null)
                    toast.view = view
                    toast.show()
                    dialog.dismiss()

                    //  Toast.makeText(applicationContext,"Please enter correct credential", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    try {
                        val result: APIResponse? = response.body()
                        var size = response.body()!!.data!!.size
                        println("size arra " + size)

                        val intent = Intent(this@MainActivity, DashboardUser::class.java)
                        println(" course >>> " + result!!.data!![0].ID)
                        saveData(
                            result!!.data!![0].ID,
                            result!!.data!![0].Parent_NAME,
                            result!!.data!![0].Parent_Mobile,
                            result!!.data!![0].Parent_EMAIL,
                            result!!.data!![0].Child_NAME,
                            result!!.data!![0].Address1,
                            result!!.data!![0].School_NAME,
                            result!!.data!![0].School_CLASS,
                            result!!.data!![0].Date ,
                            result!!.data!![0].User_Role

                        )
                        dialog.dismiss()

                        startActivity(intent)


                    } catch (ex: Exception) {
                        //    ex.printStackTrace()
                    }
                }
            })


        } catch (ex: Exception) {
            //  ex.printStackTrace()

        }
    }
*/

    private fun getVersion() {


        try {

            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                PhpApiInterface::class.java
            )
            /*   var call3: Call<ApiVersion> = phpApiInterface.api_version()*/

            var call3: Call<APIResponse> = phpApiInterface.GetAndroidVersion()

            call3.enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    try {
                        val result: APIResponse? = response.body()
                        println("result  " + result)

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            })


        } catch (ex: Exception) {
            ex.printStackTrace()

        }
    }




    private fun saveData(
        ID: String,
        Parent_NAME: String,
        Parent_Mobile: String,
        Parent_EMAIL: String,
        Child_NAME: String,
        Address: String,
        School_NAME: String,
        School_CLASS: String,
        Date: String,
        UserRole: String,
        Sid:String,
        Whatsapp_mno:String,
        Co_mobile:String,
        couns_id:String
    ) {

        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = mypref.edit()


        editor.putString("PID", ID)
        editor.putString("Parent_NAME", Parent_NAME)
        editor.putString("Parent_Mobile", Parent_Mobile)
        editor.putString("Parent_EMAIL", Parent_EMAIL)
        editor.putString("Child_NAME", Child_NAME)
        editor.putString("Address", Address)
        editor.putString("School_NAME", School_NAME)

        editor.putString("School_CLASS", School_CLASS)
        editor.putString("RegistrationDate", Date)
        editor.putString("UserRole", UserRole)
        editor.putString("SID", Sid)
        editor.putString("Whatsapp_mno", Whatsapp_mno)
        editor.putString("Co_mobile", Co_mobile)
        editor.putString("couns_id", couns_id)



        editor.apply()
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBackPressed() {
        exitDialog()
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun exitDialog() {
        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_exit, null)
        dialogBuilder.setView(dialogView)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->

                finishAffinity()
                exitProcess(0)
            }
            // negative button text and action
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box

        // show alert dialog
        alert.show()
    }


}
