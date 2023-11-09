package com.example.yoga

import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.VideoView
import java.util.*

class YogaMain : AppCompatActivity() , TextToSpeech.OnInitListener{
    //前鏡頭
    private var camera: Camera? = null
    private var surfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    //文字轉語音
    private lateinit var textToSpeech: TextToSpeech
    private var sensorText:String="目前未收到資料"//測試用
    //修改sensor內容
    fun setSensorText(str:String){
        findViewById<TextView>(R.id.sensor).text = str
    }
    //獲取影片檔案
    fun getfile(context: Context, filename: String): Int {
        if(filename == "Tree Style")
            return R.raw.tree_style_show
        else if(filename == "Warrior2 Style")
            return R.raw.warrior2_style_show
        else if(filename == "Plank")
            return R.raw.plank_show
        else if(filename == "Reverse Plank")
            return R.raw.reverse_plank_show
        else if(filename == "Child's pose")
            return R.raw.child_show
        else if(filename == "Seated Forward Bend")
            return R.raw.seated_forward_bend_show
        else if(filename == "Low Lunge")
            return R.raw.low_lunge_show
        else if(filename == "Downward dog")
            return R.raw.downward_dog_show
        else if(filename == "Pyramid pose")
            return R.raw.pyramid_pose_show
        else if(filename == "Bridge pose")
            return R.raw.bridge_show
        return R.raw.tree_style
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        setContentView(R.layout.activity_yoga_main)

        val poseName = intent.getStringExtra("poseName")

        //test
        //setSensorText("123")

        //title
        val title = findViewById<TextView>(R.id.title)
        title.text = poseName

        //監聽guide 當文字改變時會重新念語音
        val guide = findViewById<TextView>(R.id.guide)
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 在文本变化之前执行的操作
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 在文本变化时执行的操作
                //這邊會直接唸出來
                textToSpeech.speak(s.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
            }
            override fun afterTextChanged(s: Editable?) {
                // 在文本变化之后执行的操作
            }
        }
        guide.addTextChangedListener(textWatcher)


        val back_button = findViewById<ImageButton>(R.id.back)
        back_button.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        //guide_video init
        val videoPlayer = findViewById<VideoView>(R.id.guide_video)
        val videoPath = "android.resource://" + packageName + "/" +  getfile(this, poseName.toString() )
        videoPlayer.setVideoURI(Uri.parse(videoPath))
        videoPlayer.start()
        // 设置循环播放
        videoPlayer.setOnPreparedListener { mp ->
            mp.isLooping = true
        }

        //camera init
        // 連接前鏡頭
        surfaceView = findViewById(R.id.camera)
        surfaceHolder = surfaceView?.holder
        surfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // 在此处配置相机参数，例如设置摄像头预览尺寸
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                // 打开前置摄像头
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
                camera?.setPreviewDisplay(holder)
                camera?.startPreview()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                // 释放相机资源
                camera?.stopPreview()
                camera?.release()
            }
        })
        //文字轉語音設定
        textToSpeech = TextToSpeech(this, this)
    }
    //文字轉語音用
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // 设置语言
            val result = textToSpeech.setLanguage(Locale.TAIWAN)

            // 检查语音数据是否可用
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 如果语音数据不可用，可以提示用户下载相应的数据
            } else {
                // 文字转语音
                var text = findViewById<TextView>(R.id.guide).text
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        } else {
            // 初始化失败，可以处理错误情况
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 释放TextToSpeech资源
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}