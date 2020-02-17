package com.soscubs.soscounsellingapp.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable
import com.soscubs.soscounsellingapp.Generic.GenericUserFunction
import com.soscubs.soscounsellingapp.Generic.InternetConnection
import com.soscubs.soscounsellingapp.R
import com.soscubs.soscounsellingapp.common.Common
import com.soscubs.soscounsellingapp.dashboard.DashboardUser
import com.soscubs.soscounsellingapp.model.APIResponse
import com.soscubs.soscounsellingapp.model.GetLoginData
import com.soscubs.soscounsellingapp.remote.ApiClientPhp
import com.soscubs.soscounsellingapp.remote.IMyAPI
import com.soscubs.soscounsellingapp.remote.PhpApiInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Double.parseDouble
import java.util.*
import kotlin.system.exitProcess

class ForgotPassActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    private var TOKEN_ID: String = ""

    private lateinit var mServices: IMyAPI


    lateinit var et_username: EditText
    lateinit var et_password: EditText


    var userName: String = ""
    var password: String = ""

    var email  : String = ""
    var mobile : String = ""

    var usernameDataType:String="email"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
//  GenericUserFunction.showPositivePopUp(this,
//  getString(R.string.failureNoInternetErr)

// GenericUserFunction.showNegativePopUp(this,"Hello")

        mServices = Common.getAPI()

        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = mypref.edit()
//
//        btn_newRegistration.setOnClickListener {
//            val intent = Intent(this@ForgotPassActivity, NewRegistration::class.java)
//            startActivity(intent)
////    getClg()
////    getVersion()
//
//        }

        auth = FirebaseAuth.getInstance()
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result!!.token
                //saveToken(token)
                TOKEN_ID = token
                editor.putString("TOKEN_ID", TOKEN_ID)
                editor.apply()

                println("Token :--> $token")
                //textView.setText("Token : "+token);*/
            } else {

            }
        }

        et_username = findViewById(R.id.et_username)
        et_password = findViewById(R.id.et_password)

        et_username.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
               // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                try {
                    val num = parseDouble(s.toString())
                    println("input is  number >>> $count")
                    if (s!!.length>10){
//                       Toast.makeText(this@MainActivity,"You have reached ",Toast.LENGTH_SHORT).show()
                        et_username.setText(s.subSequence(0,10))
                        et_username.setSelection(et_username.text.toString().length)
                    }
                    usernameDataType="number"
                } catch (e: NumberFormatException) {
                    println("input is  string >>> $count")

                    usernameDataType="email"

                }
            }

        })



        btn_Login.setOnClickListener {

            userName = et_username.text.toString().toLowerCase()
            password = et_password.text.toString()

            if (userName.equals(""))
            {
                et_username.requestFocus()
                et_username.setError("Please enter valid Email or Mobile No")
                return@setOnClickListener
            }

            if (usernameDataType=="number" && userName.length<10){
                et_username.requestFocus()
                et_username.setError("Please enter valid Mobile No")
                return@setOnClickListener
            }

            if (usernameDataType=="email" && !CheckEmail(userName) ){
                et_username.requestFocus()
                et_username.setError("Please enter valid Email")
                return@setOnClickListener
            }


            else if (password.length < 8)
            {
                et_password.requestFocus()
                et_password.error="Please enter valid password"
                return@setOnClickListener
            }
            else
            {
                userLogin()
            }
        }
        //  startActivity(getIntent())
    }

    private fun userLogin()
    {
        if (InternetConnection.checkConnection(this)) {
        try
        {
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \n while we are checking credential")
            dialog.setCancelable(false)
            dialog.show()

            mServices.ForgetPass(
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
                        if (result!!.pid == null)
                        {
                            val toast = Toast(applicationContext)
                            toast.duration = Toast.LENGTH_LONG

                            val inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            val view: View = inflater.inflate(R.layout.toast1, null)
                            toast.view = view
                            toast.show()
                            dialog.dismiss()
                        } else
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
                                result.enq_date,
                                result.userrole,
                                result.s_id,
                                result.w_mobno,
                                result.Co_mobno,
                                result.couns_id
                            )

                            dialog.dismiss()
                            //////////Start///////////////
                            GenericPublicVariable.CustDialog = Dialog(this@ForgotPassActivity)
                            GenericPublicVariable.CustDialog.setContentView(R.layout.custom_dialog_test)
                            var ivNegClose1: ImageView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                            var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//        var btnCancel: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                            var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                            var txtcontact: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.txtcontact) as TextView
//                            txtcontact.visibility=View.GONE
//                            tvMsg.text = "Congratulation!!! Your password han been reset successfully."
//        GenericPublicVariable.CustDialog.setCancelable(false)
                            btnOk.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callDashboard()
                            }
//        btnCancel.setOnClickListener {
//            GenericPublicVariable.CustDialog.dismiss()
//
//        }
                            ivNegClose1.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callDashboard()
                            }
                            GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
                                ColorDrawable(
                                    Color.TRANSPARENT)
                            )
                            GenericPublicVariable.CustDialog.show()
                            //////////End//////////////

                        }
                    }
                })
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Exception catch 2")
        }
    }else {
        if(!isFinishing){
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )}
    }
    }

    private fun callDashboard() {
        val intent = Intent(this@ForgotPassActivity, DashboardUser::class.java)
        startActivity(intent)

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
        Sid: String,
        Whatsapp_mno: String,
        Co_mobile: String,
        couns_id: String
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


//    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
//    override fun onBackPressed() {
//        exitDialog()
//    }



    fun CheckEmail(email:String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        println("check email  " + return Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

}
