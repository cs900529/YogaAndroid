package com.example.yoga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class YogaMain : AppCompatActivity() {
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
    }
}