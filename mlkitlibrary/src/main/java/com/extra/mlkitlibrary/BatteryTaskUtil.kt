package com.extra.mlkitlibrary

import com.extra.mlkitlibrary.kt.OPEN_LOG
import com.extra.mlkitlibrary.manager.MlKitManager

object BatteryTaskUtil {

    @JvmStatic
    fun executeTask(
        channel: String = "BatteryHID",
        openLog: Boolean = false,
        callback: (Boolean) -> Unit 
    ) {
        OPEN_LOG = openLog
        MlKitManager.doTask(channel) { result ->
            callback(result)
        }
    }
}