package com.kmeoung.sms

object Comm_Params {
    val APP_NAME = "SmartMySeat"
    val URL = "http://ec2-15-164-118-112.ap-northeast-2.compute.amazonaws.com:8080"
    val URL_MEMBERS = "$URL/members" // GET uuid 조회
    val URL_SIGN_UP = "$URL_MEMBERS/signup"
    val URL_TOKEN = "$URL/tokens"
    // GET
    val CLASS_IDX = "CLASS_IDX"
    val URL_TABLES_CLASS_IDX = "$URL/tables/class/$CLASS_IDX"
    val TABLE_IDX = "TABLE_IDX"
    // 예약 / 예약 취소 POST / DELETE
    val URL_TABLES_CLASS_IDX_TABLE_IDX = "$URL_TABLES_CLASS_IDX/table/$TABLE_IDX"


}