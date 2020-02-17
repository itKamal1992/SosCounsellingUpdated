package com.soscubs.soscounsellingapp.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable
import com.soscubs.soscounsellingapp.Generic.GenericUserFunction
import com.soscubs.soscounsellingapp.Generic.InternetConnection
import com.soscubs.soscounsellingapp.R
import com.soscubs.soscounsellingapp.common.Common
import com.soscubs.soscounsellingapp.dashboard.DashboardUser
import com.soscubs.soscounsellingapp.model.GetLoginData
import com.soscubs.soscounsellingapp.remote.IMyAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Double.parseDouble
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private var uniqueTokenId: String = ""

    private lateinit var mServices: IMyAPI


    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText


    private var userName = ""
    private var password = ""
    var mobile: String = ""

    var usernameDataType = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mServices = Common.getAPI()
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = mypref.edit()
        btn_newRegistration.setOnClickListener {
            val intent = Intent(this@MainActivity, NewRegistration::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result!!.token
                //saveToken(token)
                uniqueTokenId = token
                editor.putString("TOKEN_ID", uniqueTokenId)
                editor.apply()
                println("Token :--> $token")
                //textView.setText("Token : "+token);*/
            } else {

            }
        }

        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)

        etUsername.addTextChangedListener(object : TextWatcher {
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
                    if (s!!.length > 10) {
//                       Toast.makeText(this@MainActivity,"You have reached ",Toast.LENGTH_SHORT).show()
                        etUsername.setText(s.subSequence(0, 10))
                        etUsername.setSelection(etUsername.text.toString().length)
                    }
                    usernameDataType = "number"
                } catch (e: NumberFormatException) {
                    println("input is  string >>> $count")

                    usernameDataType = "email"

                }
            }

        })

        txt_forget_pass.setOnClickListener {
            val intent = Intent(this, ForgotPassActivity::class.java)
            startActivity(intent)
        }


        btn_Login.setOnClickListener {

            userName = etUsername.text.toString().toLowerCase()
            password = etPassword.text.toString()

            if (userName.equals("")) {
                etUsername.requestFocus()
                etUsername.setError("Please enter valid Email or Mobile No")
                return@setOnClickListener
            }

            if (usernameDataType == "number" && userName.length < 10) {
                etUsername.requestFocus()
                etUsername.setError("Please enter valid Mobile No")
                return@setOnClickListener
            }

            if (usernameDataType == "email" && !CheckEmail(userName)) {
                etUsername.requestFocus()
                etUsername.setError("Please enter valid Email")
                return@setOnClickListener
            } else if (password.length < 8) {
                etPassword.requestFocus()
                etPassword.error = "Please enter valid password"
                return@setOnClickListener
            } else {
                userLogin()
            }
        }
        //  startActivity(getIntent())
    }

    private fun userLogin() {
        if (InternetConnection.checkConnection(this)) {
            try {
                val dialog: android.app.AlertDialog =
                    SpotsDialog.Builder().setContext(this).build()
                dialog.setMessage("Please Wait!!! \n while we are checking credential")
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
                            if (!isFinishing) {
                                val inflater =
                                    applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                val view: View = inflater.inflate(R.layout.toast1, null)
                                toast.view = view
                                toast.show()
                                dialog.dismiss()
                            }
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
                            if (result!!.pid == null) {
                                val toast = Toast(applicationContext)
                                toast.duration = Toast.LENGTH_LONG

                                val inflater =
                                    applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                val view: View = inflater.inflate(R.layout.toast1, null)
                                toast.view = view
                                toast.show()
                                if (!isFinishing) {
                                    dialog.dismiss()
                                }
                            } else {
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

                                if (!isFinishing) {
                                    dialog.dismiss()
                                }
                                val intent = Intent(this@MainActivity, DashboardUser::class.java)
                                startActivity(intent)
                            }
                        }
                    })
            } catch (ex: Exception) {
                ex.printStackTrace()
                println("Exception catch 2")
            }
        } else {
            if (!isFinishing) {
                GenericUserFunction.showInternetNegativePopUp(
                    this,
                    getString(R.string.failureNoInternetErr)
                )
            }
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
        if (!isFinishing) {
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
                if (!isFinishing) {
                    GenericPublicVariable.CustDialog.dismiss()
                    finishAffinity()
                    exitProcess(0)
                }

            }
            btnNo.setOnClickListener {
                if (!isFinishing) {
                    GenericPublicVariable.CustDialog.dismiss()
                }

            }
//        ivNegClose1.setOnClickListener {
//            GenericPublicVariable.CustDialog.dismiss()
////            callDashboard()
//        }
            if (!isFinishing) {
                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
                    ColorDrawable(
                        Color.TRANSPARENT
                    )
                )
                GenericPublicVariable.CustDialog.show()
            }
            //////////End//////////////
        }
    }

    fun CheckEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        println("check email  " + return Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

}
