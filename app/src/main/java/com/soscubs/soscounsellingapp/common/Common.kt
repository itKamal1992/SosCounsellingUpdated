package com.soscubs.soscounsellingapp.common

import com.soscubs.soscounsellingapp.remote.IMyAPI
import com.soscubs.soscounsellingapp.remote.RetrofitClient

class Common {
    companion object {
        var BASE_URL: String = "https://smartschoolbustracking.com/SOSCounselling/api/"

        fun getAPI(): IMyAPI {
            return RetrofitClient.getClient(BASE_URL)!!.create(IMyAPI::class.java)
        }
    }
}