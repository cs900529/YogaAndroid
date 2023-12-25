package com.example.yoga

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Menu : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var global: GlobalVariable
    fun lastpage(){
        global.currentMS = mediaPlayer.currentPosition
        mediaPlayer.stop()
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("playMusic",false)
        }
        startActivity(intent)
        finish()
    }
    fun nextpage(posename:String){
        global.currentMS = mediaPlayer.currentPosition
        mediaPlayer.stop()
        val intent = Intent(this, VideoGuide::class.java).apply {
            putExtra("poseName",posename)
        }
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide() // 隐藏title bar
        
        val back_button = findViewById<ImageButton>(R.id.back)
        back_button.setOnClickListener {
            lastpage()
        }

        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            nextpage("Tree Style")
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            nextpage("Warrior2 Style")
        }

        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {
            nextpage("Plank")
        }

        val button4 = findViewById<Button>(R.id.button4)
        button4.setOnClickListener {
            nextpage("Reverse Plank")
        }

        val button5 = findViewById<Button>(R.id.button5)
        button5.setOnClickListener {
            nextpage("Child\'s pose")
        }

        val button6 = findViewById<Button>(R.id.button6)
        button6.setOnClickListener {
            nextpage("Seated Forward Bend")
        }

        val button7 = findViewById<Button>(R.id.button7)
        button7.setOnClickListener {
            nextpage("Low Lunge")
        }

        val button8 = findViewById<Button>(R.id.button8)
        button8.setOnClickListener {
            nextpage("Downward dog")
        }

        val button9 = findViewById<Button>(R.id.button9)
        button9.setOnClickListener {
            nextpage("Pyramid pose")
        }

        val button10 = findViewById<Button>(R.id.button10)
        button10.setOnClickListener {
            nextpage("Bridge pose")
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