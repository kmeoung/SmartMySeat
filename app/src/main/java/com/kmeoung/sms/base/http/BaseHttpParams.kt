package com.truevalue.dreamappeal.http

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class BaseHttpParams {

    companion object {
        private val JSON = "application/json; charset=utf-8".toMediaType()
        fun toRequestType(json : JSONObject) : RequestBody{
            return json.toString().toRequestBody(JSON)
        }
    }

    private val map: LinkedHashMap<String, Any>?
    private var mJson : JSONObject?

    init {
        map = LinkedHashMap()
        mJson = null
    }

    fun put(jsonObject : JSONObject){
        mJson = jsonObject
    }

    fun put(key: Any, value: Any) {
        map!![key.toString()] = value
    }

    fun urlParams(): String {
        var urlParam: String = ""
        if (map!!.size > 0) {
            var i = 0
            for (key: String in map.keys) {
                urlParam = when (i) {
                    0 ->{
                        i++
                        urlParam + "?" + key + "=" + map[key]
                    }
                    else -> {
                        urlParam + "&" + key + "=" + map[key]
                    }
                }
            }
        }
        return urlParam
    }

    fun bodyParams(): RequestBody {
        if(mJson != null) return toRequestType(mJson!!)
        val json = JSONObject()
        if (map!!.size > 0) {
            for (key: String in map.keys) {
                json.put(key, map[key])
            }
        }
        return toRequestType(json)
    }

}