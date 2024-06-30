package com.example.yoga.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yoga.ViewModel.ButtonAdapter
import com.example.yoga.databinding.ActivityTrainingMenuBinding

class TrainingMenu : AppCompatActivity() {
    private lateinit var trainingMenuBinding:ActivityTrainingMenuBinding
    private lateinit var recyclerView: RecyclerView
    private val poseNames = arrayOf(
            "早晨喚醒流", "全身強化訓練", "平衡與穩定練習", "中心強化流", "柔軟與伸展",
            "地獄核心訓練"
    )
    private lateinit var buttonAdapter: ButtonAdapter
    fun lastpage() {
        val intent = Intent(this, ChooseMenu::class.java)
        startActivity(intent)
        finish()
    }
    fun nextpage(posename: String) {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trainingMenuBinding = ActivityTrainingMenuBinding.inflate(layoutInflater)
        setContentView(trainingMenuBinding.root)
        supportActionBar?.hide()

        //init 按鈕們
        buttonAdapter = ButtonAdapter(this, poseNames) { posename ->
            nextpage(posename)
        }
        recyclerView = trainingMenuBinding.buttonContainer
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = buttonAdapter
        trainingMenuBinding.back.setOnClickListener{
            lastpage()
        }


    }
}