package com.timecat.data.room.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.timecat.component.setting.SOURCE_SIMPLE_CALENDAR

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/27
 * @description 导入为生日或节日
 * @usage null
 */
@Entity(tableName = "import_record", indices = [(Index(value = ["id"], unique = true))])
data class ImportRecord (
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "record_id") var recordId: Long,
    @ColumnInfo(name = "attendees") var attendees: String = "",//参与者
    @ColumnInfo(name = "import_id") var importId: String = "",//导入的id
    @ColumnInfo(name = "source") var source: String = SOURCE_SIMPLE_CALENDAR
)