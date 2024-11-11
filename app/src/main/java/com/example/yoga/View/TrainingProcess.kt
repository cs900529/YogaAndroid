//package com.example.yoga.View
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import com.example.yoga.ViewModel.TrainingMenuViewModel
//import com.example.yoga.databinding.ActivityTrainingMenuBinding
//
//class TrainingProcess : AppCompatActivity() {
//
//    private lateinit var trainingProcessBinding: ActivityTrainingMenuBinding
//    private lateinit var viewModel: TrainingProcessViewModel
//
//    private val restIntervalResultLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                // 當休息時間結束，繼續進行下一個動作
//                if (viewModel.isTrainingComplete()) {
//                    completeTraining() // 所有動作完成後，顯示結果頁面
//                } else {
//                    viewModel.nextAction() // 更新到下一個動作
//                    startAction(viewModel.startAction())
//                }
//            }
//        }
//
//    // 執行當前動作
//    private fun startAction(poseName: String) {
//        Log.d("開始動作: ", "$poseName")
//        val intent = Intent(this, VideoGuide::class.java)
//        intent.putExtra("poseName", poseName)
//        intent.putExtra("currentIndex", viewModel.currentIndex.value ?: 0) // 傳遞當前索引
//        intent.putExtra("mode", viewModel.mode.value) // 傳遞模式
//        restIntervalResultLauncher.launch(intent)
//    }
//
//    // 完成訓練，跳轉到 YogaResult 頁面
//    private fun completeTraining() {
//        val intent = Intent(this, YogaResult::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        trainingProcessBinding = ActivityTrainingMenuBinding.inflate(layoutInflater)
//        setContentView(trainingProcessBinding.root)
//        supportActionBar?.hide()
//
//        // 初始化 ViewModel
//        viewModel = ViewModelProvider(this).get(TrainingProcessViewModel::class.java)
//
//        // 從 Intent 中獲取初始數據
//        val menuName = intent.getStringExtra("menuName").toString()
//        val mode = intent.getStringExtra("mode").toString()
//
//        // 設定 ViewModel 的數據
//        viewModel.mode.value = mode
//        viewModel.menuName.value = menuName
//        viewModel.setPoseList(menuName)
//
//        // 開始第一個動作
//        startAction(viewModel.startAction())
//    }
//}
