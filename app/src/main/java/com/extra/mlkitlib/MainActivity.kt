package com.extra.mlkitlib

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.extra.mlkitlibrary.BatteryTaskUtil

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
                BatteryTaskUtil.executeTask("0097", true) { res ->
                    if (res ){
                        // 执行成功的逻辑
                    } else {
                        // 执行失败的逻辑
                    }
                }
            }).start()
        }
    }
}