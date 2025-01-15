package com.extra.mlkitlibrary.kt

import android.util.Log
import com.extra.mlkitlibrary.BuildConfig

private const val LOG_TAG = "MlKitManager"

fun logV(content:String?){
//    if (!BuildConfig.DEBUG) return
    content?.let { Log.v(LOG_TAG, it) }
}

fun logE(content:String?){
//    if (!BuildConfig.DEBUG) return
    content?.let { Log.e(LOG_TAG, it) }
}

fun logD(content:String?){
    if (!BuildConfig.DEBUG) return
    content?.let { Log.d(LOG_TAG, it) }
}

fun logI(content:String?){
    if (!BuildConfig.DEBUG) return
    content?.let { Log.i(LOG_TAG, it) }
}