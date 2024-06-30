package com.example.yoga.View

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.yoga.Model.KSecCountdownTimer
import com.example.yoga.databinding.ActivityRestIntervalBinding

class RestInterval : AppCompatActivity() ,KSecCountdownTimer.TimerCallback{
    private lateinit var restIntervalBinding:ActivityRestIntervalBinding
    private var timer30S = KSecCountdownTimer(30)
    fun lastpage(){
        timer30S.stopTimer()
        val intent = Intent(this, TrainingMenu::class.java)
        startActivity(intent)
        finish()
    }
    //30秒倒數結束
    override fun onTimerFinished() {
        lastpage()
        /*timer30S.setRemainTimeStr("结束")
        timer30S.stopTimer()
        //停止计时
        timerCurrent.handlerStop()
        score = timerCurrent.getScore()
        nextpage()*/
    }
    //更新倒數條的顏色
    override fun updateColorBar(currentMs:Long,maxMS:Long) {
        val barColor = timer30S.getCurrentColor(currentMs)
        val layoutParams = restIntervalBinding.timeLeftBar.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.matchConstraintPercentWidth = currentMs/(1f*maxMS)
        restIntervalBinding.timeLeftBar.layoutParams = layoutParams
        restIntervalBinding.timeLeftBar.backgroundTintList = ColorStateList.valueOf(barColor)
    }
    //Timer TTS speak
    override fun timerSpeak(str: String) {

        /*if(str != "")
            TTSSpeak(str)*/
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restIntervalBinding = ActivityRestIntervalBinding.inflate(layoutInflater)
        setContentView(restIntervalBinding.root)
        supportActionBar?.hide() // 隐藏title bar

        timer30S.startTimer(this)
        restIntervalBinding.back.setOnClickListener{
            lastpage()
        }
    }
}