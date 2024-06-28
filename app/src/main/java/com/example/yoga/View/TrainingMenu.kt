package com.example.yoga.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yoga.R

class TrainingMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_menu)
        supportActionBar?.hide()
    }
}