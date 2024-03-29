package com.truevalue.dreamappeal.http

import okhttp3.Call
import org.json.JSONException
import java.io.IOException

interface BaseHttpCallback {

    fun onFailure(call: Call, e: IOException){
        e.printStackTrace()
    }

    @Throws(IOException::class, JSONException::class)
    fun onResponse(
        call: Call,
        serverCode: Int,
        body: String,
        code : String
    )
}