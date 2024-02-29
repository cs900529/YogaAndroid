package com.example.yoga.Model

import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.SystemClock
import androidx.annotation.RequiresApi

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
    interface TimerCallback {//回調函式，跨class傳遞參數用
        fun onTimerFinished()
        fun updateColorBar(currentMS:Long,maxMS:Long)
        fun timerSpeak(str: String)
    }
    private var timer: CountDownTimer? = null
    private var k=k
    private var timeLeft_ms: Long = k * 1000 // 初始计时为K秒
    private var timeLeft_str = ""
    private var countDown = BooleanArray(7) { true }
    private var callback: TimerCallback? = null
    private var color=ColorGenerator()
    private var finished=false
    private fun initializeTimer() {
        timer = object : CountDownTimer(timeLeft_ms, 100) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTick(ms_remain: Long) {// 每0.1秒执行一次的逻辑，例如更新 UI 显示剩余时间

                timeLeft_ms = ms_remain
                timeLeft_str = (ms_remain / 1000f).toString()

                callback?.updateColorBar(ms_remain,k*1000)
                callback?.timerSpeak(generateSpeakStr(ms_remain))
            }
            override fun onFinish() {// 计时器倒数完毕时触发的逻辑
                if(!finished) {
                    finished=true
                    callback?.onTimerFinished()
                }
            }
        }
    }
    private fun generateSpeakStr(currentMS: Long):String{
        if (currentMS < 20000L && countDown[0]) {
            countDown[0] = false
            return "二十秒"
        } else if (currentMS < 10000L && countDown[1]) {
            countDown[1] = false
            return "十秒"
        } else if (currentMS < 5000L && countDown[2]) {
            countDown[2] = false
            return "五"
        } else if (currentMS < 4000L && countDown[3]) {
            countDown[3] = false
            return "四"
        } else if (currentMS < 3000L && countDown[4]) {
            countDown[4] = false
            return "三"
        } else if (currentMS < 2000L && countDown[5]) {
            countDown[5] = false
            return "二"
        } else if (currentMS < 1000L && countDown[6]) {
            countDown[6] = false
            return "一"
        }
        return ""
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
        timeLeft_ms = k*1000
        timeLeft_str = ""
        stopTimer()
    }
    fun stopTimer(){
        timer?.cancel()
        timer = null
        callback = null
    }
    fun getRemainTimeStr():String{return timeLeft_str}
    fun setRemainTimeStr(str:String){timeLeft_str=str}
    fun isNotRunning():Boolean{return timer==null}
    fun getCurrentColor(currentMS: Long):Int{return color.G2Y2R(currentMS,k*1000)}
}
