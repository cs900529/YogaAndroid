package com.example.yoga.Model

import MyTTS
import android.app.Application
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech

class GlobalVariable: Application() {
    var currentMS = 0
    lateinit var TTS:MyTTS
    companion object {//單例模式
        @Volatile
        private var instance: GlobalVariable? = null

        fun getInstance(): GlobalVariable {
            return instance ?: synchronized(this) {
                instance ?: GlobalVariable().also { instance = it }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        TTS = MyTTS(applicationContext)
        TTS.init(applicationContext)
    }
}
