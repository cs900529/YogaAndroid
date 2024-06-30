package com.example.yoga.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yoga.databinding.ActivityChooseMenuBinding

class ChooseMenu : AppCompatActivity() {
    private lateinit var chooseMenuBinding:ActivityChooseMenuBinding
    fun lastpage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chooseMenuBinding = ActivityChooseMenuBinding.inflate(layoutInflater)
        setContentView(chooseMenuBinding.root)
        supportActionBar?.hide()

        chooseMenuBinding.back.setOnClickListener{
            lastpage()
        }
        //之後再來包體感互動
        chooseMenuBinding.allPose.setOnClickListener{
            val intent = Intent(this, AllPoseMenu::class.java)
            startActivity(intent)
            finish()
        }
        chooseMenuBinding.trainingMenu.setOnClickListener{
            val intent = Intent(this, TrainingMenu::class.java)
            startActivity(intent)
            finish()
        }
    }
}