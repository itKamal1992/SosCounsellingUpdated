package com.soscubs.soscounsellingapp.ApiCustomRequest

class SosPostsRequest(
    PID: String,
    TO_DATE: String,
    FROM_DATE: String

) {
    private var PID: String
    private var TO_DATE: String
    private var FROM_DATE: String

    init {
        this.PID = PID
        this.TO_DATE = TO_DATE
        this.FROM_DATE = FROM_DATE

    }
}