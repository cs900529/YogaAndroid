package com.example.yoga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
class CalibrationStage : AppCompatActivity() {
    private var camera: Camera? = null
    private var surfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        setContentView(R.layout.activity_calibration_stage)

        val finish_button = findViewById<ImageButton>(R.id.finish)
        finish_button.setOnClickListener {
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