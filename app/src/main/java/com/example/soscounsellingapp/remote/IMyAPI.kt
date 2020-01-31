package com.example.soscounsellingapp.remote

import com.example.soscounsellingapp.ApiCustomRequest.SosPostsRequest
import com.example.soscounsellingapp.DataClass.PostParameter
import com.example.soscounsellingapp.model.APIResponse
import com.example.soscounsellingapp.model.GetLoginData
import com.example.soscounsellingapp.model.SchoolDataField
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface IMyAPI {

    @GET("School/PostSchool")
    fun GetSchoolData(): Call<ArrayList<SchoolDataField>>



    @FormUrlEncoded
    @POST("Class/GetClass")
    fun GetClassData(@Field("S_ID")SID:String):Call<ArrayList<SchoolDataField>>

    @FormUrlEncoded
    @POST("Registration/PostRegistration")
    fun RegisterUser(
        @Field("SCH_UID") SCH_UID:String ,
        @Field("ADDRESS")  ADDRESS:String,
        @Field("PARENT_NAME") PARENT_NAME:String,
        @Field("MOBNO") MOBNO:String,
        @Field("P_W_MOBNO") P_W_MOBNO:String,
        @Field("CHILD_NAME") CHILD_NAME:String,
        @Field("ENQ_DATE") ENQ_DATE:String,
        @Field("P_P_EMAIL") P_P_EMAIL:String,
        @Field("P_CLASS_NAME") P_CLASS_NAME:String,
        @Field("P_Password") P_Password:String
    ):Call<APIResponse>


    @FormUrlEncoded
    @POST("Login/PostLogin")
    fun UserLogin(
        @Field("Username") SCH_UID:String ,
        @Field("Password")  ADDRESS:String
    ):Call<GetLoginData>

//    @FormUrlEncoded
//    @GET("Post/GetMessage")
//    fun getPostsData(
//        @Body sosPostsRequest:SosPostsRequest)
//    ):Call<ArrayList<PostParameter>>


    @GET("Post/GetMessage")
    fun getPostsData(
        @QueryMap  options:Map<String, String>)
    :Call<ArrayList<PostParameter>>

    @GET("Like/CommentLike")
    fun getLikePosts(
        @QueryMap  options:Map<String, String>)
            :Call<APIResponse>
}