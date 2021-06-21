package com.timecat.data.room.views

import androidx.room.*
import com.alibaba.fastjson.JSONObject
import com.timecat.data.room.record.JsonTypeConverter
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.type.BLOCK_CONTAINER
import com.timecat.identity.data.block.type.BLOCK_RECORD
import com.timecat.identity.data.randomColor
import org.joda.time.DateTime
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/27
 * @description null
 * @usage null
 */
@DatabaseView(
    viewName = "view_book",
    value = "SELECT * FROM records WHERE type = $BLOCK_CONTAINER"
)
data class Book (
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    var name: String = "",
    var title: String = "",
    var content: String = "",
    var uuid: String = UUID.randomUUID().toString(),

    /**
     * modified time 修改时间
     */
    var mtime: Long? = null,
    var icon: String = "R.drawable.ic_notes_hint_24dp",
    var coverImageUrl: String? = "R.drawable.ic_notes_hint_24dp",

    @ColumnInfo(name = "is_dummy")
    var isDummy: Boolean = false,

    @LiveRecordType
    var type: Int = BLOCK_RECORD,
    var subType: Int = NOTE,

    // TaskHeader
    var createTime: Long = DateTime().millis,
    var updateTime: Long = createTime,
    var finishTime: Long = createTime,
    var deleteTime: Long = createTime,
    var archiveTime: Long = createTime,
    var pinTime: Long = createTime,
    var lockTime: Long = createTime,
    var blockTime: Long = createTime,
    var startTime: Long = createTime,
    var totalLength: Long = LifeCycle.totalLengthFromNow2endOfToday(createTime),
    @Embedded(prefix = "lifeCycles_")
    var lifeCycles: LifeCycle = LifeCycle.now2endOfToday(),
    @TaskLabel
    var label: Int = TASK_LABEL_NOT_IMPORTANT_NOT_URGENT,
    var status: Long = TASK_MODE_INIT, //状态集

    //NoteBody
    var theme: Int = 0,
    var color: Int = randomColor(),
    @RoomRecord.Companion.ShowType
    var miniShowType: Int = RoomRecord.FOLD,
    @RenderType
    var render_type: Int = RENDER_TYPE_Record,//渲染类型

    var order: Int = 0,
    var tags: String = "",
    var topics: String = "",
    var parent: String = "",

    @Embedded(prefix = "ext_")
    @TypeConverters(JsonTypeConverter::class)
    var ext: Json = Json(JSONObject()),
    @Embedded(prefix = "attachmentItems_")
    var attachmentItems: AttachmentTail = AttachmentTail(mutableListOf())
)