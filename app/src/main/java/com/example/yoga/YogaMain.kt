package com.example.yoga

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.SystemClock
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.yoga.databinding.ActivityYogaMainBinding
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class YogaMain : AppCompatActivity() , PoseLandmarkerHelper.LandmarkerListener, TextToSpeech.OnInitListener{
    //拿mediapipe model
    private lateinit var poseLandmarkerHelper: PoseLandmarkerHelper
    private val viewModel : MainViewModel by viewModels()
    //分析圖片
    private var imageAnalyzer: ImageAnalysis? = null
    //databinding 讓xml調用不綁R.id.xxx
    private lateinit var yogamainBinding: ActivityYogaMainBinding
    private var cameraFacing = CameraSelector.LENS_FACING_FRONT
    //開個thread
    private lateinit var backgroundExecutor: ExecutorService
    //前鏡頭
    private var camera: Camera? = null
    //python 物件
    private lateinit var python : Python
    private lateinit var pose     : PyObject
    private lateinit var heatmappy : PyObject
    //文字轉語音
    private lateinit var textToSpeech: TextToSpeech
    //判別文字是否更動用
    private var lastText="提示文字在這"
    //30秒計時器
    private var timer: CountDownTimer? = null
    private var timeLeft_ms: Long = 30000 // 初始計時為30秒
    private var timeLeft_str=""
    //結算分數時間
    private var finishTime = 0.0
    private var score = 99.0
    private val handler = Handler()
    private var baseTime = SystemClock.elapsedRealtime()
    private val finishTimer = object : Runnable {
        override fun run() {
            finishTime = (SystemClock.elapsedRealtime()-baseTime)/1000.0
            handler.postDelayed(this, 100) // update every 0.1 second
        }
    }
    private fun initializeTimer() {
        timer = object : CountDownTimer(timeLeft_ms, 100) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTick(ms_remain: Long) {
                timeLeft_ms = ms_remain
                // 每0.1秒執行一次的邏輯，例如更新 UI 顯示剩餘時間
                timeLeft_str = (ms_remain/1000f).toString()
                //timeLeftBar
                val layoutParams = yogamainBinding.timeLeftBar.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.matchConstraintPercentWidth = 0.47f*(ms_remain/30000f)
                yogamainBinding.timeLeftBar.layoutParams = layoutParams
                var G = 0f
                var R = 0f
                if(ms_remain > 15000){
                    G = 1f
                    R = (30000-ms_remain)/15000f
                }
                else{
                    R = 1f
                    G = ms_remain/15000f
                }
                val barColor = Color.rgb(R,G,0f)
                yogamainBinding.timeLeftBar.backgroundTintList = ColorStateList.valueOf(barColor)
            }
            override fun onFinish() {
                // 計時器倒數完畢時觸發的邏輯
                finishFunction()
            }
        }
    }
    private fun startTimer() {
        initializeTimer()
        // 開始計時器
        timer?.start()
    }
    private fun resetTimer() {
        // 重置計時器為30秒
        timeLeft_ms = 30000
        timeLeft_str = ""
        timer?.cancel()
        timer = null
    }
    private fun finishFunction() {
        // 計時器倒數完畢後的邏輯
        // 在這裡執行你想要的操作
        timeLeft_str = "結束"
        //停止計時
        handler.removeCallbacks(finishTimer)
        // 頁面跳轉
        val intent = Intent(this, YogaResult::class.java).apply {
            putExtra("title" ,yogamainBinding.title.text)
            putExtra("finishTime",finishTime)
            putExtra("score",score)
        }
        startActivity(intent)
    }

    //獲取影片檔案
    private fun getfile(filename: String): Int {
        return when (filename) {
            "Tree Style" -> R.raw.tree_style_show
            "Warrior2 Style" -> R.raw.warrior2_style_show
            "Plank" -> R.raw.plank_show
            "Reverse Plank" -> R.raw.reverse_plank_show
            "Child's pose" -> R.raw.child_show
            "Seated Forward Bend" -> R.raw.seated_forward_bend_show
            "Low Lunge" -> R.raw.low_lunge_show
            "Downward dog" -> R.raw.downward_dog_show
            "Pyramid pose" -> R.raw.pyramid_pose_show
            "Bridge pose" -> R.raw.bridge_show
            else -> R.raw.tree_style
        }
    }
    // Function to get image resource based on poseName
    private fun getDefaultPic(filename: String?): String {
        return when (filename) {
            "Tree Style" -> "TreePose/8"
            "Warrior2 Style" -> "WarriorIIRulePic/8"
            "Plank" -> "PlankPose/10"
            "Reverse Plank" -> "ReversePlankPose/6"
            "Child's pose" -> "ChildsPose/5"
            "Seated Forward Bend" -> "SeatedForwardBendPose/5"
            "Low Lunge" -> "LowLungePose/5"
            "Downward dog" -> "DownwardDogPose/6"
            "Pyramid pose" -> "Pyramidpose/6"
            "Bridge pose" -> "BridgePose/5"
            else -> "TreePose/8"
        }
    }
    // Function to get image resource based on poseName
    private fun getPoseFolder(filename: String?): String {
        return when (filename) {
            "Tree Style" -> "TreePose"
            "Warrior2 Style" -> "WarriorIIRulePic"
            "Plank" -> "PlankPose"
            "Reverse Plank" -> "ReversePlankPose"
            "Child's pose" -> "ChildsPose"
            "Seated Forward Bend" -> "SeatedForwardBendPose"
            "Low Lunge" -> "LowLungePose"
            "Downward dog" -> "DownwardDogPose"
            "Pyramid pose" -> "Pyramidpose"
            "Bridge pose" -> "BridgePose"
            else -> "TreePose"
        }
    }
    private fun setImage(imagePath: String?) {
        val picturePath = findViewById<ImageView>(R.id.guide_picture)
        var am: AssetManager? = null
        am = assets
        val pic = am.open(imagePath.toString())
        // Decode the input stream into a Drawable
        val drawable = Drawable.createFromStream(pic, null)

        // Set the drawable as the image source for the ImageView
        picturePath.setImageDrawable(drawable)

        // Close the input stream when you're done
        pic.close()
    }

    private fun TTSSpeak(str:String){
        textToSpeech.stop()
        textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar

        //啟動python
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        python = Python.getInstance()

        //初始化yogamainBinding
        yogamainBinding = ActivityYogaMainBinding.inflate(layoutInflater)
        setContentView(yogamainBinding.root)

        val poseName = intent.getStringExtra("poseName")
        yogamainBinding.title.text = poseName

        //開始計算完成時間
        handler.post(finishTimer)

        //啟動yogapose
        pose = python.getModule("yogaPoseDetect" ).callAttr("YogaPose",poseName)

        heatmappy = python.getModule("heatmap")

        yogamainBinding.title.text = poseName

        yogamainBinding.back.setOnClickListener {
            textToSpeech.stop()
            // 頁面跳轉
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        //guide_video init
        //val videoPlayer = findViewById<VideoView>(R.id.guide_video)
        /*val videoPath = "android.resource://" + packageName + "/" +  getfile(poseName.toString())
        yogamainBinding.guideVideo.setVideoURI(Uri.parse(videoPath))
        yogamainBinding.guideVideo.start()
        // 设置循环播放
        yogamainBinding.guideVideo.setOnPreparedListener { mp ->
             mp.isLooping = true
             mp.setVolume(0f,0f)
        }*/

        //guide_picture init
        val picturePath = findViewById<ImageView>(R.id.guide_picture)
        var am: AssetManager? = null
        am = assets

        val pic = am.open("image/"+getDefaultPic(poseName)+".jpg")
        // Decode the input stream into a Drawable
        val drawable = Drawable.createFromStream(pic, null)
        // Set the drawable as the image source for the ImageView
        picturePath.setImageDrawable(drawable)
        // Close the input stream when you're done
        pic.close()


        backgroundExecutor = Executors.newSingleThreadExecutor()

        // 初始化 CameraX
        startCamera()

        //設定PoseLandmarkerHelper
        backgroundExecutor.execute {
            poseLandmarkerHelper = PoseLandmarkerHelper(
                //context = requireContext(),
                context = this,
                runningMode = RunningMode.LIVE_STREAM,
                minPoseDetectionConfidence = viewModel.currentMinPoseDetectionConfidence,
                minPoseTrackingConfidence = viewModel.currentMinPoseTrackingConfidence,
                minPosePresenceConfidence = viewModel.currentMinPosePresenceConfidence,
                currentDelegate = viewModel.currentDelegate,
                poseLandmarkerHelperListener = this
            )
        }

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
            }
        } else {
            // 初始化失败，可以处理错误情况
        }
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // 获取 CameraProvider
            val cameraProvider :ProcessCameraProvider = cameraProviderFuture.get()
            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraFacing = cameraManager.cameraIdList[0].toInt()

            // 配置预览
            //val aspectRatio: Rational = Rational(16, 9) // 指定16:9的寬高比
            //val size: Size = Size(aspectRatio.numerator, aspectRatio.denominator)

            val preview :Preview = Preview.Builder()
                //.setTargetResolution(size)
                .build()
                .also {
                    it.setSurfaceProvider(yogamainBinding.camera.getSurfaceProvider())
                }

            // 配置相机选择器
            val cameraSelector : CameraSelector =
                CameraSelector.Builder().requireLensFacing(cameraFacing).build()

            // ImageAnalysis. Using RGBA 8888 to match how our models work
            imageAnalyzer =
                ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .setTargetRotation(yogamainBinding.camera.display.rotation)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                    .build()
                    // The analyzer can then be assigned to the instance
                    .also {
                        it.setAnalyzer(backgroundExecutor) { image ->
                            detectPose(image)
                        }
                    }

            // 绑定相机和预览
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview , imageAnalyzer
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun detectPose(imageProxy: ImageProxy) {
        if(this::poseLandmarkerHelper.isInitialized) {
            poseLandmarkerHelper.detectLiveStream(
                imageProxy = imageProxy,
                //isFrontCamera = cameraFacing == CameraSelector.LENS_FACING_FRONT
                isFrontCamera = cameraFacing >= 0
            )
        }
    }

    private fun readBytesFromFile(filePath: String): ByteArray? {
        var fileBytes: ByteArray? = null
        try {
            val file = File(filePath)
            val fis = FileInputStream(file)
            fileBytes = fis.readBytes()
            fis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return fileBytes
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation =
            yogamainBinding.camera.display.rotation
    }
    // Update UI after pose have been detected. Extracts original
    // image height/width to scale and place the landmarks properly through
    // OverlayView
    override fun onResults(
        resultBundle: PoseLandmarkerHelper.ResultBundle
    ) {
        this.runOnUiThread {
            val heatmapexecutor : ExecutorService =Executors.newSingleThreadExecutor()
            thread{
                heatmapexecutor.execute {
                    val fileName = "yourFile.txt"
                    val file = File(this.filesDir, fileName)
                    val filePath = file.path

                    val readByteArray = readBytesFromFile(filePath)

                    // 檢查 ByteArray 是否為空
                    if ((readByteArray != null) && readByteArray.isNotEmpty()) {
                        val bmp: Bitmap? =
                            BitmapFactory.decodeByteArray(readByteArray, 0, readByteArray.size)

                        // 檢查解碼的 Bitmap 是否為空
                        if (bmp != null) {
                            runOnUiThread{yogamainBinding.imageView2.setImageBitmap(bmp)}
                        } else {
                            // 處理解碼失敗的情況
                            Log.e("BitmapFactory", "Failed to decode ByteArray to Bitmap")
                        }
                    } else {
                        // 處理空的 ByteArray 情況
                        Log.e("BitmapFactory", "ByteArray is null or empty")
                    }
                }
            }


            // Pass necessary information to OverlayView for drawing on the canvas
            yogamainBinding.overlay.setResults(
                    resultBundle.results.first(),
                    resultBundle.inputImageHeight,
                    resultBundle.inputImageWidth,
                    RunningMode.LIVE_STREAM
            )
            // pass result to Yogapose
            if(resultBundle.results.first().worldLandmarks().isNotEmpty()){
                val floatListList: List<List<Float>> = resultBundle.results.first().worldLandmarks().flatMap { landmarks ->
                    landmarks.map { landmark ->
                        listOf(landmark.x(), landmark.y(), landmark.z())
                    }
                }

                var guideStr = pose.callAttr("detect", floatListList , heatmappy.callAttr("get_rects") ,
                        heatmappy.callAttr("get_center")).toString()


                if (guideStr.iterator().hasNext()) {
                    val re=guideStr.split(',')
                    val re_0=re[0].length
                    val re_1=re[1].length
                    println(re[0].substring(2..re_0-2))
                    println(re[1].substring(2..re_1-3))
                    // Assume it behaves like a list and try accessing its elements
                    try {
                        yogamainBinding.guide.text = re[0].substring(2..re_0-2)
                        if (lastText != yogamainBinding.guide.text)
                            TTSSpeak(yogamainBinding.guide.text.toString())
                        lastText = yogamainBinding.guide.text.toString()

                        val imagePath = re[1].substring(2..re_1-3)
                        setImage(imagePath)

                    } catch (e: Exception) {
                        // Handle exceptions when accessing elements if the result doesn't behave like a list
                        println("Result does not have expected list behavior: ${e.message}")
                    }
                } else {
                    // Handle cases where the result does not behave like an iterable (e.g., not a list)
                    println("Result is not iterable like a list")
                }
                //var guideStr = "動作正確"
                /*yogamainBinding.guide.text = guideStr
                if (lastText != guideStr)
                    TTSSpeak(guideStr)
                lastText = guideStr
                */

                //30秒計時器
                if (lastText.contains("動作正確")){
                    if (timer == null)
                        startTimer()
                    yogamainBinding.guide.text = lastText + " " + timeLeft_str
                }
                else
                    resetTimer()
            }


            // Force a redraw
            yogamainBinding.overlay.invalidate()
        }
    }
    override fun onError(error: String, errorCode: Int) {
        this.runOnUiThread {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            if (errorCode == PoseLandmarkerHelper.GPU_ERROR) {
                viewModel.setDelegate(0)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        //關掉相機
        backgroundExecutor.shutdown()
        // 释放TextToSpeech资源
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}