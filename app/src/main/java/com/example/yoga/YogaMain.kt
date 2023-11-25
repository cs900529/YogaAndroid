package com.example.yoga

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.ImageView
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
    private lateinit var pose   : PyObject

    //文字轉語音
    private lateinit var textToSpeech: TextToSpeech
    //判別文字是否更動用
    public var lastText="提示文字在這"

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
        //啟動yogapose
        pose = python.getModule("yogaPoseDetect" ).callAttr("YogaPose",poseName)

        yogamainBinding.title.text = poseName



        //val back_button = findViewById<ImageButton>(R.id.back)
        yogamainBinding.back.setOnClickListener {
            textToSpeech.stop()
            // 頁面跳轉
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        /*//guide_video init
        //val videoPlayer = findViewById<VideoView>(R.id.guide_video)
        val videoPath = "android.resource://" + packageName + "/" +  getfile(poseName.toString())
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
        val pic = am.open("images/"+getDefaultPic(poseName)+".jpg")
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
                //var text = yogamainBinding.guide.text
                //textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
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
            if(resultBundle.results.first().worldLandmarks().isNotEmpty()) {
                val floatListList: List<List<Float>> =
                    resultBundle.results.first().worldLandmarks().flatMap { landmarks ->
                        landmarks.map { landmark ->
                            listOf(landmark.x(), landmark.y(), landmark.z())
                        }
                    }

                /*
                yogamainBinding.guide.text = pose.callAttr("detect", floatListList, 0).toString()
                if (lastText != yogamainBinding.guide.text)
                    TTSSpeak(yogamainBinding.guide.text.toString())
                lastText = yogamainBinding.guide.text.toString()
                */

                val retult = pose.callAttr("detect", floatListList, 0).toString()
                // Check if the result can be iterated over (assumed behavior of a list)
                if (retult.iterator().hasNext()) {
                    // Assume it behaves like a list and try accessing its elements
                    try {
                        yogamainBinding.guide.text = retult[0].toString()
                        if (lastText != yogamainBinding.guide.text)
                            TTSSpeak(yogamainBinding.guide.text.toString())
                        lastText = yogamainBinding.guide.text.toString()

                        val imagePath = retult[1].toString()
                        setImage(imagePath)

                    } catch (e: Exception) {
                        // Handle exceptions when accessing elements if the result doesn't behave like a list
                        println("Result does not have expected list behavior: ${e.message}")
                    }
                } else {
                    // Handle cases where the result does not behave like an iterable (e.g., not a list)
                    println("Result is not iterable like a list")
                }
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