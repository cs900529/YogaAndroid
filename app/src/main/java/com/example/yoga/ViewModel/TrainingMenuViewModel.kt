package com.example.yoga.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class TrainingMenuViewModel(application: Application) :  AndroidViewModel(application) {

    val currentIndex = MutableLiveData<Int>(0) // 當前姿勢索引
    val poseList = MutableLiveData<Array<String>>()
    // 訓練流程列表
    val processListMap = mapOf(
//        "早晨喚醒流" to arrayOf("Tree Style", "Warrior2 Style", "Plank"),
        "早晨喚醒流" to arrayOf("Mountain Pose", "Warrior2 Style", "Triangle pose", "Low Lunge", "Downward dog", "Child's Pose"),
        "全身強化訓練" to arrayOf("Tree Style", "Plank", "Cobra pose", "Boat pose", "Bridge pose", "Seated Forward Bend"),
        "平衡與穩定練習" to arrayOf("Locust pose", "Pigeon pose", "Half Moon pose", "Reverse Plank", "Pyramid pose", "Chair pose"),
        "中心強化流" to arrayOf("Mountain pose", "Tree Style", "Warrior2 Style", "Triangle pose", "Downward dog", "Child's pose"),
        "柔軟與伸展" to arrayOf("Locust pose", "Cobra pose", "Camel pose", "Fish pose", "Low Lunge", "Bridge pose"),
        "地獄核心訓練" to arrayOf("Plank", "Boat pose", "Reverse Plank", "Low Lunge", "Chair pose", "Bridge pose")
    )

    // 根據菜單名稱設置 poseList
    fun setPoseList(menuName: String) {
        poseList.value = processListMap[menuName]
        currentIndex.value = 0 // 重設為第一個動作
    }

    // 開始下一個動作
    fun nextAction() {
        // 避免 index 超過列表範圍
        val current = currentIndex.value ?: 0
        if (current < (poseList.value?.size ?: 0) - 1) {
            currentIndex.value = current + 1
        } else {
            completeTraining() // 執行完成訓練邏輯
        }
    }

    // 完成訓練
    fun completeTraining() {
        // 可以在這裡執行完成訓練的邏輯
        currentIndex.value = 0
    }
}
