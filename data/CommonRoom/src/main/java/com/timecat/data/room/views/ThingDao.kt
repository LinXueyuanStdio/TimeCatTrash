package com.timecat.data.room.views

import androidx.room.Dao
import androidx.room.Query
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_RECORD

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/27
 * @description null
 * @usage null
 */
@Dao
interface ThingDao {

    @Query("SELECT * FROM view_thing WHERE (startTime >= :startTime) AND (startTime <= :endTime)")
    fun getRecordBetween(startTime: Long, endTime: Long): MutableList<RoomRecord>

}