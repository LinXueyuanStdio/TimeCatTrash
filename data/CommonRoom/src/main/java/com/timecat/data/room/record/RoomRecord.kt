package com.timecat.data.room.record

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.IntDef
import androidx.room.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.timecat.data.room.message.EMCallbackHolder
import com.timecat.data.room.message.RoomCallBack
import com.timecat.data.room.message.Status
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.type.*
import com.timecat.identity.data.randomColor
import org.joda.time.DateTime
import java.io.Serializable
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-29
 * @description null
 * @usage null
 */
@Entity(
    tableName = "records",

    indices = [
        Index("uuid", unique = true)
    ]
)
data class RoomRecord(
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
    @ColumnInfo(defaultValue = "R.drawable.ic_notes_hint_24dp")
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
    @ShowType
    var miniShowType: Int = FOLD,
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

) : Serializable, Parcelable,
    ITaskStatus, ITaskLabel, ITaskLifeCycle, IAttachmentTail, IJson {

    override fun toString(): String {
        return "#$id ($type, $subType) $title / $uuid --> $parent\n"
    }

    //region 0. extension

    /**
     * Returns either notebook's #+TITLE or file name.
     */
    fun getFragmentTitle(): String = title

    fun getFragmentSubTitle(): String = title

    /**
     * id of tag, id is the name of tag
     */
    var tagsIdList: List<String>
        get() = tags.split(",")
        set(value) {
            tags = value.joinToString(",")
        }

    fun setShortTitle(longText: String) {
        var title: String = longText.substringBefore("\n")
        if (title.length > 20) title = title.substring(0, 20)
        this.title = title
    }

    @Ignore
    var selected: Boolean = false

    @Ignore
    var thingState: TaskStatus = TaskStatus(status)
    //endregion

    //region 1. ITaskLabel
    override fun taskLabelColor(): Int = taskLabelColor[label]

    override fun taskLabelBackgroundColor(): Int = taskBackgroundColor[label]

    override fun taskLabelColorStr(): String = taskLabelColorStr[label]

    override fun taskLabelStr(): String = taskLabelStr[label]

    override fun taskLabelBackgroundColorStr(): String = taskLabelBackgroundColorStr[label]

    override fun setNextLabel() {
        label = (label + 1) % 4
    }
    //endregion

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

    //region 3. ILifeCycle
    override fun getLifeCycle(): LifeCycle = lifeCycles

    fun status(): Pair<TaskState, Long> {
        val state: TaskState
        val time: Long
        val curDate = DateTime()
        val beginDate = lifeCycles.beginTs().toDateTime()
        val endDate = lifeCycles.endTs().toDateTime()
        val curTs: Long = curDate.millis
        //默认 checkable 为 true
        if (isFinished()) {
            state = TaskState.FINISH
            time = finishTime
        } else {
            //判断 1 等待，2 进行中，3 延期
            when {
                curTs < beginDate.millis -> {
                    state = TaskState.WAITING
                    time = beginDate.millis - curTs
                }
                curTs <= endDate.millis -> {
                    state = TaskState.DOING
                    time = 0
                }
                else -> {
                    state = TaskState.DELAY
                    val during: Long = curTs - endDate.millis
                    time = during / (1000 * 60 * 60 * 24)
                }
            }
        }
        return Pair(state, time)
    }

    fun beginTs(): Long = startTime

    fun endTs(): Long = if (totalLength == 0L) 0 else startTime + totalLength

    @Ignore
    var startTS: Long = startTime / 1000

    @Ignore
    var endTS: Long = if (totalLength == 0L) 0 else (startTime + totalLength) / 1000
    //endregion

    //region 4. AttachmentTail
    override fun removeAllAttachment() = attachmentItems.attachmentItems.clear()

    override fun isAll(type: Int): Boolean = attachmentItems.isAll(type)

    override fun add(type: Int, a: String) = attachmentItems.add(type, a)

    override fun remove(type: Int, a: String) = attachmentItems.remove(type, a)

    override fun update(type: Int, a: String) = attachmentItems.update(type, a)

    override fun get(type: Int): MutableList<String> = attachmentItems.get(type)

    override fun size(): Int = attachmentItems.size()

    override fun isEmpty(): Boolean = attachmentItems.isEmpty()
    //endregion

    //region 5. Attribute
    /**
     * \~chinese
     * 设置消息的boolean 类型扩展属性
     * @param attribute 属性名
     * @param value, 属性值
     *
     * \~english
     * set a boolean type extra attributes of the message
     * @param attribute attribute key
     * @param value attribute value
     */
    fun setAttribute(attribute: String, value: Boolean) {
        if (attribute.isEmpty()) {
            return
        }
        extension[attribute] = value
    }

    /**
     * \~chinese
     * 设置消息的int 类型扩展属性
     * @param attribute 属性名
     * @param value, 属性值
     *
     * \~english
     * set a int type extra attributes of the message
     * @param attribute attribute key
     * @param value attribute value
     */
    fun setAttribute(attribute: String, value: Int) {
        if (attribute.isEmpty()) {
            return
        }
        extension[attribute] = value
    }

    /**
     * \~chinese
     * 设置消息的long 类型扩展属性
     * @param attribute 属性名
     * @param value, 属性值
     *
     * \~english
     * set a long type extra attributes of the message
     * @param attribute attribute key
     * @param value attribute value
     */
    fun setAttribute(attribute: String, value: Long) {
        if (attribute.isEmpty()) {
            return
        }
        extension[attribute] = value
    }

    /**
     * \~chinese
     * 设置消息的 JSONObject 类型扩展属性
     * @param attribute 属性名
     * @param value, 属性值
     *
     * \~english
     * set a jsonobject type extra attributes of the message
     * @param attribute attribute key
     * @param value attribute value
     */
    fun setAttribute(attribute: String, value: JSONObject) {
        if (attribute.isEmpty()) {
            return
        }
        extension[attribute] = value
    }

    /**
     * \~chinese
     * 设置消息的 JSONArray 类型扩展属性
     * @param attribute 属性名
     * @param value, 属性值
     *
     * \~english
     * set a jsonarray type extra attributes of the message
     * @param attribute attribute key
     * @param value attribute value
     */
    fun setAttribute(attribute: String, value: JSONArray) {
        if (attribute.isEmpty()) {
            return
        }
        extension[attribute] = value
    }

    /**
     * \~chinese
     * 设置消息的 string 类型扩展属性
     * @param attribute 属性名
     * @param value, 属性值
     *
     * \~english
     * set a string type extra attributes of the message
     * @param attribute attribute key
     * @param value attribute value
     */
    fun setAttribute(attribute: String, value: String?) {
        if (attribute.isEmpty()) {
            return
        }
        extension[attribute] = value
    }

    /**
     * \~chinese
     * 获取 boolean 类型扩展属性
     * @param attribute 属性名
     * @return 属性值
     * @throws Exception
     *
     * \~english
     * get a boolean type extra attribute
     * @param attribute the attribute name
     * return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getBooleanAttribute(attribute: String): Boolean {
        if (attribute.isEmpty()) {
            throw Exception("attribute $attribute can not be null or empty")
        }
        if (!extension.keys.contains(attribute)) throw Exception("attribute $attribute not found")
        return extension[attribute] as Boolean
    }

    /**
     * \~chinese
     * 获取 boolean 类型扩展属性
     * @param attribute 属性名
     * @param defaultValue 缺省值
     * @return 属性值
     *
     * \~english
     * get a boolean type extra attribute
     * @param attribute the attribute name
     * @param defaultValue the default value you want
     * @return
     */
    fun getBooleanAttribute(
        attribute: String,
        defaultValue: Boolean
    ): Boolean {
        if (attribute.isEmpty()) {
            return defaultValue
        }
        if (!extension.keys.contains(attribute)) return defaultValue
        return extension[attribute] as Boolean
    }

    /**
     * \~chinese
     * 获取 int 类型扩展属性
     * @param attribute 属性名
     * @param defaultValue 缺省值
     * @return 属性值
     *
     * \~english
     * get a int type extra attribute
     * @param attribute the attribute name
     * @param defaultValue the default value you want
     * @return
     */
    fun getIntAttribute(attribute: String, defaultValue: Int): Int {
        if (attribute.isEmpty()) {
            return defaultValue
        }
        if (!extension.keys.contains(attribute)) return defaultValue
        return extension[attribute] as Int
    }

    /**
     * \~chinese
     * 获取 long 类型扩展属性
     * @param attribute 属性名
     * @param defaultValue 缺省值
     * @return 属性值
     *
     * \~english
     * get a long type extra attribute
     * @param attribute the attribute name
     * @param defaultValue the default value you want
     * @return
     */
    fun getLongAttribute(attribute: String, defaultValue: Long): Long {
        if (attribute.isEmpty()) {
            return defaultValue
        }
        if (!extension.keys.contains(attribute)) return defaultValue
        return extension[attribute] as Long
    }

    /**
     * \~chinese
     * 获取 int 类型扩展属性
     * @param attribute 属性名
     * @return 属性值
     * @throws Exception
     *
     * \~english
     * get a int type extra attribute
     * @param attribute the attribute name
     * @return the value
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getIntAttribute(attribute: String): Int {
        if (attribute.isEmpty()) {
            throw Exception("attribute $attribute can not be null or empty")
        }
        if (!extension.keys.contains(attribute)) throw Exception("attribute $attribute not found")
        return extension[attribute] as Int
    }

    /**
     * \~chinese
     * 获取 long 类型扩展属性
     * @param attribute 属性名
     * @return 属性值
     * @throws Exception
     *
     * \~english
     * get long type extra attribute
     * @param attribute the attribute name
     * @return the value
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getLongAttribute(attribute: String): Long {
        if (attribute.isEmpty()) {
            throw Exception("attribute $attribute can not be null or empty")
        }
        if (!extension.keys.contains(attribute)) throw Exception("attribute $attribute not found")
        return extension[attribute] as Long
    }

    /**
     * \~chinese
     * 获取 String 类型扩展属性
     * @param attribute 属性名
     * @return 属性值
     * @throws Exception
     *
     * \~english
     * get a string type extra attribute
     * @param attribute the attribute name
     * @return the value
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getStringAttribute(attribute: String): String? {
        if (attribute.isEmpty()) {
            throw Exception("attribute $attribute can not be null or empty")
        }
        if (!extension.keys.contains(attribute)) throw Exception("attribute $attribute not found")
        return extension[attribute] as String
    }

    /**
     * \~chinese
     * 获取 String 类型扩展属性
     * @param attribute 属性名
     * @param defaultValue 缺省值
     * @return 属性值
     *
     * \~english
     * get a string type extra attribute
     * @param attribute the attribute name
     * @param defaultValue the default value you want
     * @return
     */
    fun getStringAttribute(
        attribute: String,
        defaultValue: String?
    ): String? {
        if (attribute.isEmpty()) {
            return defaultValue
        }
        if (!extension.keys.contains(attribute)) return defaultValue
        return extension[attribute] as String
    }

    /**
     * \~chinese
     * 获取 JSONObject 类型扩展属性
     * @param attribute 属性名
     * @return 属性值
     * @throws Exception
     *
     * \~english
     * get a jsonobject type attribute
     * @param attribute
     * @return the value
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getJSONObjectAttribute(attribute: String): JSONObject? {
        if (attribute.isEmpty()) {
            return null
        }
        if (!extension.keys.contains(attribute)) throw Exception("attribute $attribute not found")
        return extension[attribute] as JSONObject
    }

    /**
     * \~chinese
     * 获取 JSONArray 类型扩展属性
     * @param attribute 属性名
     * @return 属性值
     * @throws Exception
     *
     * \~english
     * get a jsonarray type attribute
     * @param attribute
     * @return the value
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getJSONArrayAttribute(attribute: String): JSONArray? {
        if (attribute.isEmpty()) {
            return null
        }
        if (!extension.keys.contains(attribute)) throw Exception("attribute $attribute not found")
        return extension[attribute] as JSONArray
    }
    //endregion

    //region 6. Message
    /**
     * \~chinese
     * 消息的状态
     *
     * \~english
     * the status of the message
     *
     */
    @Ignore
    var messageStatus: Status = Status.SUCCESS

    @Ignore
    var messageStatusCallBack: EMCallbackHolder? = null

    @Ignore
    var roomCallBack: RoomCallBack? = null
    //endregion

    //region 7. Conversation
    var extension: JSONObject
        get() = ext.ext
        set(value) {
            ext.ext = value
        }
    //endregion

    //region 8. IJson
    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["id"] = id
        jsonObject["title"] = title
        jsonObject["name"] = name
        jsonObject["content"] = content
        jsonObject["uuid"] = uuid
        jsonObject["mtime"] = mtime
        jsonObject["icon"] = icon
        jsonObject["coverImageUrl"] = coverImageUrl
        jsonObject["isDummy"] = isDummy
        jsonObject["createTime"] = createTime
        jsonObject["updateTime"] = updateTime
        jsonObject["finishTime"] = finishTime
        jsonObject["deleteTime"] = deleteTime
        jsonObject["archiveTime"] = archiveTime
        jsonObject["pinTime"] = pinTime
        jsonObject["lockTime"] = lockTime
        jsonObject["blockTime"] = blockTime
        jsonObject["startTime"] = startTime
        jsonObject["totalLength"] = totalLength
        jsonObject["type"] = type
        jsonObject["subType"] = subType
        jsonObject["label"] = label
        jsonObject["status"] = status
        jsonObject["theme"] = theme
        jsonObject["color"] = color
        jsonObject["miniShowType"] = miniShowType
        jsonObject["render_type"] = render_type
        jsonObject["tags"] = tags
        jsonObject["topics"] = topics
        jsonObject["parent"] = parent
        jsonObject["lifeCycles"] = lifeCycles
        jsonObject["ext"] = ext
        jsonObject["attachmentItems"] = attachmentItems
        return jsonObject
    }
    //endregion

    //region 9. equals
    fun eq(another: RoomRecord): Boolean {
        return (id == another.id
                && name == another.name
                && title == another.title
                && content == another.content
                && uuid == another.uuid
                && mtime == another.mtime
                && icon == another.icon
                && coverImageUrl == another.coverImageUrl
                && isDummy == another.isDummy
                && createTime == another.createTime
                && updateTime == another.updateTime
                && finishTime == another.finishTime
                && deleteTime == another.deleteTime
                && archiveTime == another.archiveTime
                && pinTime == another.pinTime
                && lockTime == another.lockTime
                && blockTime == another.blockTime
                && startTime == another.startTime
                && totalLength == another.totalLength
                && type == another.type
                && subType == another.subType
                && lifeCycles == another.lifeCycles
                && label == another.label
                && status == another.status
                && theme == another.theme
                && color == another.color
                && order == another.order
                && miniShowType == another.miniShowType
                && render_type == another.render_type
                && tags == another.tags
                && topics == another.topics
                && parent == another.parent
                && ext == another.ext
                && attachmentItems == another.attachmentItems)
    }
    //endregion


    fun getCanSearchText(): String = "$title$content"

    //region Parcelable
    @Ignore
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString()!!,
        source.readString(),
        1 == source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readLong(),
        source.readSerializable() as LifeCycle,
        source.readInt(),
        source.readLong(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readSerializable() as Json,
        source.readSerializable() as AttachmentTail
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(name)
        writeString(title)
        writeString(content)
        writeString(uuid)
        writeValue(mtime)
        writeString(icon)
        writeString(coverImageUrl)
        writeInt((if (isDummy) 1 else 0))
        writeInt(type)
        writeInt(subType)
        writeLong(createTime)
        writeLong(updateTime)
        writeLong(finishTime)
        writeLong(deleteTime)
        writeLong(archiveTime)
        writeLong(pinTime)
        writeLong(lockTime)
        writeLong(blockTime)
        writeLong(startTime)
        writeLong(totalLength)
        writeSerializable(lifeCycles)
        writeInt(label)
        writeLong(status)
        writeInt(theme)
        writeInt(color)
        writeInt(miniShowType)
        writeInt(render_type)
        writeInt(order)
        writeString(tags)
        writeString(topics)
        writeString(parent)
        writeSerializable(ext)
        writeSerializable(attachmentItems)
    }
    //endregion

    //region constructor
    @Ignore
    constructor(record: RoomRecord) : this(
        record.id,
        record.name,
        record.title,
        record.content,
        record.uuid,
        record.mtime,
        record.icon,
        record.coverImageUrl,
        record.isDummy,
        record.type,
        record.subType,
        record.createTime,
        record.updateTime,
        record.finishTime,
        record.deleteTime,
        record.archiveTime,
        record.pinTime,
        record.lockTime,
        record.blockTime,
        record.startTime,
        record.totalLength,
        record.lifeCycles,
        record.label,
        record.status,
        record.theme,
        record.color,
        record.miniShowType,
        record.render_type,
        record.order,
        record.tags,
        record.topics,
        record.parent,
        record.ext,
        record.attachmentItems
    )
    //endregion


    companion object {
        const val TAG = "msg"

        @JvmStatic
        fun forBlock(id: Long, name: String): RoomRecord {
            return RoomRecord(id, name, name, name)
        }

        @JvmStatic
        fun forBlock(id: Long, name: String, type: Int, subType: Int, parent: String): RoomRecord {
            return RoomRecord(id, name, name, name, type = type, subType = subType, parent = parent)
        }

        @JvmStatic
        fun forName(name: String): RoomRecord {
            val b = forBlock(DateTime().millis, name)
            b.setUnderWay(true)
            return b
        }

        @JvmStatic
        fun forConversation(id: Long, name: String, subType: Int, parent: String): RoomRecord {
            return forBlock(id, name, BLOCK_CONVERSATION, subType, parent)
        }

        @JvmStatic
        fun forSystemConversation(
            subType: Int,
            name: String,
            content: String,
            parent: String
        ): RoomRecord {
            val c = forConversation(0, name, subType = subType, parent = parent)
            c.content = content
            c.setSystem(true)
            return c
        }

        @JvmStatic
        fun forBook(id: Long, name: String): RoomRecord {
            return forType(id, name, BLOCK_CONTAINER)
        }

        @JvmStatic
        fun forDatabase(id: Long, name: String): RoomRecord {
            return forType(id, name, BLOCK_DATABASE)
        }

        @JvmStatic
        fun forSystem(id: Long, name: String, type: Int, subType: Int, parent: String): RoomRecord {
            val b = forBlock(id, name, type, subType, parent)
            b.setSystem(true)
            return b
        }

        @JvmStatic
        fun forPage(id: Long, name: String): RoomRecord {
            return forType(id, name, BLOCK_DIVIDER)
        }

        @JvmStatic
        fun forType(id: Long, name: String, type: Int): RoomRecord {
            val model = forBlock(id, name)
            model.type = type
            return model
        }

        @JvmStatic
        fun forPage(id: Long, name: String, subType: Int, parent: String): RoomRecord {
            return forBlock(id, name, BLOCK_DIVIDER, subType, parent)
        }

        @JvmStatic
        fun forRecord(id: Long, name: String, parent: String): RoomRecord {
            val b = forType(id, name, BLOCK_RECORD)
            b.parent = parent
            return b
        }

        @JvmStatic
        fun forRecord(id: Long, name: String): RoomRecord {
            return forType(id, name, BLOCK_RECORD)
        }

        @JvmStatic
        fun forContainer(id: Long, name: String): RoomRecord {
            return forType(id, name, BLOCK_CONTAINER)
        }

        @JvmStatic
        fun forThing(id: Long, name: String, subType: Int, color: Int): RoomRecord {
            val b = forType(id, name, BLOCK_RECORD)
            b.subType = subType
            b.setUnderWay(true)
            b.color = color
            return b
        }

        @JvmStatic
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))

        @JvmStatic
        fun fromJson(jsonObject: JSONObject): RoomRecord {
            val id: Long = jsonObject.getLong("id")
            val name: String = jsonObject.getString("name")
            val title: String = jsonObject.getString("title")
            val content: String = jsonObject.getString("content")
            val uuid: String = jsonObject.getString("uuid")
            val mtime: Long? = jsonObject.getLong("mtime")
            val icon: String = jsonObject.getString("icon") ?: "R.drawable.ic_notes_hint_24dp"
            val coverImageUrl: String? = jsonObject.getString("coverImageUrl")
            val isDummy: Boolean = jsonObject.getBoolean("isDummy")
            val createTime: Long = jsonObject.getLong("createTime")
            val updateTime: Long = jsonObject.getLong("updateTime")
            val finishTime: Long = jsonObject.getLong("finishTime")
            val deleteTime: Long = jsonObject.getLong("deleteTime")
            val archiveTime: Long = jsonObject.getLong("archiveTime")
            val pinTime: Long = jsonObject.getLong("pinTime")
            val lockTime: Long = jsonObject.getLong("lockTime")
            val blockTime: Long = jsonObject.getLong("blockTime")
            val startTime: Long = jsonObject.getLong("startTime")
            val totalLength: Long = jsonObject.getLong("totalLength")
            val type: Int = jsonObject.getInteger("type")
            val subType: Int = jsonObject.getInteger("subType")
            val lifeCyclesObj: JSONObject = jsonObject.getJSONObject("lifeCycles")
            val label: Int = jsonObject.getInteger("label")
            val status: Long = jsonObject.getLong("status")
            val theme: Int = jsonObject.getInteger("theme")
            val color: Int = jsonObject.getInteger("color")
            val miniShowType: Int = jsonObject.getInteger("miniShowType")
            val render_type: Int = jsonObject.getInteger("render_type")
            val tags: String = jsonObject.getString("tags")
            val topics: String = jsonObject.getString("topics")
            val parent: String = jsonObject.getString("parent")
            val extObj: JSONObject = jsonObject.getJSONObject("ext")
            val attachmentItemsObj: JSONObject = jsonObject.getJSONObject("attachmentItems")
            val lifeCycles: LifeCycle = LifeCycle.fromJson(lifeCyclesObj)
            val ext: Json = Json.fromJson(extObj)
            val attachmentItems: AttachmentTail = AttachmentTail.fromJson(attachmentItemsObj)
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

        @IntDef(
            FOLD,
            EXPANDED,
            COLLAPSE_TO_PARENT
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class ShowType

        const val FOLD = 0

        const val EXPANDED = 1

        const val COLLAPSE_TO_PARENT = 2

        @JvmField
        val CREATOR: Parcelable.Creator<RoomRecord> = object : Parcelable.Creator<RoomRecord> {
            override fun createFromParcel(source: Parcel): RoomRecord = RoomRecord(source)
            override fun newArray(size: Int): Array<RoomRecord?> = arrayOfNulls(size)
        }
    }
}
