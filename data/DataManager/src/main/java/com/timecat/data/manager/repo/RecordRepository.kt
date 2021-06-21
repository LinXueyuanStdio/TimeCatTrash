package com.timecat.data.manager.repo

import androidx.lifecycle.LiveData
import com.timecat.data.room.TimeCatRoomDatabase
import com.timecat.data.room.record.RoomRecord

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-25
 * @description
 * 这里只负责对各个数据源的访问顺序，具体的访问由底一层实现
 * RecordBlockItem Block
 * @usage null
 */
class RecordRepository(private val db: TimeCatRoomDatabase) {

    fun getRecordLiveData(id: String): LiveData<RoomRecord> {
        return db.recordDao().getLiveData(id)
    }

    fun getRecord(id: String): RoomRecord? {
        return db.recordDao().getByUuid(id)
    }

    fun getAll(): List<RoomRecord> {
        return db.recordDao().getAll_BLOCK_RECORD()
    }

    fun save(record:RoomRecord){
        db.recordDao().update(record)
    }
}