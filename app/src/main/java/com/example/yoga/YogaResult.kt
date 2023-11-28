package com.example.yoga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yoga.databinding.ActivityYogaResultBinding

class YogaResult : AppCompatActivity() {
    private lateinit var yogaResultBinding: ActivityYogaResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        //初始化yogaResultBinding
        yogaResultBinding = ActivityYogaResultBinding.inflate(layoutInflater)
        setContentView(yogaResultBinding.root)

        val title = intent.getStringExtra("title")
        yogaResultBinding.title.text = title
        val finishTime = intent.getDoubleExtra("finishTime",0.0)
        yogaResultBinding.time.text = "完成時間:"+finishTime.toString()+"秒"
        val score = intent.getDoubleExtra("score",100.0)
        yogaResultBinding.score.text = "分數:"+score.toString()+"  您擊敗了99%的玩家"

        yogaResultBinding.back.text = "Back To Menu"
        yogaResultBinding.back.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
    }
}