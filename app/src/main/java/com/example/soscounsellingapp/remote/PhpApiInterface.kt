package com.example.soscounsellingapp.remote

import com.example.soscounsellingapp.model.APIResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface PhpApiInterface {
    @GET("dmims/api/apiversion/read_single.php")
    fun GetAndroidVersion(): Call<APIResponse>


    @FormUrlEncoded
    @POST("api_cubs/user_reg.php")
    fun RegisterUser(@Field("REQUEST") REQUEST : String, @Field("Parent_NAME") Parent_NAME: String, @Field("Parent_Mobile") Parent_Mobile: String, @Field("Parent_EMAIL") Parent_EMAIL: String, @Field("Child_NAME") Child_NAME: String, @Field("Address")Address:String, @Field("School_NAME") School_NAME: String, @Field("School_CLASS") School_CLASS: String, @Field("Password") Password: String, @Field("User_Role") UserRole: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("api_cubs/user_reg.php")
    fun UserLogin(@Field("REQUEST") REQUEST : String, @Field("Parent_Mobile") Parent_Mobile: String, @Field("Parent_EMAIL") Parent_EMAIL: String, @Field("Password") Password: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("api_cubs/user_reg.php")
    fun GetAllUsers(@Field("REQUEST") REQUEST : String): Call<APIResponse>

    @FormUrlEncoded
    @POST("api_cubs/fcm_userInsert.php")
    fun InsertToken(
        @Field("Pid") Pid: String, @Field("TOKEN_ID") TOKEN_ID: String,
        @Field("Parent_name") Parent_name: String, @Field("School_name") School_name: String,
        @Field("Sid") Sid: String,@Field("Cid") Cid: String, @Field("Class_name") Class_name: String,
        @Field("Mobile_no") Mobile_no: String, @Field("Parent_email") Parent_email: String,
        @Field("Whatsapp_mno") Whatsapp_mno: String, @Field("User_role") User_role: String,
        @Field("Doe") Doe: String, @Field("STATUS") STATUS: String, @Field("REMARK") REMARK: String): Call<APIResponse>


}