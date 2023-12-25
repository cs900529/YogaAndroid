package com.example.yoga

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yoga.databinding.ActivityYogaResultBinding

class YogaResult : AppCompatActivity() {
    private lateinit var yogaResultBinding: ActivityYogaResultBinding
    lateinit var global: GlobalVariable
    private lateinit var mediaPlayer: MediaPlayer
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

        global = application as GlobalVariable
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.isLooping = true // 設定音樂循環播放
        mediaPlayer.seekTo(global.currentMS)
        mediaPlayer.start()
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