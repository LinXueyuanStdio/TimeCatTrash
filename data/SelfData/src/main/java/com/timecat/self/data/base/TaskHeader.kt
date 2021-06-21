package com.timecat.self.data.base

import androidx.annotation.IntDef
import androidx.annotation.StringDef
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.timecat.self.data.block.type.BLOCK_CONVERSATION
import com.timecat.self.data.block.type.BLOCK_DATABASE
import com.timecat.self.data.block.type.BLOCK_RECORD
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author lxy
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description null
 * @usage null
 */
data class TaskHeader(
    var createTime: Long = DateTime().millis,
    var updateTime: Long = createTime,
    var finishTime: Long = createTime,
    var deleteTime: Long = createTime,
    var archiveTime: Long = createTime,
    var pinTime: Long = createTime,
    var lockTime: Long = createTime,
    var blockTime: Long = createTime,
    var startTime: Long = createTime,
    var totalLength: Long = 0,
    var lifeCycles: LifeCycle = LifeCycle.now2endOfToday(),
    @TaskLabel
    var label: Int = TASK_LABEL_NOT_IMPORTANT_NOT_URGENT,
    var status: Long = TASK_MODE_INIT //状态集
) : Serializable, ITaskStatus, ITaskLabel,
    IJson, ITaskLifeCycle {

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

    //region 3. IJson<TaskHeader>
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): TaskHeader {
            val j = jsonObject.getJSONObject("lifeCycle")
            val lifeCycle: LifeCycle = LifeCycle.fromJson(j)
            return TaskHeader(
                jsonObject.getLong("createTime"),
                jsonObject.getLong("updateTime"),
                jsonObject.getLong("finishTime"),
                jsonObject.getLong("deleteTime"),
                jsonObject.getLong("archiveTime"),
                jsonObject.getLong("pinTime"),
                jsonObject.getLong("lockTime"),
                jsonObject.getLong("blockTime"),
                jsonObject.getLong("startTime"),
                jsonObject.getLong("totalLength"),
                lifeCycle,
                jsonObject.getInteger("label"),
                jsonObject.getLong("status")
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
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
        jsonObject["lifeCycle"] = lifeCycles
        jsonObject["label"] = label
        jsonObject["status"] = status
        return jsonObject
    }
    //endregion

    //region 4. ILifeCycle
    override fun getLifeCycle(): LifeCycle = lifeCycles

    fun status(): Pair<TaskState, Long> {
        var state: TaskState = TaskState.DOING
        var time: Long = 0
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

    //endregion
}

fun Long.toDateTime() = DateTime(this)

// region 1. status
interface ITaskStatus : IStatus {
    fun isReadOnly(): Boolean = isStatusEnabled(TASK_READ_ONLY)
    fun isFinished(): Boolean = isStatusEnabled(TASK_FINISH)
    fun isArchived(): Boolean = isStatusEnabled(TASK_ARCHIVE)
    fun isDeleted(): Boolean = isStatusEnabled(TASK_DELETE)
    fun isPined(): Boolean = isStatusEnabled(TASK_PIN)
    fun isLocked(): Boolean = isStatusEnabled(TASK_LOCK)
    fun isPrivate(): Boolean = isStatusEnabled(TASK_PRIVATE)
    fun isAllDay(): Boolean = isStatusEnabled(TASK_ALL_DAY)
    fun canFinish(): Boolean = isStatusEnabled(TASK_CAN_FINISH)
    fun isBlock(): Boolean = isStatusEnabled(TASK_BLOCK)
    fun isBlack(): Boolean = isStatusEnabled(TASK_BLACK)
    fun isTemp(): Boolean = isStatusEnabled(TASK_TEMP)
    fun isSystem(): Boolean = isStatusEnabled(TASK_SYSTEM)
    fun isReminderOn(): Boolean = isStatusEnabled(TASK_OPEN_REMINDER)
    fun isUnderWay(): Boolean = isStatusEnabled(TASK_UNDERWAY)
    fun isFavourite(): Boolean = isStatusEnabled(TASK_FAVOURITE)
    fun isDisableBackup(): Boolean = isStatusEnabled(TASK_DISABLE_BACKUP)

    fun setReadOnly(yes: Boolean) = updateStatus(TASK_READ_ONLY, yes)
    fun setFinished(yes: Boolean) = updateStatus(TASK_FINISH, yes)
    fun setArchived(yes: Boolean) = updateStatus(TASK_ARCHIVE, yes)
    fun setDeleted(yes: Boolean) = updateStatus(TASK_DELETE, yes)
    fun setPined(yes: Boolean) = updateStatus(TASK_PIN, yes)
    fun setLocked(yes: Boolean) = updateStatus(TASK_LOCK, yes)
    fun setPrivate(yes: Boolean) = updateStatus(TASK_PRIVATE, yes)
    fun setAllDay(yes: Boolean) = updateStatus(TASK_ALL_DAY, yes)
    fun setCanFinish(yes: Boolean) = updateStatus(TASK_CAN_FINISH, yes)
    fun setBlock(yes: Boolean) = updateStatus(TASK_BLOCK, yes)
    fun setBlack(yes: Boolean) = updateStatus(TASK_BLACK, yes)
    fun setTemp(yes: Boolean) = updateStatus(TASK_TEMP, yes)
    fun setSystem(yes: Boolean) = updateStatus(TASK_SYSTEM, yes)
    fun setOpenReminder(yes: Boolean) = updateStatus(TASK_OPEN_REMINDER, yes)
    fun setUnderWay(yes: Boolean) = updateStatus(TASK_UNDERWAY, yes)
    fun setFavourite(yes: Boolean) = updateStatus(TASK_FAVOURITE, yes)
    fun setDisableBackup(yes: Boolean) = updateStatus(TASK_DISABLE_BACKUP, yes)

    override fun statusDescription(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(if (isReadOnly()) "只读 " else "")
        stringBuilder.append(if (isFinished()) "已完成 " else "")
        stringBuilder.append(if (isArchived()) "已归档 " else "")
        stringBuilder.append(if (isDeleted()) "已删除 " else "")
        stringBuilder.append(if (isPined()) "已置顶 " else "")
        stringBuilder.append(if (isLocked()) "已锁定 " else "")
        stringBuilder.append(if (isPrivate()) "已私有 " else "")
        stringBuilder.append(if (isAllDay()) "全天 " else "")
        stringBuilder.append(if (canFinish()) "可完成 " else "")
        stringBuilder.append(if (isBlock()) "已拉黑 " else "")
        stringBuilder.append(if (isBlack()) "已拉黑 " else "")
        stringBuilder.append(if (isTemp()) "暂存区 " else "")
        stringBuilder.append(if (isSystem()) "系统级 " else "")
        stringBuilder.append(if (isReminderOn()) "开启提醒 " else "")
        stringBuilder.append(if (isUnderWay()) "将要开始 " else "")
        stringBuilder.append(if (isFavourite()) "喜欢 " else "")
        stringBuilder.append(if (isDisableBackup()) "禁用对此的备份 " else "")
        return stringBuilder.toString()
    }
}

const val TASK_DELETE: Long = 0x00000001//是否删除，回收站
const val TASK_READ_ONLY: Long = 0x00000002//只读，不可编辑
const val TASK_FINISH: Long = 0x00000004//完成
const val TASK_ARCHIVE: Long = 0x00000008//归档，形如虚设，无限制，只是换了个区
const val TASK_PIN: Long = 0x00000010//置顶
const val TASK_LOCK: Long = 0x00000020//锁定，不能删除，不能修改，限制对内容的操作
const val TASK_PRIVATE: Long = 0x00000040//私有，权限控制
const val TASK_ALL_DAY: Long = 0x00000080//全天
const val TASK_CAN_FINISH: Long = 0x00000100//可以被完成
const val TASK_BLOCK: Long = 0x00000200//封印，用户设置密码，用密码才能解除封印。被封印状态下仅所有者可查看，修改，删除
const val TASK_BLACK: Long = 0x00000400//黑名单，不能查看，不能删除，不能修改。官方控制
const val TASK_TEMP: Long = 0x00000800//暂存区，形如虚设，无限制，只是换了个区
const val TASK_SYSTEM: Long = 0x00001000//暂存区，形如虚设，无限制，只是换了个区
const val TASK_OPEN_REMINDER: Long = 0x00002000//打开提醒
const val TASK_UNDERWAY: Long = 0x00004000//将要到来
const val TASK_FAVOURITE: Long = 0x00008000//喜欢，收藏夹
const val TASK_DISABLE_BACKUP: Long = 0x00010000//禁用对此模型的备份

const val TASK_MODE_INIT: Long = TASK_UNDERWAY
const val TASK_MODE_FALSE: Long = TASK_DELETE or TASK_FINISH or TASK_ARCHIVE

class TaskStatus(var status: Long) : ITaskStatus, Serializable {
    companion object {
        @JvmStatic
        fun ofUnderWay(): TaskStatus = TaskStatus(TASK_UNDERWAY)

        @JvmStatic
        fun ofFinished(): TaskStatus = TaskStatus(TASK_FINISH)

        @JvmStatic
        fun ofDeleted(): TaskStatus = TaskStatus(TASK_DELETE)

        @JvmStatic
        fun ofAll(): TaskStatus = TaskStatus(-1)
    }

    override fun toString(): String = "$status"
    //region 2. ITaskStatus
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
//endregion

// region 2. label
interface ITaskLabel {
    @TaskLabelColor
    fun taskLabelColor(): Int

    fun taskLabelBackgroundColor(): Int


    @TaskLabelColorStr
    fun taskLabelColorStr(): String

    fun taskLabelStr(): String
    fun taskLabelBackgroundColorStr(): String

    fun setNextLabel()
}

/**
 * 有生命的数据结构
 */
@IntDef(
    BLOCK_RECORD,
    BLOCK_DATABASE,
    BLOCK_CONVERSATION
)
@Retention(AnnotationRetention.SOURCE)
annotation class LiveRecordType

@IntDef(
    TASK_LABEL_IMPORTANT_URGENT,
    TASK_LABEL_IMPORTANT_NOT_URGENT,
    TASK_LABEL_NOT_IMPORTANT_URGENT,
    TASK_LABEL_NOT_IMPORTANT_NOT_URGENT
)
@Retention(AnnotationRetention.SOURCE)
annotation class TaskLabel

const val TASK_LABEL_IMPORTANT_URGENT = 0
const val TASK_LABEL_IMPORTANT_NOT_URGENT = 1
const val TASK_LABEL_NOT_IMPORTANT_URGENT = 2
const val TASK_LABEL_NOT_IMPORTANT_NOT_URGENT = 3

@IntDef(
    TASK_LABEL_IMPORTANT_URGENT_COLOR,
    TASK_LABEL_IMPORTANT_NOT_URGENT_COLOR,
    TASK_LABEL_NOT_IMPORTANT_URGENT_COLOR,
    TASK_LABEL_NOT_IMPORTANT_NOT_URGENT_COLOR
)
@Retention(AnnotationRetention.SOURCE)
annotation class TaskLabelColor

const val TASK_LABEL_IMPORTANT_URGENT_COLOR = -0xbbcca
const val TASK_LABEL_IMPORTANT_NOT_URGENT_COLOR = -0x7900
const val TASK_LABEL_NOT_IMPORTANT_URGENT_COLOR = -0xde690d
const val TASK_LABEL_NOT_IMPORTANT_NOT_URGENT_COLOR = -0xb350b0

val taskLabelColor = intArrayOf(
    TASK_LABEL_IMPORTANT_URGENT_COLOR,
    TASK_LABEL_IMPORTANT_NOT_URGENT_COLOR,
    TASK_LABEL_NOT_IMPORTANT_URGENT_COLOR,
    TASK_LABEL_NOT_IMPORTANT_NOT_URGENT_COLOR
)
val taskBackgroundColor = intArrayOf(0x50f44336, 0x50ff9800, 0x502196f3, 0x504caf50)

@StringDef(
    TASK_LABEL_IMPORTANT_URGENT_COLOR_STRING,
    TASK_LABEL_IMPORTANT_NOT_URGENT_COLOR_STRING,
    TASK_LABEL_NOT_IMPORTANT_URGENT_COLOR_STRING,
    TASK_LABEL_NOT_IMPORTANT_NOT_URGENT_COLOR_STRING
)
@Retention(AnnotationRetention.SOURCE)
annotation class TaskLabelColorStr

const val TASK_LABEL_IMPORTANT_URGENT_COLOR_STRING = "重要且紧急"
const val TASK_LABEL_IMPORTANT_NOT_URGENT_COLOR_STRING = "重要不紧急"
const val TASK_LABEL_NOT_IMPORTANT_URGENT_COLOR_STRING = "紧急不重要"
const val TASK_LABEL_NOT_IMPORTANT_NOT_URGENT_COLOR_STRING = "不重要不紧急"


val taskLabelStr = arrayOf(
    TASK_LABEL_IMPORTANT_URGENT_COLOR_STRING,
    TASK_LABEL_IMPORTANT_NOT_URGENT_COLOR_STRING,
    TASK_LABEL_NOT_IMPORTANT_URGENT_COLOR_STRING,
    TASK_LABEL_NOT_IMPORTANT_NOT_URGENT_COLOR_STRING
)
val taskLabelColorStr = arrayOf("#f44336", "#ff9800", "#2196f3", "#4caf50")
val taskLabelBackgroundColorStr = arrayOf("#50f44336", "#50ff9800", "#502196f3", "#504caf50")
//endregion

// region 3. LifeCycle
/**
 * 生命周期
 * 精确到天，小时
 * 单日，多日，起止，重复
 * 单日，多日，起止：天数列表或小时数列表
 * 重复：
 *     重复方式：每天，每年，每周（周一...），每月（21日...）
 *     截止日期：永不结束、明确日期到天
 * type:
 *     -1 单日，-2 多日，-3 起止，0 重复.永不结束，date().Millis 重复.截止日期到天
 * list:
 *     单日，""
 *     多日，"Millis,Millis,Millis"
 *     起止，"begin_Millis"
 *     重复，第一位为大周期，后面是大周期里偏移，偏移=-1，大周期都是，偏移>=0，大周期的时间点
 *         "60" 每分钟，
 *         "3600" 每小时，
 *         "86400" 每天，
 *         "86400*365" 每年，
 *         "86400*7,1,4,7,8" 每周，[1, 7] 周一到周日，8 节假日
 *         "86400*30,1,5,6,21,31,0" 每月，[1， 31] 日期，0 每月最后一天
 * deadLine:
 *     截止日期 0 不截止，>0 截止
 *     单日，截止日期
 *     多日，0
 *     起止，end_Millis
 *     重复，
 *         每天，0 不截止，>0 截止
 *         每年，0 不截止，>0 截止
 *         每周，[1, 7] 周一到周日，0 不截止，>0 截止
 *         每月，[1， 31] 日期，0 不截止，>0 截止
 *         每3天，0 不截止，>0 截止
 *         每3月，0 不截止，>0 截止
 *         每3周，0 不截止，>0 截止
 */
data class LifeCycle(
    var type: Int,
    var start: Long, // 开始时间，保存时自动计算，可能是创建时，也可能是之后某个时刻
    var totalLen: Long, // 0 永不停止，保存时自动计算
    var cycles: MutableList<Cycle>
) : Serializable, IJson {
    fun beginTs(): Long = start
    fun endTs(): Long = if (totalLen == 0L) 0 else start + totalLen
    fun isRepeat(): Boolean = type == LIFECYCLE_REPEAT_NEVER_STOP

    //region 3. IJson<TaskHeader>
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): LifeCycle {
            val showArray = jsonObject.getJSONArray("cycles")
            val list: MutableList<Cycle> = mutableListOf()
            for (i in showArray) {
                list.add(Cycle.fromJson(i as JSONObject))
            }
            return LifeCycle(
                jsonObject.getInteger("type"),
                jsonObject.getLong("start"),
                jsonObject.getLong("totalLen"),
                list
            )
        }

        @JvmStatic
        fun totalLengthFromNow2endOfToday(now:Long) :Long{
            return DateTime(now).plusDays(1).withMillisOfDay(0).millis - now - 1
        }

        @JvmStatic
        fun now2endOfToday(): LifeCycle {
            val now = System.currentTimeMillis()
            val totalLen: Long = totalLengthFromNow2endOfToday(now)
            return LifeCycle(
                LIFECYCLE_SINGLE,
                now,
                totalLen,
                mutableListOf(
                    Cycle(
                        mutableListOf(
                            Time(now, totalLen, "做事", mutableListOf())
                        ),
                        1
                    )
                )
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["type"] = type
        jsonObject["start"] = start
        jsonObject["totalLen"] = totalLen
        jsonObject["cycles"] = cycles
        return jsonObject
    }
    //endregion

}

enum class TaskState {
    FINISH, WAITING, DOING, DELAY
}

interface ITaskLifeCycle {
    fun getLifeCycle(): LifeCycle
}

const val LIFECYCLE_SINGLE: Int = 0 // 单日
const val LIFECYCLE_MULTI: Int = 1 // 多日
const val LIFECYCLE_BEGIN_END: Int = 2 // 起止
const val LIFECYCLE_REPEAT_NEVER_STOP: Int = 3 // 重复
const val LIFECYCLE_CUSTOM: Int = 4 // 自定义

data class Behavior(val type: String, val structure: String) : Serializable, IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): Behavior {
            val type = jsonObject.getString("type")
            val structure = jsonObject.getString("structure")
            return Behavior(
                type,
                structure
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val j = JSONObject()
        j["type"] = type
        j["structure"] = structure
        return j
    }

}

const val BEHAVIOR_MUSIC = "Music"

/**
 * start 0 那时的时间，即上一个 Time 或 Cycle 刚结束
 * length 0 默认 1 秒
 */
data class Time(
    var start: Long,
    var length: Long = 1000,
    var name: String,
    var behavior: MutableList<Behavior>? = null
) : Serializable, IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): Time {
            val start = jsonObject.getLong("start")
            val length = jsonObject.getLong("length")
            val name = jsonObject.getString("name")
            val behaviors = jsonObject.getJSONArray("behavior")
            val list: MutableList<Behavior> = mutableListOf()
            for (i in behaviors) {
                list.add(Behavior.fromJson(i as JSONObject))
            }
            return Time(
                start,
                length,
                name,
                list
            )
        }
    }

    override fun toJsonObject(): JSONObject {
        val j = JSONObject()
        j["start"] = start
        j["length"] = length
        j["name"] = name
        j["behaviors"] = behavior
        return j
    }

}

