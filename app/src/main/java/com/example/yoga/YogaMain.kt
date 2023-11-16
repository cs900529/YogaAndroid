package com.example.yoga

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
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
import com.example.yoga.databinding.ActivityYogaMainBinding
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


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
    //private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var python : Python

    private lateinit var pyObject : PyObject
    private lateinit var pose     : PyObject

    //文字轉語音
    private lateinit var textToSpeech: TextToSpeech

    //獲取影片檔案
    private fun getfile(context: Context, filename: String): Int {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // 隐藏title bar

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        python = Python.getInstance()

        //初始化yogamainBinding
        yogamainBinding = ActivityYogaMainBinding.inflate(layoutInflater)
        setContentView(yogamainBinding.root)

        val poseName = intent.getStringExtra("poseName")
        pose = python.getModule("yogaPoseDetect" ).callAttr("YogaPose",poseName)
        //pose = pyObject.callAttr("YogaPose",poseName).toJava(YogaPose::class.java)
        yogamainBinding.title.text = poseName

        //監聽guide 當文字改變時會重新念語音
        //val guide = findViewById<TextView>(R.id.guide)
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
        yogamainBinding.guide.addTextChangedListener(textWatcher)


        //val back_button = findViewById<ImageButton>(R.id.back)
        yogamainBinding.back.setOnClickListener {
            // 頁面跳轉
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        //guide_video init
        //val videoPlayer = findViewById<VideoView>(R.id.guide_video)
        val videoPath = "android.resource://" + packageName + "/" +  getfile(this, poseName.toString() )
        yogamainBinding.guideVideo.setVideoURI(Uri.parse(videoPath))
        yogamainBinding.guideVideo.start()
        // 设置循环播放
        yogamainBinding.guideVideo.setOnPreparedListener { mp ->
            mp.isLooping = true
        }

        //camera init
        // 連接前鏡頭
        /*surfaceView = findViewById(R.id.camera)
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
        })*/

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
                var text = yogamainBinding.guide.text
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
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
            val preview :Preview = Preview.Builder()
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

                //preview?.setSurfaceProvider(surfaceView.getSurfaceProvider())
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
        this?.runOnUiThread {
            if (yogamainBinding != null) {

                // Pass necessary information to OverlayView for drawing on the canvas
                yogamainBinding.overlay.setResults(
                    resultBundle.results.first(),
                    resultBundle.inputImageHeight,
                    resultBundle.inputImageWidth,
                    RunningMode.LIVE_STREAM
                )
                // pass result to Yogapose
                //yogamainBinding.guide.text =
                    //pose.getMediapipeResult(resultBundle.results.first().worldLandmarks().first())
                if(resultBundle.results.first().worldLandmarks().isNotEmpty()){
                    val floatListList: List<List<Float>> = resultBundle.results.first().worldLandmarks().flatMap { landmarks ->
                        landmarks.map { landmark ->
                            listOf(landmark.x(), landmark.y(), landmark.z())
                        }
                    }

                    yogamainBinding.guide.text = pose.callAttr("detect",floatListList , 0).toString()
                }

                //resultBundle.results.last()


                // Force a redraw
                yogamainBinding.overlay.invalidate()
            }
        }
    }
    override fun onError(error: String, errorCode: Int) {
        this?.runOnUiThread {
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