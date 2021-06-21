package com.timecat.data.room.views

import androidx.room.*
import com.alibaba.fastjson.JSONObject
import com.timecat.component.setting.CALDAV
import com.timecat.component.setting.REGULAR_EVENT_TYPE_ID
import com.timecat.component.setting.SOURCE_SIMPLE_CALENDAR
import com.timecat.data.room.record.JsonTypeConverter
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.type.BLOCK_RECORD
import com.timecat.identity.data.randomColor
import org.joda.time.DateTime
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/27
 * @description 日历事件
 * @usage null
 */
@DatabaseView(
    viewName = "view_event",
    value = "SELECT records.*, import_export.event_type_id, import_record.attendees, import_record.import_id, import_record.source FROM records LEFT JOIN import_record ON (import_record.record_id = records.id) LEFT JOIN import_export ON (import_export.record_id = records.id) WHERE records.type = $BLOCK_RECORD AND records.subType IN($REMINDER, $HABIT, $GOAL) "
)
data class Event(
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
    var subType: Int = REMINDER,

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
    var attachmentItems: AttachmentTail = AttachmentTail(mutableListOf()),

    @ColumnInfo(name = "event_type_id") var eventType: Long = REGULAR_EVENT_TYPE_ID,
    @ColumnInfo(name = "attendees") var attendees: String = "",//参与者
    @ColumnInfo(name = "import_id") var importId: String = "",//导入的id
    @ColumnInfo(name = "source") var source: String = SOURCE_SIMPLE_CALENDAR //"$CALDAV-$calendarId"
) : ITaskStatus {
    @Ignore
    var startTS: Long = startTime / 1000

    @Ignore
    var endTS: Long = if (totalLength == 0L) 0 else (startTime + totalLength) / 1000
    fun getCalDAVCalendarId() =
        if (source.startsWith(CALDAV)) (source.split("-").lastOrNull() ?: "0").toString().toInt() else 0

    fun getCalDAVEventId(): Long {
        return try {
            (importId.split("-").lastOrNull() ?: "0").toString().toLong()
        } catch (e: NumberFormatException) {
            0L
        }
    }

    fun getRoomRecord(): RoomRecord {
        return RoomRecord(
            id = id,
            name = name,
            title = title,
            content = content,
            uuid = uuid,
            mtime = mtime,
            icon = icon,
            coverImageUrl = coverImageUrl,
            isDummy = isDummy,
            createTime = createTime,
            updateTime = updateTime,
            finishTime = finishTime,
            deleteTime = deleteTime,
            archiveTime = archiveTime,
            pinTime = pinTime,
            lockTime = lockTime,
            blockTime = blockTime,
            startTime = startTime,
            totalLength = totalLength,
            type = type,
            subType = subType,
            lifeCycles = lifeCycles,
            label = label,
            status = status,
            theme = theme,
            color = color,
            miniShowType = miniShowType,
            render_type = render_type,
            tags = tags,
            topics = topics,
            parent = parent,
            ext = ext,
            attachmentItems = attachmentItems
        )
    }

    //region 2. ITaskStatus
    override fun updateStatus(s: Long, yes: Boolean) {
        super.updateStatus(s, yes)
        val now = DateTime().millis
        when (s) {
            TASK_FINISH -> finishTime = now
            TASK_ARCHIVE -> archiveTime = now
            TASK_DELETE -> deleteTime = now
            TASK_PIN -> pinTime = now
            TASK_LOCK -> lockTime = now
            TASK_BLOCK -> blockTime = now
        }
        updateTime = now
    }

    //region Status 用 16 进制管理状态
    /**
     * 往状态集中加一个状态
     * @param status status
     */
    override fun addStatus(status: Long) {
        this.status = this.status or status
    }

    /**
     * 往状态集中移除一个状态
     * @param status status
     */
    override fun removeStatus(status: Long) {
        this.status = this.status and status.inv()
    }

    /**
     * 状态集中是否包含某状态
     * @param status status
     */
    override fun isStatusEnabled(status: Long): Boolean {
        return this.status and status != 0L
    }
    //endregion
    //endregion
}