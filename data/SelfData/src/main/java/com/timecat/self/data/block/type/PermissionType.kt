package com.timecat.self.data.block.type

import androidx.annotation.IntDef

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description null
 * @usage null
 */
@IntDef(
    PERMISSION_Meta,
    PERMISSION_Hun,
)
@Retention(AnnotationRetention.SOURCE)
annotation class PermissionType

const val PERMISSION_Meta: Int = 0 // 元权限
const val PERMISSION_Hun: Int = 1 // 混权限
