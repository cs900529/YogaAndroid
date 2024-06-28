package com.example.yoga.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yoga.R

class ChooseMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_menu)
        supportActionBar?.hide()
    }
}