const val TIME_DO_Nothing: Int = 0 // 啥也不做
const val TIME_DO_Unknown: Int = 1 // 知道在做事，但不知道具体在做什么
const val TIME_DO_Alert: Int = 2 // 提醒

const val TIME_LEN_Second = 1000
const val TIME_LEN_Minute = 60 * 1000
const val TIME_LEN_Hour = 60 * 60 * 1000
const val TIME_LEN_Day = 24 * 60 * 60 * 1000
const val TIME_LEN_Week = 7 * 24 * 60 * 60 * 1000
const val TIME_LEN_ThisMonth = -1 // 当前时间所在的那个月的长度
const val TIME_LEN_ThatMonth = -2 // 那时的那个月的长度
const val TIME_LEN_ThisYear = -3 // 当前时间所在的那个年的长度
const val TIME_LEN_ThatYear = -4 // 那时的那个年的长度

/**
 * loop 重复多少次
 *   0 无限次，永不停止
 *   1 一次
 *   n n 次
 */
data class Cycle(val timeList: MutableList<Time>, var loop: Int) : Serializable, IJson {
    companion object {
        fun fromJson(json: String) =
            fromJson(JSON.parseObject(json))

        fun fromJson(jsonObject: JSONObject): Cycle {
            val timeList = jsonObject.getJSONArray("timeList")
            val list: MutableList<Time> = mutableListOf()
            for (i in timeList) {
                list.add(Time.fromJson(i as JSONObject))
            }
            val loop = jsonObject.getInteger("loop")
            return Cycle(list, loop)
        }
    }

    override fun toJsonObject(): JSONObject {
        val j = JSONObject()
        j["timeList"] = timeList
        j["loop"] = loop
        return j
    }

}
//endregion