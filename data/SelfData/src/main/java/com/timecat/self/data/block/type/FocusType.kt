package com.timecat.self.data.block.type

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-23
 * @description BLOCK_PAGE 类型的 subType
 * @usage null
 */

@IntDef(
    FOCUS_Pomodoro,
    FOCUS_Plant
)
@Retention(AnnotationRetention.SOURCE)
annotation class FocusType

const val FOCUS_Pomodoro: Int = 0 //番茄钟
const val FOCUS_Plant: Int = 1 //种树
