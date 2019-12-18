package com.kmeoung.sms

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.kmeoung.sms.base.BaseActivity


class ActivityIntro : BaseActivity(){

    private val DELAY : Long = 1000

    private val handler = Handler(object : Handler.Callback{
        override fun handleMessage(msg: Message): Boolean {
            val intent = Intent(this@ActivityIntro,ActivityLogin::class.java)
            startActivity(intent)
            finish()
            return false
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }

    override fun onResume() {
        super.onResume()

        handler.sendEmptyMessageDelayed(0,DELAY)
    }

    override fun onPause() {
        super.onPause()

        handler.removeMessages(0)
    }
}