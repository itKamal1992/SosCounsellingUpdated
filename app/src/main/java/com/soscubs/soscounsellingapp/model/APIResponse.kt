package com.soscubs.soscounsellingapp.model

class APIResponse {
    var Responsecode: Int = 0
    var ResponseCode: Int = 0

    var success: String = ""
    var Status: String = ""
    var Status1: String = ""
    var response: String = ""
    var VersionName: String = ""
    var VersionCode: String = ""

    var NEW_VERSION: String = ""
    var VERSION_NAME: String = ""
    var DESCRIPTION: String = ""
    var message: String = ""
    var Data18: List<GrievanceDataField>? = null
    var data: List<GetLoginData>? = null

    var data1: List<GetLoginData>? = null
}