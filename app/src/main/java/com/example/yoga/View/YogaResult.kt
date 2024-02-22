package com.example.yoga.View

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.yoga.Model.GlobalVariable
import com.example.yoga.R
import com.example.yoga.databinding.ActivityYogaResultBinding

class YogaResult : AppCompatActivity() {
    private lateinit var yogaResultBinding: ActivityYogaResultBinding
    private var global=GlobalVariable.getInstance()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var python : Python
    private lateinit var heatmapReturn : PyObject
    private var myThread: Thread? = null
    fun lastpage(){
        global.currentMS = mediaPlayer.currentPosition
        mediaPlayer.stop()
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        //初始化yogaResultBinding
        yogaResultBinding = ActivityYogaResultBinding.inflate(layoutInflater)
        setContentView(yogaResultBinding.root)

        //啟動python
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        python = Python.getInstance()

        heatmapReturn = python.getModule("heatmap")

        // yogamap return
        myThread = Thread {
            try {
                while (!heatmapReturn.callAttr("checkReturn").toBoolean()) {
                    Thread.sleep(100)
                    print("checkReturn")
                }
                runOnUiThread {
                    lastpage()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        myThread?.start()


        val title = intent.getStringExtra("title")
        yogaResultBinding.title.text = title
        val finishTime = intent.getDoubleExtra("finishTime",0.0)
        yogaResultBinding.time.text = "完成時間:"+finishTime.toString()+"秒"
        val score = intent.getDoubleExtra("score",100.0)
        yogaResultBinding.score.text = "分數:${"%.2f".format(score)}"

        yogaResultBinding.back.text = "Back To Menu"
        yogaResultBinding.back.setOnClickListener {
            lastpage()
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.isLooping = true // 設定音樂循環播放
        mediaPlayer.seekTo(global.currentMS)
        mediaPlayer.start()
    }
    override fun onDestroy() {
        super.onDestroy()

        // 在Activity銷毀時結束thread
        myThread?.interrupt()
    }
    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }
}