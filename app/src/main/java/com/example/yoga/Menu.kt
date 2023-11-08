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

        val button5 = findViewById<Button>(R.id.button5)
        button5.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Child\'s pose")
            }
            startActivity(intent)
        }

        val button6 = findViewById<Button>(R.id.button6)
        button6.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Seated Forward Bend")
            }
            startActivity(intent)
        }

        val button7 = findViewById<Button>(R.id.button7)
        button7.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Low Lunge")
            }
            startActivity(intent)
        }

        val button8 = findViewById<Button>(R.id.button8)
        button8.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Downward dog")
            }
            startActivity(intent)
        }

        val button9 = findViewById<Button>(R.id.button9)
        button9.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Pyramid pose")
            }
            startActivity(intent)
        }

        val button10 = findViewById<Button>(R.id.button10)
        button10.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, VideoGuide::class.java).apply {
                putExtra("poseName","Bridge pose")
            }
            startActivity(intent)
        }
    }
}