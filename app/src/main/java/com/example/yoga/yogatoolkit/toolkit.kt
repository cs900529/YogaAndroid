package com.example.yoga.yogatoolkit

import android.content.Context
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt
import com.google.mediapipe.tasks.components.containers.*
import org.json.JSONObject
import java.io.IOException
import com.example.yoga.yogatoolkit.AngleNodeDef.Constants as Constants1

fun computeAngle(point1: List<Float?>, centerPoint: List<Float?>, point2: List<Float?>): Double {
    val (p1_x, pc_x, p2_x) = Triple(
            point1[0]?.toDouble() ?: 0.0,
            centerPoint[0]?.toDouble() ?: 0.0,
            point2[0]?.toDouble() ?: 0.0
    )
    val (p1_y, pc_y, p2_y) = Triple(
            point1[1]?.toDouble() ?: 0.0,
            centerPoint[1]?.toDouble() ?: 0.0,
            point2[1]?.toDouble() ?: 0.0
    )
    val (p1_z, pc_z, p2_z) = Triple(point1.getOrElse(2) { 0.0 }?.toDouble() ?: 0.0,
            centerPoint.getOrElse(2) { 0.0 }?.toDouble() ?: 0.0,
            point2.getOrElse(2) { 0.0 }?.toDouble() ?: 0.0
    )
    /*val (p1_x, pc_x, p2_x) = Triple(point1[0], centerPoint[0], point2[0])
    val (p1_y, pc_y, p2_y) = Triple(point1[1], centerPoint[1], point2[1])
    val (p1_z, pc_z, p2_z) = if (point1.size == centerPoint.size && centerPoint.size == point2.size && point1.size == 3) {
        // 3 dim
        Triple(point1[2], centerPoint[2], point2[2])
    } else {
        // 2 dim
        Triple(0.0, 0.0, 0.0)
    }*/
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
    return Math.toDegrees(acos(cos_b))
}

fun getLandmarks(landmarks: Landmark?, w: Int? = null, h: Int? = null):List<Float?> {
    return if (w == null || h == null)
        listOf(landmarks?.x(),landmarks?.y(),landmarks?.z())
    else
        listOf(landmarks?.x()?.times(w),landmarks?.y()?.times(h),null)
}

fun readJsonFile(context: Context, fileName: String): JSONObject? {
    try {
        // 打开 JSON 文件
        val inputStream = context.assets.open(fileName)

        // 从 InputStream 创建 JSON 字符串
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // 解析 JSON 字符串为 JSONObject
        return JSONObject(jsonString)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}


fun treePoseRule(roi: MutableMap<String,Boolean>,
                 tips : String,
                 sample_angle_dict:JSONObject?,
                 angle_dict: MutableMap<String,Any>,
                 point3d: List<Landmark?>,
                 /*mat:MutableMap<String,Any>*/):String {
    var tip :String = tips
    for ((key,_) in roi ){
        var tip_flag = false
        if (tip == "") tip_flag = true

        /*if mat.point_count == 0:
        tip = "請將腳踩到瑜珈墊中" if tip_flag else tip
        elif mat.point_count >= 2:
        tip = "請將右腳抬起" if tip_flag else tip*/
        if (key == "LEFT_KNEE" || key == "LEFT_HIP"){
            val tolerance_val = 8
            val min_angle = (sample_angle_dict?.get(key) as? Int)?.minus(tolerance_val)
            val max_angle = (sample_angle_dict?.get(key) as? Int)?.plus(tolerance_val)
            if ( (angle_dict[key] as? Int)!! >= min_angle!! && (angle_dict[key] as? Int)!! <= max_angle!!){
                roi[key] = true
            }
            else if ((angle_dict[key] as? Int)!! < min_angle) {
                roi[key] = false
                tip = if (tip_flag) "將左腳打直平均分配雙腳重量，勿將右腳重量全放在左腳大腿" else tip
            }
            else{
                roi[key] = false
                tip = if (tip_flag) "請勿將右腳重量全放在左腳大腿，避免傾斜造成左腳負擔" else tip
            }
        }
        else if (key == "RIGHT_FOOT_INDEX") {
            val foot_y = getLandmarks(point3d[Constants1.RIGHT_FOOT_INDEX])[1]
            val knee_y = getLandmarks(point3d[Constants1.LEFT_KNEE])[1]
            if (foot_y != null) {
                roi[key] = foot_y <= knee_y!!
            }
            if (tip_flag)
                tip = "請將右腳抬至高於左腳膝蓋的位置，勿將右腳放在左腳膝蓋上，\n避免造成膝蓋負擔"
        }

    }
    if (tip == "") {
        tip = "動作正確"
    }

    return tip
}