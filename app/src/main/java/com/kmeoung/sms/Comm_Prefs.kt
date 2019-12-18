package com.kmeoung.sms

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object Comm_Prefs {

    private var mContext: Context? = null
    private var prefs: SharedPreferences?

    init {
        prefs = null
    }

    /**
     * App 실행 시 한번만 실행
     */
    fun init(context: Context) {
        mContext = context
        prefs = mContext!!.getSharedPreferences(Comm_Params.APP_NAME, MODE_PRIVATE)
    }

    fun setToken(token : String){
        prefs!!.edit().putString(Comm_Prefs_Param.PREFS_TOKEN,token).commit()
    }

    fun getToken() : String?{
        return prefs!!.getString(Comm_Prefs_Param.PREFS_TOKEN,null)
    }

    fun setUuid(token : String){
        prefs!!.edit().putString(Comm_Prefs_Param.PREFS_UUID,token).commit()
    }

    fun getUuid() : String?{
        return prefs!!.getString(Comm_Prefs_Param.PREFS_UUID,null)
    }





}