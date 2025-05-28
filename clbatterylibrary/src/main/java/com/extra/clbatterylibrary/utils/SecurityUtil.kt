package com.extra.clbatterylibrary.utils

object SecurityUtil {
    init {
        System.loadLibrary("security")
    }

    private external fun getNativeKey(): String
    private external fun getNativeIv(): String
    private external fun getNativeConfigUrl(): String
    private external fun getNativeSubmitUrl(): String

    fun getAesKey(): String = getNativeKey()
    fun getAesIv(): String = getNativeIv()
    fun getConfigUrl(): String = getNativeConfigUrl()
    fun getSubmitUrl(): String = getNativeSubmitUrl()
} 