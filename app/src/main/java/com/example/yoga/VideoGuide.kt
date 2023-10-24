package com.example.yoga

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.VideoView

class VideoGuide : AppCompatActivity() {
    fun getfile(context: Context, filename: String): Int {
        if(filename == "Tree Style")
            return R.raw.tree_style
        return R.raw.tree_style
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        setContentView(R.layout.activity_video_guide)

        val poseName = intent.getStringExtra("poseName")

        val title = findViewById<TextView>(R.id.videoTitle)
        title.text = poseName

        val videoPlayer = findViewById<VideoView>(R.id.videoPlayer)
        // 获取视频文件的路径
        val videoPath = "android.resource://" + packageName + "/" +  getfile(this, poseName.toString() )

        // 设置视频路径并开始播放
        videoPlayer.setVideoURI(Uri.parse(videoPath))
        videoPlayer.start()

        val finish = findViewById<ImageButton>(R.id.finish)
        finish.setOnClickListener {

        }
    }
}