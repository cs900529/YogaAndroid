package com.example.yoga

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.VideoView

class VideoGuide : AppCompatActivity() {
    //獲取影片檔案
    fun getfile(context: Context, filename: String): Int {
        if(filename == "Tree Style")
            return R.raw.tree_style
        else if(filename == "Warrior2 Style")
            return R.raw.warrior2_style
        else if(filename == "Plank")
            return R.raw.plank
        else if(filename == "Reverse Plank")
            return R.raw.reverse_plank
        else if(filename == "Child's pose")
            return R.raw.childspose_teacher
        else if(filename == "Seated Forward Bend")
            return R.raw.seatedforwardbend_teacher
        else if(filename == "Low Lunge")
            return R.raw.lowlunge_teacher
        else if(filename == "Downward dog")
            return R.raw.downwarddog_teacher
        else if(filename == "Pyramid pose")
            return R.raw.pyramid_teacher
        else if(filename == "Bridge pose")
            return R.raw.bridge_teacher
        return R.raw.tree_style
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
        videoPlayer.setOnPreparedListener { mp ->
            mp.isLooping = true
        }

        val finish = findViewById<ImageButton>(R.id.finish)
        finish.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, YogaMain::class.java).apply {
                putExtra("poseName",poseName)
            }
            startActivity(intent)
        }
    }
}