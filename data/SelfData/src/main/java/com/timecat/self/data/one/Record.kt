package com.timecat.self.data.one

import com.timecat.self.data.block.RecordBlock

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-25
 * @description null
 * @usage null
 */
data class Record(
    var id: Long? = null,
    val objectId: String? = null,
    val block: RecordBlock
) {
}