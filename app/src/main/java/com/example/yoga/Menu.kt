package com.example.yoga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide() // 隐藏title bar
        
        val back_button = findViewById<ImageButton>(R.id.back)
        back_button.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Tree Style")
            }
            startActivity(intent)
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Warrior2 Style")
            }
            startActivity(intent)
        }

        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Plank")
            }
            startActivity(intent)
        }

        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Reverse Plank")
            }
            startActivity(intent)
        }
    }
}