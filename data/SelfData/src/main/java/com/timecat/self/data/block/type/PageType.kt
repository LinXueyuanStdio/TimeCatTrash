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
    PAGE_Record,
    PAGE_MD,
    PAGE_ORG
)
@Retention(AnnotationRetention.SOURCE)
annotation class PageType

const val PAGE_Record: Int = BLOCK_RECORD
const val PAGE_MD: Int = 1 //指向文件，文件格式为 markdown，预览是文件路径
const val PAGE_ORG: Int = 2 // 指向文件，文件格式为 org，先查询数据库对象，再查询文件，预览是文件路径
