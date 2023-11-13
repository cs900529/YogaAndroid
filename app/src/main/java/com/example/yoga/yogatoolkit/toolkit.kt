package com.example.yoga.yogatoolkit

import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.google.mediapipe.tasks.components.containers.*
fun computeAngle(point1: List<Double>, centerPoint: List<Double>, point2: List<Double>): Double {
    val (p1_x, pc_x, p2_x) = Triple(point1[0], centerPoint[0], point2[0])
    val (p1_y, pc_y, p2_y) = Triple(point1[1], centerPoint[1], point2[1])

    val (p1_z, pc_z, p2_z) = if (point1.size == centerPoint.size && centerPoint.size == point2.size && point1.size == 3) {
        // 3 dim
        Triple(point1[2], centerPoint[2], point2[2])
    } else {
        // 2 dim
        Triple(0.0, 0.0, 0.0)
    }

    // vector
    val x1 = p1_x - pc_x
    val y1 = p1_y - pc_y
    val z1 = p1_z - pc_z

    val x2 = p2_x - pc_x
    val y2 = p2_y - pc_y
    val z2 = p2_z - pc_z

    // angle
    val cos_b = (x1 * x2 + y1 * y2 + z1 * z2) /
            (sqrt(x1.pow(2) + y1.pow(2) + z1.pow(2)) * sqrt(x2.pow(2) + y2.pow(2) + z2.pow(2)))
    val B = Math.toDegrees(acos(cos_b))
    return B
}

fun getLandmarks(landmarks: Landmark?, w: Int? = null, h: Int? = null):Triple<Float?,Float?,Float?> {
    if (w == null || h == null)
        return Triple(landmarks?.x(),landmarks?.y(),landmarks?.z())
    else
        return Triple(landmarks?.x()?.times(w),landmarks?.y()?.times(h),null)
}