package com.example.yoga.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var recyclerView: RecyclerView
    private final var WIDTH = 2
    private final var HEIGHT = 10

    private val poseNames = arrayOf(
            "Tree Style", "Warrior2 Style", "Plank", "Reverse Plank", "Child's pose",
            "Seated Forward Bend", "Low Lunge", "Downward dog", "Pyramid pose", "Bridge pose",
            "Mountain pose", "Triangle pose", "Pose 13", "Pose 14", "Pose 15",
            "Pose 16", "Pose 17", "Pose 18", "Pose 19", "Pose 20"
    )

    private lateinit var buttonAdapter: ButtonAdapter

    fun lastpage() {
        try {
            functionThread?.interrupt()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun nextpage() {
        nextpage(poseNames[buttonAdapter.getIndexByButton(currentSelect)])
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
    fun moveRollBarTo(index:Int){
        recyclerView.smoothScrollToPosition(index)
    }
    fun selectTo(index:Int){
        buttonAdapter.setSelectedIndex(index)
        moveRollBarTo(index)
        currentSelect = buttonAdapter.getButtonByIndex(index)
    }

    fun up() {
        recyclerView.post {
            var index = buttonAdapter.getIndexByButton(currentSelect)
            index = (index - WIDTH + poseNames.size) % poseNames.size
            selectTo(index)
        }
    }

    fun down() {
        recyclerView.post {
            var index = buttonAdapter.getIndexByButton(currentSelect)
            index = (index + WIDTH) % poseNames.size
            selectTo(index)
        }
    }

    fun left() {
        recyclerView.post {
            var index = buttonAdapter.getIndexByButton(currentSelect)
            var q = index/WIDTH
            var r = index%WIDTH
            r=(r-1+WIDTH)%WIDTH
            index = q*WIDTH+r
            selectTo(index)
        }
    }

    fun right() {
        recyclerView.post {
            var index = buttonAdapter.getIndexByButton(currentSelect)
            var q = index/WIDTH
            var r = index%WIDTH
            r=(r+1)%WIDTH
            index = q*WIDTH+r
            selectTo(index)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuBinding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(menuBinding.root)
        supportActionBar?.hide()

        menuBinding.back.setOnClickListener {
            lastpage()
        }

        buttonAdapter = ButtonAdapter(this, poseNames) { posename ->
            nextpage(posename)
        }
        recyclerView = menuBinding.buttonContainer
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = buttonAdapter

        // 獲取索引為 0 的按鈕並設置 currentSelect
        recyclerView.post {
            val button = buttonAdapter.getButtonByIndex(0)
            if (button != null) {
                currentSelect = button
            }
        }

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        python = Python.getInstance()

        heatmapFunction = python.getModule("heatmap")

        functionThread = Thread {
            try {
                Thread.sleep(1000)
                while (true) {
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
