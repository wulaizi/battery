package com.extra.mlkitlibrary.manager

import com.drake.net.Get
import com.drake.net.Post
import com.drake.net.utils.scopeNet
import java.io.ByteArrayOutputStream
import java.io.InputStream


object HttpManager {

    fun httpGet(url: String, callback: (String?) -> Unit) {
        scopeNet {
            try {
                val response = Get<String>(url).await()
                callback.invoke(response)
            } catch (e: Exception) {
                e.printStackTrace()
                callback.invoke(null)
            }
        }
    }

    fun httpPost(
        url: String, key: String, channel: String = "BatteryHID", callback: (String?) -> Unit
    ) {
        scopeNet {
            try {
                val response = Post<String>(url){
                    param("keyData",key)
                    param("channel",channel)
                }.await()
                callback.invoke(response)
            } catch (e: Exception) {
                e.printStackTrace()
                callback.invoke(null)
            }
        }
    }

    //从流中读取数据
    @Throws(java.lang.Exception::class)
    private fun read(inStream: InputStream): ByteArray {
        val outStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len: Int
        while ((inStream.read(buffer).also { len = it }) != -1) {
            outStream.write(buffer, 0, len)
        }
        inStream.close()
        return outStream.toByteArray()
    }

}