package com.example.soscounsellingapp.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import com.example.soscounsellingapp.Generic.GenericPublicVariable
import com.example.soscounsellingapp.Generic.GenericUserFunction
import com.example.soscounsellingapp.Generic.InternetConnection
import com.example.soscounsellingapp.R
import com.example.soscounsellingapp.common.Common
import com.example.soscounsellingapp.dashboard.DashboardUser
import com.example.soscounsellingapp.model.APIResponse
import com.example.soscounsellingapp.model.GetLoginData
import com.example.soscounsellingapp.model.SchoolDataField
import com.example.soscounsellingapp.remote.IMyAPI
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_new_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class NewRegistration : AppCompatActivity() {


    lateinit var et_ParentName: EditText
    lateinit var et_ParentMobile: EditText
    lateinit var et_ParentWhatsAppMobile: EditText
    lateinit var et_ParentEmail: EditText
    lateinit var et_Password: EditText
    lateinit var et_ChildName: EditText
    lateinit var et_ResidentialAddress: EditText

    lateinit var spinnerSchool: Spinner
    lateinit var spinnerClass: Spinner

    lateinit var checkBoxNumber: CheckBox

    var listsinstz: Int = 0
    var parentName: String = ""
    var parentMobile: String = ""
    var parentWhatsAppMobile: String = ""
    var parentEmail: String = ""
    var password: String = ""
    var childName: String = ""
    var residentialAddress: String = ""
    var schoolName: String = ""
    var class_name: String = ""

    var Flag_Email: String = ""

    lateinit var buttonRegister: Button
    var numeric: Boolean = true

    var schoolNameArray = ArrayList<String>()
    var schoolIdArray = ArrayList<String>()
    var schoolClassArray = ArrayList<String>()
    private lateinit var mServices: IMyAPI


    var schoolSid: String = ""
    var cal = Calendar.getInstance()
    var current_date: String = "-"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_registration)



        mServices = Common.getAPI()

        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        current_date = sdf.format(cal.time)

        et_ParentName = findViewById(R.id.et_parentName)
        et_ParentMobile = findViewById(R.id.et_parentMobile)
        et_ParentEmail = findViewById(R.id.et_parentEmail)
        et_ParentWhatsAppMobile = findViewById(R.id.et_whatsAppNumber)
        et_Password = findViewById(R.id.et_password)
        et_ChildName = findViewById(R.id.et_childName)
        et_ResidentialAddress = findViewById(R.id.et_residentialAddress)

        spinnerSchool = findViewById(R.id.spinner_School)
        spinnerClass = findViewById(R.id.spinner_class)

        checkBoxNumber = findViewById(R.id.cb_whatsAppNo)

        checkBoxNumber.isChecked


        println(" checkBoxNumber.isChecked  " + checkBoxNumber.isChecked)

        var schoolAdap: ArrayAdapter<String> = ArrayAdapter<String>(
            this@NewRegistration,
            R.layout.support_simple_spinner_dropdown_item, schoolNameArray
        )
        spinnerSchool!!.adapter = schoolAdap

        schoolNameArray.add("Select School")
        GetSchoolName(schoolAdap)
        schoolIdArray.add("000")
        schoolClassArray.add("Select class")

        /*   checkBoxNumber.setOnCheckedChangeListener { buttonView, isChecked ->
               Toast.makeText(this,isChecked.toString(),Toast.LENGTH_SHORT).show()
           }*/

        checkBoxNumber.setOnCheckedChangeListener { buttonView, isChecked ->


            if (isChecked.toString().equals("true")) {
                if (et_ParentMobile.text.toString().length < 10) {
                    ChekNumberDialog()

                } else {
                    et_whatsAppNumber.setText(et_ParentMobile.text.toString())
                    et_ParentWhatsAppMobile.isEnabled = false
                }


                /*     et_whatsAppNumber.setText(et_ParentMobile.text.toString())
                     et_ParentWhatsAppMobile.isEnabled=false*/
            } else {
                et_whatsAppNumber.setText("")
                et_ParentWhatsAppMobile.isEnabled = true

            }

        }



        buttonRegister = findViewById(R.id.btn_register)




        buttonRegister.setOnClickListener {


            parentName = et_ParentName.text.toString()
            parentMobile = et_ParentMobile.text.toString()
            parentWhatsAppMobile = et_ParentWhatsAppMobile.text.toString()

            parentEmail = et_ParentEmail.text.toString()
            password = et_Password.text.toString()
            childName = et_ChildName.text.toString()
            residentialAddress = et_ResidentialAddress.text.toString()
            schoolName = spinnerSchool.selectedItem.toString()
            class_name = spinnerClass.selectedItem.toString()

            var parentNameStatus = ""

            if (parentName.isDigitsOnly()) {
                parentNameStatus = "true"
            } else {
                parentNameStatus = "false"

            }
            println("parentNameStatus " + parentNameStatus.toBoolean())


            CheckEmail()
            Flag_Email = CheckEmail().toString()

            println("Flag_Email " + Flag_Email)

            if (et_ParentName.text.isEmpty() && parentNameStatus.toBoolean() == true) {
                et_ParentName.setError("Please enter name")


                println("false here")
            } else if (et_ParentMobile.text.isEmpty() && parentMobile.length < 8) {
                et_parentMobile.setError("Please enter mobile number")
            } else if (parentMobile.length < 10) {
                et_parentMobile.setError("Please enter valid mobile number")
            } else if (et_ParentWhatsAppMobile.text.isEmpty() && parentMobile.length < 8) {
                et_ParentWhatsAppMobile.setError("Please enter mobile number")
            } else if (parentWhatsAppMobile.length < 10) {

                et_whatsAppNumber.setError("Please enter valid mobile number")
            } else if (Flag_Email.equals("false")) {
                et_parentEmail.setError("Please enter valid Email ")
            } else if (et_Password.text.isEmpty()) {
                et_Password.setError("Password ")
            } else if (et_Password.text.toString().length < 8) {
                et_Password.setError("Password length atleast 8 character")
            } else if (et_ChildName.text.isEmpty()) {
                et_childName.setError("Please enter child name")
            } else if (et_ResidentialAddress.text.isEmpty()) {
                et_residentialAddress.setError("Please address")
            } else if (schoolName.equals("Select School")) {
                Toast.makeText(applicationContext, "Select School", Toast.LENGTH_SHORT).show()
            } else if (class_name.equals("Select class")) {
                Toast.makeText(applicationContext, "Select class", Toast.LENGTH_SHORT).show()
            } else {
                registerUser()

                println(
                    "Submit data " + parentName + parentMobile + parentEmail + password +
                            childName + residentialAddress + schoolName + class_name
                )



                println("checkOut " + CheckEmail())

//               RegisterUser


            }


        }




        spinnerSchool.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                var idschool = spinnerSchool.selectedItemPosition
                println("idschool " + idschool)
                println("spinnerSchool >> " + spinnerSchool.selectedItem)
                spinnerSchool.selectedItem
                if (idschool == 0) {

                } else {
                    GetClassDetails(schoolIdArray[idschool])
                    schoolSid = schoolIdArray[idschool]
                }

            }

        }

        var SchoolClassAdap: ArrayAdapter<String> = ArrayAdapter<String>(
            this@NewRegistration,
            R.layout.support_simple_spinner_dropdown_item, schoolClassArray
        )
        spinnerClass!!.adapter = SchoolClassAdap


    }

    fun CheckEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(parentEmail).matches()
        println("check email  " + return Patterns.EMAIL_ADDRESS.matcher(parentEmail).matches())
    }

    fun CheckNumber(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(parentEmail).matches()
        println("check email  " + return Patterns.EMAIL_ADDRESS.matcher(parentEmail).matches())
    }


    private fun registerUser() {
        if (InternetConnection.checkConnection(this)) {
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are Registering ")
            dialog.setCancelable(false)
            dialog.show()


            try {

                mServices.RegisterUser(
                    schoolSid,
                    residentialAddress,
                    parentName,
                    parentMobile,
                    parentWhatsAppMobile,
                    childName,
                    current_date,
                    parentEmail.toLowerCase().replace(" ",""),
                    class_name,
                    password
                )
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            dialog.dismiss()

                            GenericUserFunction.showNegativePopUp(
                                this@NewRegistration,
                                t.message.toString()
                            )

                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            dialog.dismiss()
                            try {
//                        val result: APIResponse? = response.body()

                                var result = response.body()
                                var responseCode = response.body()!!.ResponseCode
                                println("response.body()!!.ResponseCode  " + response.body()!!.ResponseCode)
                                println("result  " + response.body()!!.response)
                                if (responseCode == 200) {

                                    signIn(parentMobile, password)

//                                    GenericPublicVariable.CustDialog = Dialog(this@NewRegistration)
//                                    GenericPublicVariable.CustDialog.setContentView(R.layout.positive_popup)
//                                    var ivPosClose1: ImageView =
//                                        GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
//                                    var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//                                    var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                                    tvMsg.text = "Congratulation you have registered successfully."
//                                    GenericPublicVariable.CustDialog.setCancelable(false)
//                                    btnOk.setOnClickListener {
//                                        GenericPublicVariable.CustDialog.dismiss()
//                                        signIn(parentMobile, password)
//                                    }
//                                    ivPosClose1.setOnClickListener {
//                                        GenericPublicVariable.CustDialog.dismiss()
//                                    }
//
//                                    GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                                    GenericPublicVariable.CustDialog.show()


                                    println("Success")
                                } else {
                                    GenericUserFunction.showApiError(
                                        this@NewRegistration,
                                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                    )

                                    println("Not Registered")
                                }


                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                println("Exception catch 1")
                            }
                        }
                    })


            } catch (ex: Exception) {
                dialog.dismiss()
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }
    }

    private fun signIn(usernameLogin: String, password: String) {
        try {
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are Login In")
            dialog.setCancelable(false)
            dialog.show()

            mServices.UserLogin(usernameLogin,password)
                .enqueue(object : Callback<GetLoginData> {
                    override fun onFailure(call: Call<GetLoginData>, t: Throwable) {
                        if (!isFinishing){
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
//                        val toast = Toast(applicationContext)
//                        toast.duration = Toast.LENGTH_LONG
//
//                        val inflater =
//                            applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                        val view: View = inflater.inflate(R.layout.toast1, null)
//                        toast.view = view
//                        toast.show()
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
                            dialog.dismiss()
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
                            dialog.dismiss()
                            SuccessDialog()
                        }


                    }
                })


        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Exception catch 2")
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

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    /*private fun registerUser() {


        try {





            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                PhpApiInterface::class.java
            )
            *//*   var call3: Call<ApiVersion> = phpApiInterface.api_version()*//*

            var call3: Call<APIResponse> = phpApiInterface.RegisterUser(
                "Register",
                parentName,
                "9555555555",
                parentWhatsAppMobile,
                parentEmail,
                childName,
                residentialAddress,
                schoolName,
                class_name,
                password,
                "Parent"
            )

            call3.enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {

                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()

                }

                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    try {
                        val result: APIResponse? = response.body()
                        println("result  " + response.body()!!.response)
                        SuccessDialog()


                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            })


        } catch (ex: Exception) {
            ex.printStackTrace()

        }
    }*/


    fun callSelf(ctx: Context) {
        val intent = Intent(ctx, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ctx.startActivity(intent)
        startLogin()
    }

    private fun startLogin() {
        val intent = Intent(this@NewRegistration, MainActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun SuccessDialog() {


        // custom dialog
        // custom dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.success_dialog)
        dialog.setTitle("Title...")


        val dialogButton =
            dialog.findViewById<View>(R.id.btnCustomDialogAccept) as Button
        // if button is clicked, close the custom dialog
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()

                val intent = Intent(this@NewRegistration, DashboardUser::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        })

        dialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun ChekNumberDialog() {


        // custom dialog
        // custom dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.checknumber_dialog)
        dialog.setTitle("Title...")


        val dialogButton =
            dialog.findViewById<View>(R.id.btn_okNumber) as Button
        // if button is clicked, close the custom dialog
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                et_whatsAppNumber.setText("")
                et_ParentWhatsAppMobile.isEnabled = true
                dialog.dismiss()
                checkBoxNumber.isChecked = false
            }
        })

        dialog.show()
    }


    private fun GetSchoolName(schoolAdap: ArrayAdapter<String>) {
        if (InternetConnection.checkConnection(this)) {
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are getting School Info")
            dialog.setCancelable(false)
            dialog.show()
            try {
                mServices.GetSchoolData().enqueue(object : Callback<ArrayList<SchoolDataField>> {
                    override fun onFailure(call: Call<ArrayList<SchoolDataField>>, t: Throwable) {
                        dialog.dismiss()
                        GenericUserFunction.showNegativePopUp(
                            this@NewRegistration,
                            t.message.toString()
                        )
                        println("failure" + t)

                    }


                    override fun onResponse(
                        call: Call<ArrayList<SchoolDataField>>,
                        response: Response<ArrayList<SchoolDataField>>
                    ) {
                        dialog.dismiss()
                        println("Success")

                        val result: ArrayList<SchoolDataField>? = response.body()

                        listsinstz = result!!.size

                        println("print array " + result.toString())
                        if (listsinstz > 0) {
                            schoolNameArray.clear()
                            schoolNameArray.add(0, "Select School")
                            for (i in 0..listsinstz - 1) {

                                schoolNameArray.add(result!![i].SCHOOL)
                                schoolIdArray.add(result!![i].S_ID)

                            }
                            schoolAdap.notifyDataSetChanged()
                            println("response.body() " + schoolNameArray)


//                        println("result!!.SCHOOL[1]  "+result!!.SCHOOL[0])
                        } else {
                            GenericUserFunction.showOopsError(
                                this@NewRegistration,
                                "No Schools Found"
                            )
                        }

                    }

                })
            } catch (ex: Exception) {
                dialog.dismiss()
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }

        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }


    }


    private fun GetClassDetails(idschool: String) {
        if (InternetConnection.checkConnection(this)) {
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are getting Class Info")
            dialog.setCancelable(false)
            dialog.show()

            try {

                println("idschool" + idschool)
                mServices.GetClassData(idschool)
                    .enqueue(object : Callback<ArrayList<SchoolDataField>> {
                        override fun onFailure(
                            call: Call<ArrayList<SchoolDataField>>,
                            t: Throwable
                        ) {
                            dialog.dismiss()
                            GenericUserFunction.showNegativePopUp(
                                this@NewRegistration,
                                t.message.toString()
                            )
                        }

                        override fun onResponse(
                            call: Call<ArrayList<SchoolDataField>>,
                            response: Response<ArrayList<SchoolDataField>>
                        ) {
                            dialog.dismiss()
//                        var name= response.body()!!.VersionName
//                        var code= response.body()!!.VersionCode
                            val result: ArrayList<SchoolDataField>? = response.body()
                            listsinstz = result!!.size

                            println("print array " + result.toString())

                            if (listsinstz > 0) {
                                schoolClassArray.clear()
                                schoolClassArray.add(0, "Select Class")
                                for (i in 0..listsinstz - 1) {

                                    schoolClassArray.add(result!![i].CLASS_NAME)

                                }
                            }

                        }

                    })

            } catch (ex: Exception) {
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )

            }

        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }

    }
}
