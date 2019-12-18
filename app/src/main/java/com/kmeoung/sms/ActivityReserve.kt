package com.kmeoung.sms

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.kmeoung.sms.base.BaseActivity
import com.kmeoung.sms.base.BaseRecyclerViewAdapter2
import com.kmeoung.sms.base.BaseViewHolder
import com.kmeoung.sms.base.IORecyclerViewListener
import com.truevalue.dreamappeal.http.BaseHttpCallback
import com.truevalue.dreamappeal.http.BaseOkhttpClient
import com.truevalue.dreamappeal.http.HttpType
import kotlinx.android.synthetic.main.activity_reserve.*
import okhttp3.Call
import org.json.JSONObject


class ActivityReserve : BaseActivity() {

    val TYPE_CLASS_1 = 1
    val TYPE_CLASS_2 = 2

    var mClassType = 1

    var mAdapter: BaseRecyclerViewAdapter2<BeanReserve>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)

        initAdapter()

        getReserve()

        tv_class_1.setOnClickListener(View.OnClickListener { mClassType = TYPE_CLASS_1
        getReserve()})

        tv_class_2.setOnClickListener(View.OnClickListener { mClassType = TYPE_CLASS_2
            getReserve()})
    }

    private fun initAdapter() {
        mAdapter = BaseRecyclerViewAdapter2(object : IORecyclerViewListener {
            override val itemCount: Int
                get() = mAdapter!!.size()

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return BaseViewHolder.newInstance(R.layout.listitem_reserve, parent, false)
            }

            override fun onBindViewHolder(h: BaseViewHolder, i: Int) {
                val bean = mAdapter!!.get(i)
                val item = h.getItemView<TextView>(R.id.tv_item)

                when(bean.type_idx){
                    1->{
                        item.setBackgroundColor(resources.getColor(R.color.color_10))
                    }
                    2->{
                        item.setBackgroundColor(resources.getColor(R.color.color_11))
                    }
                    3->{
                        item.setBackgroundColor(resources.getColor(R.color.black))
                    }
                }
                if(bean.name.isNullOrEmpty()){
                    item.text = "${bean.type_name}"
                }else {
                    item.text = "${bean.type_name}\n${bean.name}"
                }

                item.setOnClickListener(View.OnClickListener {
                    val builder = AlertDialog.Builder(this@ActivityReserve)

                    builder.setTitle("예약 / 취소").setMessage("예약 / 취소 선택")
                        .setPositiveButton("예약",object : DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                setReserve(bean.table_idx)
                            }
                        })
                        .setNegativeButton("예약취소",object : DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                deleteReserve(bean.table_idx)
                            }
                        })
                    val alertDialog = builder.create()

                    alertDialog.show()
                })
            }

            override fun getItemViewType(i: Int): Int {
                return 0
            }
        })
        rv_cycle.adapter = mAdapter
        rv_cycle.layoutManager = GridLayoutManager(this,2)
    }

    private fun getReserve() {

        BaseOkhttpClient.request(
            HttpType.GET,
            Comm_Params.URL_TABLES_CLASS_IDX.replace(Comm_Params.CLASS_IDX,mClassType.toString()),
            null,
            null,
            object : BaseHttpCallback {
                override fun onResponse(call: Call, serverCode: Int, body: String, code: String) {
                    if(code == BaseOkhttpClient.DAOK){
                        val json = JSONObject(body)
                        val tables = json.getJSONArray("tables")
                        mAdapter!!.clear()
                        for(i in 0 until tables.length()){
                            val bean = Gson().fromJson<BeanReserve>(tables.getJSONObject(i).toString(),BeanReserve::class.java)
                            mAdapter!!.add(bean)
                        }
                        mAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        )
    }

    private fun setReserve(idx : Int) {
        BaseOkhttpClient.request(
            HttpType.POST,
            Comm_Params.URL_TABLES_CLASS_IDX_TABLE_IDX
                .replace(Comm_Params.CLASS_IDX,mClassType.toString())
                .replace(Comm_Params.TABLE_IDX,idx.toString()),
            BaseOkhttpClient.getHttpHeader(),
            null,
            object : BaseHttpCallback {
                override fun onResponse(call: Call, serverCode: Int, body: String, code: String) {
                    val json = JSONObject(body)
                    val message = json.getString("message")
                    Toast.makeText(this@ActivityReserve,message,Toast.LENGTH_SHORT).show()
                    getReserve()
                }
            }
        )
    }

    private fun deleteReserve(idx : Int) {
        BaseOkhttpClient.request(
            HttpType.DELETE,
            Comm_Params.URL_TABLES_CLASS_IDX_TABLE_IDX
                .replace(Comm_Params.CLASS_IDX,mClassType.toString())
                .replace(Comm_Params.TABLE_IDX,idx.toString()),
            BaseOkhttpClient.getHttpHeader(),
            null,
            object : BaseHttpCallback {
                override fun onResponse(call: Call, serverCode: Int, body: String, code: String) {
                    val json = JSONObject(body)
                    val message = json.getString("message")
                    Toast.makeText(this@ActivityReserve,message,Toast.LENGTH_SHORT).show()
                    getReserve()
                }
            }
        )
    }



}