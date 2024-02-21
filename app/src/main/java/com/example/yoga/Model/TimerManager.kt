package com.example.yoga.Model

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.SystemClock
import androidx.annotation.RequiresApi

class TimerManager {

}

class FinishTimer{
    private var finishTime = 0.0
    private val handler = Handler()
    private var baseTime = SystemClock.elapsedRealtime()
    private val finishTimer = object : Runnable {
        override fun run() {
            finishTime = (SystemClock.elapsedRealtime()-baseTime)/1000.0
            handler.postDelayed(this, 100) // update every 0.1 second
        }
    }
    fun getTime():Double{return finishTime}
    fun handlerStart(){handler.post(finishTimer)}
    fun handlerStop(){handler.removeCallbacks(finishTimer)}
    fun getScore():Double{
        if (finishTime<40.1)
            return 100.0
        else if(finishTime<100.1)
            return 100.0 - 40.0*(finishTime - 40.0)/60.0
        else
            return 60.0
    }
}
class KSecCountdownTimer(k: Long) {
    interface TimerCallback {
        fun onTimerFinished()
    }
    private var timer: CountDownTimer? = null
    private var k=k
    private var timeLeft_ms: Long = k * 1000 // 初始计时为K秒
    private var timeLeft_str = ""
    private var countDown = BooleanArray(7) { true }
    private var callback: TimerCallback? = null

    private fun initializeTimer() {
        timer = object : CountDownTimer(timeLeft_ms, 100) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTick(ms_remain: Long) {
                timeLeft_ms = ms_remain
                // 每0.1秒执行一次的逻辑，例如更新 UI 显示剩余时间
                timeLeft_str = (ms_remain / 1000f).toString()
                //timeLeftBar

                var G = 0f
                var R = 0f
                if (ms_remain > k*500) {
                    G = 1f
                    R = (k*1000 - ms_remain) / (k*500f)
                } else {
                    R = 1f
                    G = ms_remain / (k*500f)
                }
                val barColor = Color.rgb(R, G, 0f)

                /*val layoutParams = yogamainBinding.timeLeftBar.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.matchConstraintPercentWidth = 0.47f*(ms_remain/30000f)
                yogamainBinding.timeLeftBar.layoutParams = layoutParams
                yogamainBinding.timeLeftBar.backgroundTintList = ColorStateList.valueOf(barColor)*/
                //tts
                if (ms_remain < 20000L && countDown[0]) {
                    //TTSSpeak("二十秒")
                    countDown[0] = false
                } else if (ms_remain < 10000L && countDown[1]) {
                    //TTSSpeak("十秒")
                    countDown[1] = false
                } else if (ms_remain < 5000L && countDown[2]) {
                    //TTSSpeak("五")
                    countDown[2] = false
                } else if (ms_remain < 4000L && countDown[3]) {
                    //TTSSpeak("四")
                    countDown[3] = false
                } else if (ms_remain < 3000L && countDown[4]) {
                    //TTSSpeak("三")
                    countDown[4] = false
                } else if (ms_remain < 2000L && countDown[5]) {
                    //TTSSpeak("二")
                    countDown[5] = false
                } else if (ms_remain < 1000L && countDown[6]) {
                    //TTSSpeak("一")
                    countDown[6] = false
                }
            }

            override fun onFinish() {
                // 计时器倒数完毕时触发的逻辑
                callback?.onTimerFinished()
            }
        }
    }

    fun startTimer(callback: TimerCallback) {
        this.callback=callback
        initializeTimer()
        // 开始计时器
        timer?.start()
    }
    fun resetTimer() {
        countDown = BooleanArray(7) { true }
        // 重置计时器为K秒
        timeLeft_ms = 30000
        timeLeft_str = ""
        timer?.cancel()
        timer = null
    }
    fun getRemainTimeStr():String{return timeLeft_str}
    fun setRemainTimeStr(str:String){timeLeft_str=str}
    fun isNotRunning():Boolean{return timer==null}
}
