package com.extra.mlkitlibrary.manager

import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object HttpManager {

    private val okHttpClient = OkHttpClient.Builder().build()

    fun httpGet(url: String,callback:(String?)->Unit){
        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                callback.invoke(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                callback.invoke(json)
            }
        })
    }

    fun httpPost(url: String,key:String,channel:String="BatteryHID",callback:(String?)->Unit){
        val builder = FormBody.Builder()
        builder.add("keyData",key)
        builder.add("channel",channel)
        val request = Request.Builder().url(url).post(builder.build()).build()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                callback.invoke(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                callback.invoke(json)
            }
        })
    }

}