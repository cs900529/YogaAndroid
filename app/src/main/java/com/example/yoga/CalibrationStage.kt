package com.example.yoga

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.yoga.databinding.ActivityCalibrationStageBinding
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CalibrationStage : AppCompatActivity() , PoseLandmarkerHelper.LandmarkerListener, TextToSpeech.OnInitListener{
    //databinding 讓xml調用不綁R.id.xxx
    private lateinit var CalibrationStageBinding: ActivityCalibrationStageBinding
    //拿mediapipe model
    private lateinit var poseLandmarkerHelper: PoseLandmarkerHelper
    private val viewModel : MainViewModel by viewModels()
    //分析圖片
    private var imageAnalyzer: ImageAnalysis? = null
    //前鏡頭
    private var camera: Camera? = null
    //private var surfaceView: SurfaceView? = null
    //private var surfaceHolder: SurfaceHolder? = null
    private var cameraFacing = CameraSelector.LENS_FACING_FRONT
    //開個thread
    private lateinit var backgroundExecutor: ExecutorService
    //文字轉語音
    private lateinit var textToSpeech: TextToSpeech
    private var lastspeak:String = ""
    lateinit var global: GlobalVariable
    private var delate_count : Int = 0
    private lateinit var mediaPlayer: MediaPlayer

    // yogamap next
    private lateinit var python : Python
    private lateinit var heatmapNext : PyObject
    private var nextThread: Thread? = null

    fun nextpage(){
        global.currentMS = mediaPlayer.currentPosition
        mediaPlayer.stop()
        textToSpeech.stop()
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar
        setContentView(R.layout.activity_calibration_stage)
        //文字轉語音設定
        textToSpeech = TextToSpeech(this, this)
        CalibrationStageBinding = ActivityCalibrationStageBinding.inflate(layoutInflater)
        setContentView(CalibrationStageBinding.root)
        CalibrationStageBinding.finish.setOnClickListener {
            nextpage()
            try {
                nextThread?.interrupt()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }



        //// 連接前鏡頭
        //surfaceView = findViewById(R.id.camera)
        //surfaceHolder = surfaceView?.holder
        //surfaceHolder?.addCallback(object : SurfaceHolder.Callback {
        //    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        //        // 在此处配置相机参数，例如设置摄像头预览尺寸
        //    }
//
        //    override fun surfaceCreated(holder: SurfaceHolder) {
        //        // 打开前置摄像头
        //        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
        //        camera?.setPreviewDisplay(holder)
        //        camera?.startPreview()
        //    }
//
        //    override fun surfaceDestroyed(holder: SurfaceHolder) {
        //        // 释放相机资源
        //        camera?.stopPreview()
        //        camera?.release()
        //    }
        //})
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


        global = application as GlobalVariable
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.isLooping = true // 設定音樂循環播放
        mediaPlayer.seekTo(global.currentMS)
        mediaPlayer.start()

        //啟動python
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        python = Python.getInstance()

        // yogamap return
        heatmapNext = python.getModule("heatmap")

        // yogamap return
        nextThread = Thread {
            try {
                Thread.sleep(2000)
                while (!heatmapNext.callAttr("checkReturn").toBoolean()) {
                    Thread.sleep(100)
                }
                runOnUiThread {
                    nextpage()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        nextThread?.start()

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
                    it.setSurfaceProvider(CalibrationStageBinding.camera.getSurfaceProvider())
                }

            // 配置相机选择器
            val cameraSelector : CameraSelector =
                CameraSelector.Builder().requireLensFacing(cameraFacing).build()

            // ImageAnalysis. Using RGBA 8888 to match how our models work
            imageAnalyzer =
                ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .setTargetRotation(CalibrationStageBinding.camera.display.rotation)
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
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation =
            CalibrationStageBinding.camera.display.rotation
    }
    override fun onResults(
        resultBundle: PoseLandmarkerHelper.ResultBundle
    ){
        if(delate_count == 0){
            delate_count++
            this.runOnUiThread{
                if (resultBundle.results.first().landmarks().isNotEmpty()){
                    val count_node = resultBundle.results.first().landmarks()
                        .flatMap { nlandmarks ->
                            nlandmarks.map {
                                mutableListOf( it.visibility().orElse((-1.0).toFloat()).toFloat())
                            }
                        }
                        .count { it[0] > 0.5 }
                    println(count_node)
                    if (count_node < 16) //node 總數32, 先假設1/2沒偵測到,讓使用者調整人至鏡頭裡
                    {
                        lastspeak = CalibrationStageBinding.guide.text.toString()
                        CalibrationStageBinding.guide.text = "請將人調整至鏡頭裡以完成校正"
                        if (lastspeak != CalibrationStageBinding.guide.text) {
                            textToSpeech.stop()
                            textToSpeech.speak("請將人調整至鏡頭裡以完成校正",TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    }
                    else if (count_node > 16){
                        lastspeak = CalibrationStageBinding.guide.text.toString()
                        CalibrationStageBinding.guide.text = "完成校正"
                        if (lastspeak != CalibrationStageBinding.guide.text) {
                            textToSpeech.stop()
                            textToSpeech.speak("完成校正",TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    }
                }
            }
        }
        else if (delate_count == 5)
            delate_count=0
    }
    override fun onDestroy() {
        super.onDestroy()
        // 释放TextToSpeech资源
        textToSpeech.stop()
        textToSpeech.shutdown()
        //關掉相機
        backgroundExecutor.shutdown()
    }
    override fun onError(error: String, errorCode: Int) {
        this.runOnUiThread {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            if (errorCode == PoseLandmarkerHelper.GPU_ERROR) {
                viewModel.setDelegate(0)
            }
        }
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