package com.kmeoung.sms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kmeoung.sms.base.BaseActivity
import com.truevalue.dreamappeal.http.BaseHttpCallback
import com.truevalue.dreamappeal.http.BaseHttpParams
import com.truevalue.dreamappeal.http.BaseOkhttpClient
import com.truevalue.dreamappeal.http.HttpType
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject

class ActivityLogin : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Comm_Prefs.init(this@ActivityLogin)

        onClickView()
    }

    private fun onClickView() {
        val listener = View.OnClickListener {
            when (it) {
                btn_login -> {
                   login()
                }
                tv_register -> {
                    val intent = Intent(this@ActivityLogin, ActivityRegister::class.java)
                    startActivity(intent)
                }
            }
        }
        btn_login.setOnClickListener(listener)
        tv_register.setOnClickListener(listener)
    }

    private fun login(){
        val id = et_id.text.toString()
        val password = et_password.text.toString()

        if(id.isNullOrEmpty() || password.isNullOrEmpty()){
            Toast.makeText(this@ActivityLogin,"아이디 / 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        val params = BaseHttpParams()
        params.put("id",id)
        params.put("password",password)

        BaseOkhttpClient.request(
            HttpType.POST,
            Comm_Params.URL_TOKEN,
            null,
            params,
            object : BaseHttpCallback{
                override fun onResponse(call: Call, serverCode: Int, body: String, code: String) {
                    try{
                        val json = JSONObject(body)
                        val token = json.getString("token")
                        Comm_Prefs.setToken(token)
                        Toast.makeText(this@ActivityLogin,"로그인이 성공했습니다.",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ActivityLogin, ActivityMain::class.java)
                        startActivity(intent)
                        finish()
                    }catch (e : JSONException){
                        Toast.makeText(this@ActivityLogin,"아이디 / 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}