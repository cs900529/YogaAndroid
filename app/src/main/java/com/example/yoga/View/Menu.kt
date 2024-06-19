package com.example.yoga.View

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.yoga.Model.GlobalVariable
import com.example.yoga.ViewModel.ButtonAdapter
import com.example.yoga.databinding.ActivityMenuBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Menu : AppCompatActivity() {
    private lateinit var menuBinding: ActivityMenuBinding
    private var global = GlobalVariable.getInstance()
    private lateinit var currentSelect: Button

    private lateinit var python: Python
    private lateinit var heatmapFunction: PyObject
    private var functionThread: Thread? = null
    private var functionNumber: Int = 0

    private val poseNames = arrayOf(
            "Tree Style", "Warrior2 Style", "Plank", "Reverse Plank", "Child's pose",
            "Seated Forward Bend", "Low Lunge", "Downward dog", "Pyramid pose", "Bridge pose",
            "Pose 11", "Pose 12", "Pose 13", "Pose 14", "Pose 15",
            "Pose 16", "Pose 17", "Pose 18", "Pose 19", "Pose 20"
    )

    fun lastpage() {
        try {
            functionThread?.interrupt()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("playMusic", false)
        }
        startActivity(intent)
        finish()
    }

    fun nextpage() {
        nextpage(currentSelect.text.toString())
    }

    fun nextpage(posename: String) {
        try {
            functionThread?.interrupt()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val intent = Intent(this, VideoGuide::class.java).apply {
            putExtra("poseName", posename)
        }
        startActivity(intent)
        finish()
    }

    fun select() {
        currentSelect.setBackgroundColor(Color.rgb(10, 240, 5))
        currentSelect.setTextColor(Color.rgb(60, 60, 0))
        currentSelect.setShadowLayer(15f, 5f, 5f, Color.WHITE)
    }

    fun unselect() {
        currentSelect.setBackgroundColor(Color.BLUE)
        currentSelect.setTextColor(Color.WHITE)
        currentSelect.setShadowLayer(0f, 0f, 0f, 0)
    }

    fun selectTo(btn: Button) {
        unselect()
        currentSelect = btn
        select()
    }

    fun up() {
        val index = (poseNames.indexOf(currentSelect.text.toString()) - 2 + poseNames.size) % poseNames.size
        selectTo(buttons[index])
    }

    fun down() {
        val index = (poseNames.indexOf(currentSelect.text.toString()) + 2) % poseNames.size
        selectTo(buttons[index])
    }

    fun left() {
        val index = (poseNames.indexOf(currentSelect.text.toString()) - 1 + poseNames.size) % poseNames.size
        selectTo(buttons[index])
    }

    fun right() {
        val index = (poseNames.indexOf(currentSelect.text.toString()) + 1) % poseNames.size
        selectTo(buttons[index])
    }

    private lateinit var buttons: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuBinding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(menuBinding.root)
        supportActionBar?.hide()

        val recyclerView = menuBinding.buttonContainer
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ButtonAdapter(this, poseNames) { posename ->
            nextpage(posename)
        }

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        python = Python.getInstance()

        heatmapFunction = python.getModule("heatmap")

        functionThread = Thread {
            try {
                while (true) {
                    Thread.sleep(2000)
                    functionNumber = heatmapFunction.callAttr("checkFunction").toInt()
                    runOnUiThread {
                        when (functionNumber) {
                            1 -> right()
                            2 -> up()
                            3 -> left()
                            4 -> down()
                        }
                    }
                    if (heatmapFunction.callAttr("checkReturn").toBoolean()) {
                        nextpage()
                        break
                    }
                    Thread.sleep(750)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        functionThread?.start()
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(800)
            global.backgroundMusic.play()
        }
    }

    override fun onPause() {
        super.onPause()
        global.backgroundMusic.pause()
    }

    override fun onResume() {
        super.onResume()
        global.backgroundMusic.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        global.backgroundMusic.pause()
    }
}
