package com.timecat.self.data.base

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-17
 * @description null
 * @usage null
 */
@IntDef(
    NOTE,
    REMINDER,
    HABIT,
    GOAL
)
@Retention(AnnotationRetention.SOURCE)
annotation class Type

const val NOTE: Int = 0
const val REMINDER: Int = 1 //有时间 有提醒
const val HABIT: Int = 2//有循环时间 有提醒
const val GOAL: Int = 3//有时间 有长提醒
const val ALL_RECORD_TYPE: Int = 5
