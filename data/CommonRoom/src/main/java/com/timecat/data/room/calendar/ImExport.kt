package com.timecat.data.room.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/27
 * @description RoomRecord 和 导入的系统日历 绑定
 * @usage null
 */
@Entity(tableName = "import_export", indices = [(Index(value = ["id"], unique = true))])
data class ImExport (
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "event_type_id") var eventTypeId: Long,
    @ColumnInfo(name = "record_id") var recordId: Long
)