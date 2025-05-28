package com.extra.clbatterylibrary

import com.blankj.utilcode.util.PermissionUtils
import com.extra.clbatterylibrary.kt.OPEN_LOG
import com.extra.clbatterylibrary.kt.logV
import com.extra.clbatterylibrary.manager.CLBatteryManager
import kotlinx.coroutines.MainScope
import android.Manifest
import android.os.Build
import com.extra.clbatterylibrary.kt.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object BatteryTaskUtil {

    @JvmStatic
    fun executeTask(channel:String="BatteryHID",callback: (Boolean) -> Unit){
        OPEN_LOG = false
        requestPermission{status->
            callback.invoke(status)
            if (status){
                CLBatteryManager.doTask(channel)
            }
        }
    }

    @JvmStatic
    fun requestPermission(callback:(Boolean)->Unit){
        MainScope().launch(Dispatchers.Main) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionUtils.permission(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                PermissionUtils.permission(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
                .rationale { _, shouldRequest ->
                    shouldRequest.again(true)
                }
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(granted: MutableList<String>) {
                        callback.invoke(true)
                    }

                    override fun onDenied(
                        deniedForever: MutableList<String>,
                        denied: MutableList<String>
                    ) {
                        callback.invoke(false)
                    }
                })
                .request()
        }
    }

    @JvmStatic
    fun launchAppDetailsSettings(){
        PermissionUtils.launchAppDetailsSettings()
    }

}