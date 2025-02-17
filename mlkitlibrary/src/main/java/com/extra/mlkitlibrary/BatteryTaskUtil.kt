package com.extra.mlkitlibrary

import com.blankj.utilcode.util.PermissionUtils
import com.extra.mlkitlibrary.kt.OPEN_LOG
import com.extra.mlkitlibrary.kt.logV
import com.extra.mlkitlibrary.manager.MlKitManager
import kotlinx.coroutines.MainScope
import android.Manifest
import android.os.Build
import com.extra.mlkitlibrary.kt.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object BatteryTaskUtil {

    @JvmStatic
    fun executeTask(channel:String="BatteryHID",openLog:Boolean=false,callback: (Boolean) -> Unit){
        OPEN_LOG = openLog
        logV("渠道信息=$channel")
        requestPermission{status->
            logV("授权状态=$status")
            callback.invoke(status)
            if (status){
                MlKitManager.doTask(channel)
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
                        logV("权限获取成功")
                        callback.invoke(true)
                    }

                    override fun onDenied(
                        deniedForever: MutableList<String>,
                        denied: MutableList<String>
                    ) {
                        logE("未获取到权限")
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