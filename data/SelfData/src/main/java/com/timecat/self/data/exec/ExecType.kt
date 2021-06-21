package com.timecat.self.data.exec

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-15
 * @description null
 * @usage null
 */
@IntDef(
    EXEC_Recommend,
    EXEC_Report
)
@Retention(AnnotationRetention.SOURCE)
annotation class ExecType

const val EXEC_Recommend = 0
const val EXEC_Report = 1