package com.timecat.self.data

import android.graphics.Color
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/22
 * @description 颜色相关
 * @usage null
 */

/**
 * 随机的好看颜色
 */
fun randomColor(): Int {
    return Color.HSVToColor(floatArrayOf(Random().nextInt(360).toFloat(), 0.5f, 0.9f))
}