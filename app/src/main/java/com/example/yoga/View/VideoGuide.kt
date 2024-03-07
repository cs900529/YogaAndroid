package com.example.yoga.View

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.yoga.Model.GlobalVariable
import com.example.yoga.Model.fileNameGetter
import com.example.yoga.databinding.ActivityVideoGuideBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoGuide : AppCompatActivity() {
    private lateinit var videoGuideBinding: ActivityVideoGuideBinding
    private var global=GlobalVariable.getInstance()
    var poseName=""

    // yogamap next
    private lateinit var python : Python
    private lateinit var heatmapNext : PyObject
    private var nextThread: Thread? = null
    private var fileGetter=fileNameGetter()

    fun nextpage(){

        try {
            nextThread?.interrupt()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val intent = Intent(this, YogaMain::class.java).apply {
            putExtra("poseName",poseName)
        }
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar

        videoGuideBinding = ActivityVideoGuideBinding.inflate(layoutInflater)
        setContentView(videoGuideBinding.root)

        poseName = intent.getStringExtra("poseName").toString()

        videoGuideBinding.videoTitle.text = poseName

        //video player init
        val videoPath = "android.resource://" + packageName + "/" +  fileGetter.getfile(this, poseName)
        videoGuideBinding.videoPlayer.setVideoURI(Uri.parse(videoPath))
        videoGuideBinding.videoPlayer.start()
        // 设置循环播放
        videoGuideBinding.videoPlayer.setOnPreparedListener { mp -> // mp = mediaplayer
            mp.isLooping = true
        }

        videoGuideBinding.finish.setOnClickListener {
            nextpage()
        }

        //啟動python
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        python = Python.getInstance()

        // yogamap return
        heatmapNext = python.getModule("heatmap")

        // yogamap return
        nextThread = Thread {
            try {
                Thread.sleep(2000)
                while (!heatmapNext.callAttr("checkReturn").toBoolean()) {
                    Thread.sleep(100)
                }
                runOnUiThread {
                    nextpage()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        nextThread?.start()
    }
    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(800)
            global.backgroundMusic.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        global.backgroundMusic.pause()
    }
    override fun onPause() {
        super.onPause()
        global.TTS.stop()
        global.backgroundMusic.pause()
    }

    override fun onResume() {
        super.onResume()
        global.backgroundMusic.play()
    }
}
