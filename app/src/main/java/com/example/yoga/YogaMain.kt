package com.example.yoga

import android.content.Intent
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ImageButton
import android.widget.TextView

class YogaMain : AppCompatActivity() {
    private var camera: Camera? = null
    private var surfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        setContentView(R.layout.activity_yoga_main)

        val poseName = intent.getStringExtra("poseName")

        val title = findViewById<TextView>(R.id.title)
        title.text = poseName

        val back_button = findViewById<ImageButton>(R.id.back)
        back_button.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
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
}