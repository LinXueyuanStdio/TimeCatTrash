package com.timecat.data.room.container

import com.timecat.data.room.RoomClient
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.BLOCK_APP_WebApp

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-05
 * @description 容器符文
 * @usage null
 */
object RoomContainer {
    @JvmStatic
    fun collectUrl(title: String, url: String) {
        val record = RoomRecord.forName(title)
        record.content = url
        record.type = BLOCK_APP_WebApp
        RoomClient.recordDao().insert(record)
    }
}