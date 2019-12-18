package com.kmeoung.sms

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kmeoung.sms.base.BaseActivity
import com.truevalue.dreamappeal.http.BaseHttpCallback
import com.truevalue.dreamappeal.http.BaseOkhttpClient
import com.truevalue.dreamappeal.http.HttpType
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import org.json.JSONObject
import java.nio.charset.Charset


class ActivityMain : BaseActivity() {

    private val TYPE_TEXT = 0
    private val TYPE_URI = 1
    private lateinit var mNfcAdapter : NfcAdapter
    var mPendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getuuid()
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

        btn_reserve.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,ActivityReserve::class.java)
            startActivity(intent)
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent == null)
            return

        val detectedTag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        writeTag(getTextAsNdef(),detectedTag)

    }

    fun writeTag(message : NdefMessage,tag : Tag){
        val size = message.toByteArray().size

        try{
            val ndef = Ndef.get(tag)

            if(ndef != null){
                ndef.connect()

                if(!ndef.isWritable){
                    Toast.makeText(this@ActivityMain, "can not write NFC tag",
                        Toast.LENGTH_SHORT).show()
                }

                if(ndef.maxSize < size){
                    Toast.makeText(this@ActivityMain, "NFC tag size too large",
                        Toast.LENGTH_SHORT).show()
                }
                ndef.writeNdefMessage(message)

                Toast.makeText(this@ActivityMain, "NFC tag is writted",
                    Toast.LENGTH_SHORT).show()

            }else{
                val format = NdefFormatable.get(tag)

                if (format != null) {
                    try {
                        format.connect()
                        format.format(message)

                        Toast.makeText(this@ActivityMain, "NFC tag is formatting",

                            Toast.LENGTH_SHORT).show()

                    } catch (e : Exception) {
                        e.printStackTrace()
                    }

                } else {

                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun getTextAsNdef() : NdefMessage{
        val textBytes = tv_text.text.toString().toByteArray()
        val textRecord = NdefRecord(NdefRecord.TNF_MIME_MEDIA,

            "text/plain".toByteArray(),
            byteArrayOf(),
            textBytes)
        return NdefMessage(arrayOf(textRecord))

    }

    private fun getUriAsNdef(): NdefMessage {

        val textBytes = tv_text.text.toString().toByteArray()

        val record1 = NdefRecord(
            NdefRecord.TNF_WELL_KNOWN,

            "U".toByteArray(Charset.forName("US-ASCII")),

            ByteArray(0),

            textBytes
        )


        return NdefMessage(arrayOf(record1))
    }

    private fun getuuid(){
        BaseOkhttpClient.request(
            HttpType.GET,
            Comm_Params.URL_MEMBERS,
            BaseOkhttpClient.getHttpHeader(),
            null,
            object : BaseHttpCallback{
                override fun onResponse(call: Call, serverCode: Int, body: String, code: String) {

                    if(code == BaseOkhttpClient.DAOK){
                        val json = JSONObject(body)
                        val member = json.getJSONObject("member")
                        val id = member.getString("id")
                        val uuid = member.getString("uuid")
                        val name = member.getString("name")
                        Comm_Prefs.setUuid(uuid)
                    }
                }
            }
        )
    }

}
