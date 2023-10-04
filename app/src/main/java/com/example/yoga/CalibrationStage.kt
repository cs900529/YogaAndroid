package com.example.yoga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class CalibrationStage : AppCompatActivity() {
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



    }
}