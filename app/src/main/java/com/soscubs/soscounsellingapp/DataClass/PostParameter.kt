package com.soscubs.soscounsellingapp.DataClass

import org.json.JSONArray
import java.util.ArrayList

data class PostParameter(
    val ID: String,
    val PID: String,
    val COUNS_ID: String,
    val S_ID: String,
    val POST_TITLE: String,
    val POST_DESCRIPTION: String,
    val POST_DATE_GEN: String,
    val SCHOOL_NAME: String,
    val POST_URL: String,
    val ATTACH_TYPE: String,
    val SENDER_ID : String,
    val SENDER_NAME : String,
    val SENDER : String,
    val LIKES: Int,
    val ParentName : String,
    val COMMENTS:ArrayList<CommentParameter>,
    val LIKES_STATUS_PAR: String,
    val LIKES_STATUS_COUS: String,
    val comment_count: String,
    val commentAgainst:ArrayList<String>


)