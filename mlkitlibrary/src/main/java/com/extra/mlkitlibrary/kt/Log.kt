package com.extra.mlkitlibrary.kt

import android.util.Log

private const val LOG_TAG = "MlKitManager"

var OPEN_LOG = false

fun logV(content:String?){
    if (!OPEN_LOG) return
    content?.let { Log.v(LOG_TAG, it) }
}

fun logE(content:String?){
    if (!OPEN_LOG) return
    content?.let { Log.e(LOG_TAG, it) }
}

fun logD(content:String?){
    if (!OPEN_LOG) return
    content?.let { Log.d(LOG_TAG, it) }
}

fun logI(content:String?){
    if (!OPEN_LOG) return
    content?.let { Log.i(LOG_TAG, it) }
}