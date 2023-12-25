package com.example.yoga

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.VideoView

class VideoGuide : AppCompatActivity() {
    lateinit var global: GlobalVariable
    private lateinit var mediaPlayer: MediaPlayer
    fun nextpage(poseName:String){
        global.currentMS = mediaPlayer.currentPosition
        mediaPlayer.stop()
        val intent = Intent(this, YogaMain::class.java).apply {
            putExtra("poseName",poseName)
        }
        startActivity(intent)
        finish()
    }
    //獲取影片檔案
    private fun getfile(context: Context, filename: String): Int {
        return when (filename) {
            "Tree Style" -> R.raw.tree_style
            "Warrior2 Style" -> R.raw.warrior2_style
            "Plank" -> R.raw.plank
            "Reverse Plank" -> R.raw.reverse_plank
            "Child's pose" -> R.raw.childspose_teacher
            "Seated Forward Bend" -> R.raw.seatedforwardbend_teacher
            "Low Lunge" -> R.raw.lowlunge_teacher
            "Downward dog" -> R.raw.downwarddog_teacher
            "Pyramid pose" -> R.raw.pyramid_teacher
            "Bridge pose" -> R.raw.bridge_teacher
            else -> R.raw.tree_style
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        setContentView(R.layout.activity_video_guide)

        val poseName = intent.getStringExtra("poseName")

        val title = findViewById<TextView>(R.id.videoTitle)
        title.text = poseName

        //video player init
        val videoPlayer = findViewById<VideoView>(R.id.videoPlayer)
        val videoPath = "android.resource://" + packageName + "/" +  getfile(this, poseName.toString() )
        videoPlayer.setVideoURI(Uri.parse(videoPath))
        videoPlayer.start()
        // 设置循环播放
        videoPlayer.setOnPreparedListener { mp -> // mp = mediaplayer
            mp.isLooping = true
        }

        val finish = findViewById<ImageButton>(R.id.finish)
        finish.setOnClickListener {
            nextpage(poseName.toString())
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