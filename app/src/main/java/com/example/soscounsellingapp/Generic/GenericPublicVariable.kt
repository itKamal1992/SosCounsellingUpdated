package com.example.soscounsellingapp.Generic

import android.Manifest
import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Typeface
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.soscounsellingapp.common.Common

class GenericPublicVariable {


    companion object {


        //Array
        var permission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CAMERA
        )

        //ArrayList
        var remainingPermissions: ArrayList<String> = ArrayList<String>()

        //Connection Variable
        var mServices = Common.getAPI()

        //font
        lateinit var myCustomFont: Typeface
        lateinit var myCustomFont2: Typeface

        //AlertDialog
        lateinit var CustDialog: Dialog

        //Shared Preferences var
        var mypref: SharedPreferences? = null
        var ID:String? = null
        var Parent_NAME:String? = null
        var Parent_Mobile:String? = null
        var Parent_EMAIL:String? = null
        var Child_NAME:String? = null
        var Address:String? = null
        var School_NAME:String? = null
        var School_CLASS:String? = null
        var Date:String? = null
        var UserRole:String? = null

        //RegActivity
        var editMobOtp: EditText? = null
        var btnGenOtp: Button? = null
        var progressbarlogin: ProgressBar? = null

        //Common Course ID
        var common_Course_ID:String = "C000000"
        //Common Department ID
        var common_Department_ID:String = "D000000"










    }
}