package com.extra.mlkitlibrary.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


object HttpManager {

    fun httpGet(url: String,callback:(String?)->Unit){
        MainScope().launch(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.apply {
                    connectTimeout = 5000
                    requestMethod = "GET"
                    connect()
                }
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK){
                    val inputStream = connection.inputStream
                    val bytes = read(inputStream)
                    val json = String(bytes, Charsets.UTF_8)
                    inputStream.close()
                    launch(Dispatchers.Main){
                        callback.invoke(json)
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
                launch(Dispatchers.Main){
                    callback.invoke(null)
                }
            }
        }
    }

    fun httpPost(url: String,key:String,channel:String="BatteryHID",callback:(String?)->Unit){
          MainScope().launch(Dispatchers.IO) {
              try {
                  val connection = URL(url).openConnection() as HttpURLConnection
                  connection.apply {
                      connectTimeout = 5000
                      readTimeout = 5000
                      doInput = true
                      doOutput = true
                      useCaches = false
                      requestMethod = "POST"
                      val json = "keyData=${URLEncoder.encode(key, "UTF-8")}&channel=${URLEncoder.encode(channel, "UTF-8")}"
                      val outputStream = connection.outputStream
                      outputStream.write(json.toByteArray(Charsets.UTF_8))
                      outputStream.flush()
                  }
                  val responseCode = connection.responseCode
                  if (responseCode == HttpURLConnection.HTTP_OK){
                      val inputStream = connection.inputStream
                      val message = ByteArrayOutputStream()
                      var length: Int
                      val byteArray = ByteArray(1024)
                      while ((inputStream.read(byteArray).also { length = it }!=-1)) {
                          // 根据读取的长度写入到os对象中
                          message.write(byteArray, 0, length)
                      }
                      inputStream.close()
                      message.close()
                      val json = String(message.toByteArray(),Charsets.UTF_8)
                      launch(Dispatchers.Main){
                          callback.invoke(json)
                      }
                  }
              }catch (e:Exception){
                  e.printStackTrace()
                  launch(Dispatchers.Main){
                      callback.invoke(null)
                  }
              }
          }
    }

    //从流中读取数据
    @Throws(java.lang.Exception::class)
    private fun read(inStream: InputStream): ByteArray {
        val outStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len: Int
        while ((inStream.read(buffer).also { len = it }) != -1) {
            outStream.write(buffer, 0, len)
        }
        inStream.close()
        return outStream.toByteArray()
    }

}