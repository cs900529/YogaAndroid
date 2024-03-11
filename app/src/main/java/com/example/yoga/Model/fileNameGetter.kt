package com.example.yoga.Model

import android.content.Context
import com.example.yoga.R

class fileNameGetter {
    //獲取影片檔案
     fun getfile(context: Context, filename: String): Int {
        return when (filename) {
            "Tree Style" -> R.raw.tree_style
            "Warrior2 Style" -> R.raw.warrior2_style
            "Plank" -> R.raw.plank
            "Reverse Plank" -> R.raw.reverse_plank
            "Child's pose" -> R.raw.childspose_teacher
            "Seated Forward Bend" -> R.raw.seatedforwardbend_teacher
            "Low Lunge" -> R.raw.lowlunge_teacher
            "Downward dog" -> R.raw.downwarddog_teacher
            "Pyramid pose" -> R.raw.pyramid_teacher
            "Bridge pose" -> R.raw.bridge_teacher
            else -> R.raw.tree_style
        }
    }
    // Function to get image resource based on poseName
    fun getDefaultPic(filename: String?): String {
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
    fun getPoseFolder(filename: String?): String {
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
}