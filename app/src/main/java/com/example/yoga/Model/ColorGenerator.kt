package com.example.yoga.Model

import android.graphics.Color

class ColorGenerator {
    //隨著時間 Green -> Yellow -> Red
    fun G2Y2R(currentMS:Long,maxMS:Long):Int {
        var G = 0f
        var R = 0f
        if (currentMS > maxMS/2) {
            G = 1f
            R = (maxMS - currentMS) / (maxMS/2f)
        } else {
            R = 1f
            G = currentMS / (maxMS/2f)
        }
        return Color.rgb(R, G, 0f)
    }
}