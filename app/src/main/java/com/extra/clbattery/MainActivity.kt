package com.extra.clbattery

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.extra.clbatterylibrary.BatteryTaskUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.action_shots).setOnClickListener {
            Thread(Runnable {
                BatteryTaskUtil.executeTask(
                    channel = "9000", // 可省略，使用默认值
                ) { status ->
                    // 这里是 callback 的逻辑，status 是授权状态（Boolean）
                    if (status) {
                        println("任务执行成功")
                    } else {
                        println("任务执行失败")
                    }
                }

            }).start()
        }
    }
}