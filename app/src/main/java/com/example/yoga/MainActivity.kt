package com.example.yoga

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.yoga.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private  val CAMERA_PERMISSION_REQUEST_CODE = 1001  //據說是隨便設定就好
    //private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        setContentView(R.layout.activity_main)

        val start = findViewById<Button>(R.id.start)
        start.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, CalibrationStage::class.java)
            startActivity(intent)
        }

        // 如果没有相机权限，请求相机权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }

    }
}