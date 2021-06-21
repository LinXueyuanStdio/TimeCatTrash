package com.timecat.component.bmob.dao.block

import com.timecat.component.bmob.data.common.Block
import com.timecat.identity.data.block.type.BLOCK_TOPIC
import com.timecat.identity.data.service.OnFindListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/15
 * @description null
 * @usage null
 */
object TopicDao {
    fun getAll(listener: OnFindListener<Block>? = null) {
        BlockDao.findAll(listOf(BLOCK_TOPIC), null, null, listener)
    }
}