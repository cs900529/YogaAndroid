package com.example.yoga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
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

    }
}