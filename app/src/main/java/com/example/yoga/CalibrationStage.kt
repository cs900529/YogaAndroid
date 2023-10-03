package com.example.yoga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout.LayoutParams

class CalibrationStage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        setContentView(R.layout.activity_calibration_stage)

        val finish_button = findViewById<ImageButton>(R.id.finish)

        // 获取屏幕宽度和高度
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        // 计算按钮的宽度和高度为屏幕宽度和高度的10%
        val buttonWidth = (0.1 * screenWidth).toInt()
        val buttonHeight = (0.1 * screenHeight).toInt()

        // 设置按钮的布局参数
        val layoutParams = LayoutParams(buttonHeight, buttonHeight)
        layoutParams.topMargin=(0.9*screenWidth).toInt()
        layoutParams.leftMargin=(0.9*screenHeight).toInt()
        finish_button.layoutParams = layoutParams
    }
}