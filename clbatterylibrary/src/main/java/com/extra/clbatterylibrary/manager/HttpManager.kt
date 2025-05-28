package com.extra.clbatterylibrary.manager

import com.drake.net.Get
import com.drake.net.Post
import com.drake.net.utils.scopeNet
import com.extra.clbatterylibrary.utils.SecurityUtil

object HttpManager {
    private val HTTP_CONFIG: String by lazy { SecurityUtil.getConfigUrl() }
    private val HTTP_SUBMIT: String by lazy { SecurityUtil.getSubmitUrl() }

    // 提供公共访问方法
    fun getConfigUrl(): String = HTTP_CONFIG
    fun getSubmitUrl(): String = HTTP_SUBMIT

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
                val response = Post<String>(url) {
                    param("keyData", key)
                    param("q", channel)
                }.await()
                callback.invoke(response)
            } catch (e: Exception) {
                e.printStackTrace()
                callback.invoke(null)
            }
        }
    }
}