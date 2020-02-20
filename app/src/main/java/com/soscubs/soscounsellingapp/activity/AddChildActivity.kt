package com.soscubs.soscounsellingapp.activity

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
import com.soscubs.soscounsellingapp.Generic.GenericPublicVariable
import com.soscubs.soscounsellingapp.Generic.GenericUserFunction
import com.soscubs.soscounsellingapp.Generic.InternetConnection
import com.soscubs.soscounsellingapp.R
import com.soscubs.soscounsellingapp.common.Common
import com.soscubs.soscounsellingapp.dashboard.DashboardUser
import com.soscubs.soscounsellingapp.model.APIResponse
import com.soscubs.soscounsellingapp.model.GetLoginData
import com.soscubs.soscounsellingapp.model.SchoolDataField
import com.soscubs.soscounsellingapp.remote.IMyAPI
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_new_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddChildActivity : AppCompatActivity() {



    lateinit var et_ChildName: EditText
    lateinit var spinnerSchool: Spinner
    lateinit var spinnerClass: Spinner
    lateinit var checkBoxNumber: CheckBox

    var listsinstz: Int = 0
//    var parentName: String = ""
//    var parentMobile: String = ""
//    var parentWhatsAppMobile: String = ""
//    var parentEmail: String = ""
//    var password: String = ""
    var childName: String = ""
//    var residentialAddress: String = ""
//    var schoolName: String = ""
    var class_name: String = ""

//    var Flag_Email: String = ""

    lateinit var buttonRegister: Button
    var numeric: Boolean = true

    var schoolNameArray = ArrayList<String>()
    var schoolIdArray = ArrayList<String>()
    var schoolClassArray = ArrayList<String>()
    private lateinit var mServices: IMyAPI


    var schoolSid: String = ""
    var cal = Calendar.getInstance()
    var current_date: String = "-"


    var PID: String = ""
    var S_ID: String = ""
    var parentName: String = ""
    var schoolName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_child_activity_layout)
        mServices = Common.getAPI()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        current_date = sdf.format(cal.time)

        et_ChildName = findViewById(R.id.et_childName)
        spinnerSchool = findViewById(R.id.spinner_School)
        spinnerClass = findViewById(R.id.spinner_class)


        PID = intent.getStringExtra("PID")
        S_ID = intent.getStringExtra("S_ID")
        parentName = intent.getStringExtra("parentName")
        schoolName = intent.getStringExtra("schoolName")

        schoolNameArray.add(schoolName)
        var schoolAdap: ArrayAdapter<String> = ArrayAdapter<String>(
            this@AddChildActivity,
            R.layout.support_simple_spinner_dropdown_item, schoolNameArray
        )
        spinnerSchool!!.adapter = schoolAdap
        schoolIdArray.add("000")
        schoolClassArray.add("Select class")
        GetClassDetails(S_ID)

        /*   checkBoxNumber.setOnCheckedChangeListener { buttonView, isChecked ->
               Toast.makeText(this,isChecked.toString(),Toast.LENGTH_SHORT).show()
           }*/



        buttonRegister = findViewById(R.id.btn_register)




        buttonRegister.setOnClickListener {


//            parentName = et_ParentName.text.toString()
//            parentMobile = et_ParentMobile.text.toString()
//            parentWhatsAppMobile = et_ParentWhatsAppMobile.text.toString()
//            parentEmail = et_ParentEmail.text.toString()
//            password = et_Password.text.toString()
//            residentialAddress = et_ResidentialAddress.text.toString()

            childName = et_ChildName.text.toString()
            schoolName = spinnerSchool.selectedItem.toString()
            class_name = spinnerClass.selectedItem.toString()

            var parentNameStatus = ""


            println("parentNameStatus " + parentNameStatus.toBoolean())




           if (et_ChildName.text.isEmpty()) {
                et_childName.setError("Please enter child name")
            } else if (schoolName.equals("Select School")) {
                Toast.makeText(applicationContext, "Select School", Toast.LENGTH_SHORT).show()
            } else if (class_name.equals("Select class",ignoreCase = true)) {
                Toast.makeText(applicationContext, "Select class", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(
                    PID.toInt(),
                    ""+childName,
                    ""+class_name,
                    ""+current_date
                )

//                println(
//                    "Submit data " + PID +  S_ID +
//                            childName + schoolName + class_name+current_date
//                )

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
//                if (idschool == 0) {
//
//                } else {
//                    GetClassDetails(S_ID)
//                }

            }

        }

        var SchoolClassAdap: ArrayAdapter<String> = ArrayAdapter<String>(
            this@AddChildActivity,
            R.layout.support_simple_spinner_dropdown_item, schoolClassArray
        )
        spinnerClass!!.adapter = SchoolClassAdap


    }



    private fun registerUser(parentID:Int,childName:String,class_name:String,current_date:String) {
        if (InternetConnection.checkConnection(this)) {
            if(!isFinishing){
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are Registering ")
            dialog.setCancelable(false)
            dialog.show()
                try {
                    mServices.AddChild(
                    parentID,
                    ""+class_name,
                    ""+childName
                )
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            if(!isFinishing){
                            dialog.dismiss()
                            GenericUserFunction.showNegativePopUp(
                                this@AddChildActivity,
                                "Sorry for inconvenience\nRequest getting failed due to server issue,\nPlease try after some time."
                            )}

                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            if(!isFinishing){
                            dialog.dismiss()
                            try {
//                        val result: APIResponse? = response.body()

                                var result : APIResponse?= response.body()
                                var responsecode = response.body()!!.Responsecode
                                println("response.body()!!.Responsecode  " + response.body()!!.ResponseCode)
                                println("result  " + response.body()!!.response)
                                if (responsecode == 200) {

                                    val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                                    val editor = mypref.edit()
                                    editor.putString("Child_NAME1", childName)
                                    editor.putString("School_CLASS1", class_name)
                                    editor.apply()


                                    GenericPublicVariable.CustDialog = Dialog(this@AddChildActivity)
                                    GenericPublicVariable.CustDialog.setContentView(R.layout.success_dialog)
                                    var ivPosClose1: ImageView =
                                        GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
                                    var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                                    var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                                    tvMsg.text = "Congratulation!!!\n 2nd child registered successfully."
                                    GenericPublicVariable.CustDialog.setCancelable(false)
                                    btnOk.setOnClickListener {
                                        GenericPublicVariable.CustDialog.dismiss()
                                        val intent = Intent(this@AddChildActivity, DashboardUser::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                    }
                                    ivPosClose1.setOnClickListener {
                                        GenericPublicVariable.CustDialog.dismiss()
                                    }

                                    GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                    GenericPublicVariable.CustDialog.show()


                                    println("Success")
                                }else
                                    if (responsecode == 201) {
                                        if(!isFinishing){
                                            GenericUserFunction.showApiError(
                                                this@AddChildActivity,
                                                "Sorry for inconvenience, \nHowever you have already two added children"
                                            )}

                                }else {
                                    if(!isFinishing){
                                    GenericUserFunction.showApiError(
                                        this@AddChildActivity,
                                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                    )}
                                }


                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                if(!isFinishing){
                                    dialog.dismiss()
                                    ex.printStackTrace()
                                    GenericUserFunction.showApiError(
                                        this@AddChildActivity,
                                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                    )}
                            }
                            }
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
        }} else {
            if(!isFinishing){
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
            }
        }
    }

    private fun signIn(usernameLogin: String, password: String) {
        if (InternetConnection.checkConnection(this)) {
        try {if(!isFinishing) {

            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are Login In")
            dialog.setCancelable(false)
            dialog.show()

            mServices.UserLogin(usernameLogin, password)
                .enqueue(object : Callback<GetLoginData> {
                    override fun onFailure(call: Call<GetLoginData>, t: Throwable) {
                        if (!isFinishing) {
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

        }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            println("Exception catch 2")
        }}
        else {
            if(!isFinishing){
                GenericUserFunction.showInternetNegativePopUp(
                    this,
                    getString(R.string.failureNoInternetErr)
                )}
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
        val intent = Intent(this@AddChildActivity, MainActivity::class.java)
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
                if(!isFinishing) {
                    dialog.dismiss()

                    val intent = Intent(this@AddChildActivity, DashboardUser::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }
        })
        if(!isFinishing) {
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    Color.TRANSPARENT
                )
            )

            dialog.show()
        }
    }


//    private fun GetSchoolName(schoolAdap: ArrayAdapter<String>) {
//        if (InternetConnection.checkConnection(this)) {
//            if(!isFinishing) {
//            val dialog: android.app.AlertDialog =
//                SpotsDialog.Builder().setContext(this).build()
//            dialog.setMessage("Please Wait!!! \nwhile we are getting School Info")
//            dialog.setCancelable(false)
//            dialog.show()
//            try {
//                mServices.GetSchoolData().enqueue(object : Callback<ArrayList<SchoolDataField>> {
//                    override fun onFailure(call: Call<ArrayList<SchoolDataField>>, t: Throwable) {
//                        if(!isFinishing) {
//                            dialog.dismiss()
//                            GenericUserFunction.showNegativePopUp(
//                                this@AddChildActivity,
//                                t.message.toString()
//                            )
//                            println("failure" + t)
//                        }
//                    }
//
//
//                    override fun onResponse(
//                        call: Call<ArrayList<SchoolDataField>>,
//                        response: Response<ArrayList<SchoolDataField>>
//                    ) {
//                        if (!isFinishing) {
//                            dialog.dismiss()
//                            println("Success")
//
//                            val result: ArrayList<SchoolDataField>? = response.body()
//
//                            listsinstz = result!!.size
//
//                            println("print array " + result.toString())
//                            if (listsinstz > 0) {
//                                schoolNameArray.clear()
//                                schoolNameArray.add(0, "Select School")
//                                for (i in 0..listsinstz - 1) {
//
//                                    schoolNameArray.add(result!![i].SCHOOL)
//                                    schoolIdArray.add(result!![i].S_ID)
//
//                                }
//                                schoolAdap.notifyDataSetChanged()
//                                println("response.body() " + schoolNameArray)
//
//
////                        println("result!!.SCHOOL[1]  "+result!!.SCHOOL[0])
//                            } else {
//                                GenericUserFunction.showOopsError(
//                                    this@AddChildActivity,
//                                    "No Schools Found"
//                                )
//                            }
//
//                        }
//                    }
//                })
//            } catch (ex: Exception) {
//                if(!isFinishing) {
//                dialog.dismiss()
//                ex.printStackTrace()
//                GenericUserFunction.showApiError(
//                    this,
//                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
//                )}
//            }
//
//        }
//        }
//        else {
//            GenericUserFunction.showInternetNegativePopUp(
//                this,
//                getString(R.string.failureNoInternetErr)
//            )
//        }
//
//
//    }


    private fun GetClassDetails(idschool: String) {
        if (InternetConnection.checkConnection(this)) {
            if(!isFinishing) {
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
                                if(!isFinishing) {
                                    dialog.dismiss()
                                    GenericUserFunction.showNegativePopUp(
                                        this@AddChildActivity,
                                        t.message.toString()
                                    )
                                }
                            }

                            override fun onResponse(
                                call: Call<ArrayList<SchoolDataField>>,
                                response: Response<ArrayList<SchoolDataField>>
                            )  {
                                if(!isFinishing){

                                dialog.dismiss()
//                        var name= response.body()!!.VersionName
//                        var code= response.body()!!.VersionCode
                                val result: ArrayList<SchoolDataField>? = response.body()
                                listsinstz = result!!.size

                                println("print array " + result.toString())

                                if (listsinstz > 0) {
                                    schoolClassArray.clear()
                                    schoolClassArray.add(0, "Select class")
                                    for (i in 0..listsinstz - 1) {

                                        schoolClassArray.add(result!![i].CLASS_NAME)

                                    }
                                }

                            }}

                        })

                } catch (ex: Exception) {
                    if(!isFinishing){
                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )}
                }
            }
        } else {
            if(!isFinishing){
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
            }
        }

    }
}
