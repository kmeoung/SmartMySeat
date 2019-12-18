package com.kmeoung.sms

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kmeoung.sms.base.BaseActivity
import com.truevalue.dreamappeal.http.BaseHttpCallback
import com.truevalue.dreamappeal.http.BaseHttpParams
import com.truevalue.dreamappeal.http.BaseOkhttpClient
import com.truevalue.dreamappeal.http.HttpType
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.Call

class ActivityRegister : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        onClickView()
    }

    private fun onClickView() {
        val listener = View.OnClickListener {
            when (it) {
                btn_register -> {
                    register()
                }
            }
        }
        btn_register.setOnClickListener(listener)
    }

    private fun register(){
        val id = et_id.text.toString()
        val password = et_password.text.toString()
        val rePassword = et_re_password.text.toString()
        val name = et_name.text.toString()

        if(id.isNullOrEmpty() || password.isNullOrEmpty() || name.isNullOrEmpty()){
            Toast.makeText(this@ActivityRegister,"모든 값을 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }

        if(password != rePassword){
            Toast.makeText(this@ActivityRegister,"비밀번호가 서로 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return
        }
        val params = BaseHttpParams()
        params.put("id",id)
        params.put("password",password)
        params.put("name",name)
        BaseOkhttpClient.request(
            HttpType.POST,
            Comm_Params.URL_SIGN_UP,
            null,
            params,
            object : BaseHttpCallback{
                override fun onResponse(call: Call, serverCode: Int, body: String, code : String) {

                    if(code == BaseOkhttpClient.DAOK){
                        Toast.makeText(this@ActivityRegister,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()

                        finish()
                    }else{
                        Toast.makeText(this@ActivityRegister,"회원가입이 실패하였습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}