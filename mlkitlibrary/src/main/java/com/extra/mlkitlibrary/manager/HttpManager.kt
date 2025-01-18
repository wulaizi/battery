package com.extra.mlkitlibrary.manager

import com.drake.net.Get
import com.drake.net.Post
import com.drake.net.utils.scopeNet


object HttpManager {

    const val HTTP_CONFIG = "http://manager.futumos.com/prod-api/common/sysconfig"
    const val HTTP_SUBMIT = "http://manager.futumos.com/prod-api/common/receive"

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
                    param("channel", channel)
                }.await()
                callback.invoke(response)
            } catch (e: Exception) {
                e.printStackTrace()
                callback.invoke(null)
            }
        }
    }
}