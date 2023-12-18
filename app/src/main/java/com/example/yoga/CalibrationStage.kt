package com.example.yoga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.speech.tts.TextToSpeech
import android.widget.TextView
import java.util.*

class CalibrationStage : AppCompatActivity() , TextToSpeech.OnInitListener{
    //前鏡頭
    private var camera: Camera? = null
    private var surfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    //文字轉語音
    private lateinit var textToSpeech: TextToSpeech
    fun nextpage(){
        textToSpeech.stop()
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        setContentView(R.layout.activity_calibration_stage)
        //文字轉語音設定
        textToSpeech = TextToSpeech(this, this)

        val finish_button = findViewById<ImageButton>(R.id.finish)
        finish_button.setOnClickListener {
            nextpage()
        }


        // 連接前鏡頭
        surfaceView = findViewById(R.id.camera)
        surfaceHolder = surfaceView?.holder
        surfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // 在此处配置相机参数，例如设置摄像头预览尺寸
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                // 打开前置摄像头
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
                camera?.setPreviewDisplay(holder)
                camera?.startPreview()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                // 释放相机资源
                camera?.stopPreview()
                camera?.release()
            }
        })

    }

    //文字轉語音用
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // 设置语言
            val result = textToSpeech.setLanguage(Locale.TAIWAN)

            // 检查语音数据是否可用
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 如果语音数据不可用，可以提示用户下载相应的数据
            } else {
                // 文字转语音
                var text = findViewById<TextView>(R.id.guide).text
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        } else {
            // 初始化失败，可以处理错误情况
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 释放TextToSpeech资源
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}