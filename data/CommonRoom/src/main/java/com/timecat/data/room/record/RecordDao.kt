package com.timecat.data.room.record

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.timecat.component.setting.DEF
import com.timecat.data.room.*
import com.timecat.data.room.habit.Habit
import com.timecat.data.room.habit.HabitRecord
import com.timecat.data.room.habit.HabitReminder
import com.timecat.data.room.message.RoomConversationType
import com.timecat.data.room.reminder.Reminder
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.BLOCK_APP_WebApp
import com.timecat.identity.data.block.type.*
import org.joda.time.DateTime
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-29
 * @description null
 * @usage null
 */
@Dao
abstract class RecordDao : BaseDao<RoomRecord> {
    //region 通用
    @Query("SELECT * FROM records WHERE id = :id LIMIT 1")
    abstract fun get(id: Long): RoomRecord?

    @Query("SELECT * FROM records WHERE title = :title LIMIT 1")
    abstract fun get(title: String): RoomRecord?

    @Query("SELECT * FROM records WHERE uuid = :uuid")
    abstract fun getByUuid(uuid: String): RoomRecord?

    @Query("SELECT * FROM records WHERE type IN (:type) AND subType IN (:subTypes) AND (status & :status != 0)")
    abstract fun getByDomain(
        type: List<Int>,
        subTypes: List<Int>,
        status: Long
    ): MutableList<RoomRecord>?

    @Query("SELECT * FROM records WHERE type IN (:type) AND (status & :status != 0)")
    abstract fun getByDomain(
        type: List<Int>,
        status: Long
    ): MutableList<RoomRecord>?

    @Query("SELECT * FROM records WHERE type IN (:type) AND (status & :status != 0) AND (status & :falseState = 0)")
    abstract fun getByDomain(
        type: List<Int>,
        status: Long,
        falseState: Long
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type IN (:type) AND (status & :status != 0) AND (status & :falseState = 0) AND parent = :parent")
    abstract fun getByDomain(
        type: List<Int>,
        status: Long,
        falseState: Long,
        parent: String
    ): MutableList<RoomRecord>

    @Transaction
    open fun loadFor(
        parent: String?,
        type: List<Int>,
        status: Long,
        falseState: Long
    ): MutableList<RoomRecord> {
        if (parent == null) {
            return getByDomain(type, status, falseState)
        } else {
            return getByDomain(type, status, falseState, parent)
        }
    }

    @Query("SELECT * FROM records WHERE uuid = :uuid")
    abstract fun getLiveData(uuid: String): LiveData<RoomRecord> // null not allowed, use List

    @Query("SELECT * FROM records WHERE (startTime >= :startTime) AND (startTime <= :endTime)")
    abstract fun getBetween(startTime: Long, endTime: Long): MutableList<RoomRecord>

    @Query("SELECT * FROM records ORDER BY createTime DESC")
    abstract fun getAll(): MutableList<RoomRecord>

    @Query("SELECT count(*) FROM records")
    abstract fun count(): Int

    @Query("UPDATE records SET status = status & $TASK_ARCHIVE WHERE uuid = :uuid")
    abstract fun markArchive(uuid: String)

    @Query("UPDATE records SET status = status & $TASK_DELETE WHERE uuid = :uuid")
    abstract fun markDelete(uuid: String)

    @Transaction
    open fun archiveBatch(uuids: List<String>) {
        uuids.forEach {
            markArchive(it)
        }
    }

    @Transaction
    open fun deleteBatch(uuids: List<String>) {
        uuids.forEach {
            markDelete(it)
        }
    }

    @Insert
    abstract fun insertRoomRecords(vararg records: RoomRecord): LongArray

    @Update
    abstract fun updateRoomRecords(vararg record: RoomRecord): Int
    //endregion

    //region BLOCK_COLLECTION
    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY createTime DESC")
    abstract fun getAllContainer(): MutableList<RoomRecord>
    //endregion

    //region BLOCK_CONVERSATION
    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION ORDER BY updateTime DESC")
    abstract fun getAll_BLOCK_CONVERSATION(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND uuid = :conversationUuid")
    abstract fun get_BLOCK_CONVERSATION(conversationUuid: String): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND id = :conversationId")
    abstract fun get_BLOCK_CONVERSATION(conversationId: Long): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND parent = :bookUuid LIMIT 1")
    abstract fun getForBook_BLOCK_CONVERSATION(bookUuid: String): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND parent = :bookUuid")
    abstract fun getListForBook_BLOCK_CONVERSATION(bookUuid: String): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND parent = :bookUuid")
    abstract fun getLiveListForBook_BLOCK_CONVERSATION(bookUuid: String): LiveData<MutableList<RoomRecord>>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND uuid = :conversationId AND subType = :conversationType")
    abstract fun get_BLOCK_CONVERSATION(conversationId: String, conversationType: Int): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND uuid = :conversationId")
    abstract fun getLiveData_BLOCK_CONVERSATION(conversationId: String): LiveData<RoomRecord> // null not allowed, use List

    @Transaction
    open fun initConversation() {
        insert(
            RoomRecord.forSystemConversation(
                RoomConversationType.Novel.id,
                "逐书者",
                "解析网页获取书",
                ""
            )
        )
        insert(
            RoomRecord.forSystemConversation(
                RoomConversationType.Record.id,
                "著书者",
                "记录，融合了笔记、任务、计划、目标",
                ""
            )
        )

        val defaultBook = RoomRecord.forBlock(DEFAULT_BOOK_ID, "默认之书")
        defaultBook.type = BLOCK_CONTAINER
        defaultBook.content = "默认之书，系统级，所有未归类的页都属于默认之书。"
        defaultBook.coverImageUrl = "R.drawable.ic_folder"
        defaultBook.setSystem(true)
        insert(defaultBook)
        DEF.block().putString(DEFAULT_BOOK_UUID, defaultBook.uuid)

        val defaultConversation = RoomRecord.forBlock(DEFAULT_CONVERSATION_ID, "默认会话")
        defaultConversation.type = BLOCK_CONVERSATION
        defaultConversation.subType = BLOCK_Chat
        defaultConversation.content = "默认会话，系统级。"
        defaultConversation.coverImageUrl = "R.drawable.ic_comment"
        defaultConversation.setSystem(true)
        insert(defaultConversation)
        DEF.block().putString(DEFAULT_CONVERSATION_UUID, defaultConversation.uuid)

        val examplePage = RoomRecord.forBlock(20200124, "示例之页")
        examplePage.type = BLOCK_DIVIDER
        examplePage.subType = PAGE_Record
        examplePage.parent = defaultBook.uuid
        examplePage.content = "页示例。"
        insert(examplePage)

        val exampleBook = RoomRecord.forBlock(202001240, "嵌套之书")
        exampleBook.type = BLOCK_CONTAINER
        exampleBook.content = "嵌套之书，示例。可无穷嵌套。相当于文件夹。"
        exampleBook.coverImageUrl = "R.drawable.ic_folder"
        exampleBook.parent = defaultBook.uuid
        insert(exampleBook)

        val exampleBook2 = RoomRecord.forBlock(202001241, "嵌套之书2")
        exampleBook2.type = BLOCK_CONTAINER
        exampleBook2.content = "嵌套之书2，示例。可无穷嵌套。相当于文件夹。"
        exampleBook2.coverImageUrl = "R.drawable.ic_folder"
        exampleBook2.parent = exampleBook.uuid
        insert(exampleBook2)

        val examplePage2 = RoomRecord.forBlock(202001242, "示例之页")
        examplePage2.type = BLOCK_DIVIDER
        examplePage2.subType = PAGE_Record
        examplePage2.parent = exampleBook.uuid
        examplePage2.content = "页示例。"
        insert(examplePage2)

        val examplePage3 = RoomRecord.forBlock(202001243, "示例之页")
        examplePage3.type = BLOCK_DIVIDER
        examplePage3.subType = PAGE_Record
        examplePage3.parent = exampleBook2.uuid
        examplePage3.content = "页示例。"
        insert(examplePage3)
    }
    //endregion

    //region BLOCK_MESSAGE
    @Query("SELECT * FROM records WHERE type = $BLOCK_MESSAGE AND id = :id LIMIT 1")
    abstract fun get_BLOCK_MESSAGE(id: Long): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_MESSAGE ORDER BY createTime")
    abstract fun getAll_BLOCK_MESSAGE(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_MESSAGE AND parent = :conversationId ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_MESSAGE(conversationId: String): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_MESSAGE AND uuid = :msgId")
    abstract fun get_BLOCK_MESSAGE(msgId: String): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_MESSAGE AND id = :id ORDER BY createTime")
    abstract fun getLiveData_BLOCK_MESSAGE(id: Long): LiveData<RoomRecord> // null not allowed, use List

    @Query("SELECT count(*) FROM records WHERE type = $BLOCK_MESSAGE AND parent = :conversationId")
    abstract fun getAllMsgCount_BLOCK_MESSAGE(conversationId: String): Int

    @Query("SELECT * FROM records WHERE parent = :conversationId ORDER BY createTime")
    abstract fun getLatestMessage_BLOCK_MESSAGE(conversationId: String): RoomRecord?

    @Query("SELECT * FROM records WHERE parent = :conversationId AND createTime < :createTime ORDER BY createTime LIMIT :pageSize")
    abstract fun getAll_BLOCK_MESSAGE(
        conversationId: String,
        createTime: Long,
        pageSize: Int
    ): MutableList<RoomRecord>

    @Insert
    abstract fun insertMessages(vararg message: RoomRecord): LongArray

    @Update
    abstract fun updateMessages(vararg message: RoomRecord): Int
    //endregion

    //region BLOCK_RECORD
    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND ((status & $TASK_DELETE) = 0) ORDER BY updateTime DESC")
    abstract fun getAll_BLOCK_RECORD(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND ((status & $TASK_DELETE) = 0) ORDER BY updateTime DESC")
    abstract fun getAllLive_BLOCK_RECORD(): LiveData<List<RoomRecord>>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN ($REMINDER, $HABIT, $GOAL) AND ((status & $TASK_MODE_FALSE) = 0) ORDER BY updateTime DESC")
    abstract fun getAllTime_BLOCK_RECORD(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN ($REMINDER, $HABIT, $GOAL) AND startTime<=:toTS AND (startTime+totalLength > :fromTS) AND ((status & $TASK_MODE_FALSE) = 0) ORDER BY updateTime DESC")
    abstract fun getAllTime_BLOCK_RECORD(
        fromTS: Long,
        toTS: Long
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND id = :id LIMIT 1")
    abstract fun getBLOCK_RECORD(id: Long): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) ORDER BY updateTime DESC")
    abstract fun getForDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Int
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType =:subType AND render_type = $RENDER_TYPE_Record ORDER BY updateTime DESC")
    abstract fun getAllForDisplay_BLOCK_RECORD(subType: Int): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND render_type = $RENDER_TYPE_Record ORDER BY updateTime DESC")
    abstract fun getAllForDisplay_BLOCK_RECORD(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) AND ((status & :falseStatus) = 0) ORDER BY updateTime DESC")
    abstract fun getForDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) AND ((status & :falseStatus) = 0) ORDER BY updateTime DESC")
    abstract fun getCursorForDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long
    ): Cursor

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND (subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) ) AND ((status & :falseStatus) = 0)  AND color = :color AND (title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%') ORDER BY `order` DESC")
    abstract fun getForKeywordDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long,
        keyword: String,
        color: Int
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND (subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) ) AND ((status & :falseStatus) = 0)  AND (title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%') ORDER BY `order` DESC")
    abstract fun getForKeywordDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long,
        keyword: String
    ): MutableList<RoomRecord>

    @Query("SELECT attachmentItems_attachmentItems FROM records")
    abstract fun getAllAttachments(): MutableList<AttachmentTail>

    @Query("SELECT id, subType FROM records WHERE type = $BLOCK_RECORD AND render_type = $RENDER_TYPE_Record AND ((status & $TASK_UNDERWAY) != 0) ")
    abstract fun getAllUnderWay(): MutableList<IdAndType>

    data class IdAndType(val id: Long, val subType: Int)

    @Update
    abstract fun updateReminder(reminder: Reminder): Int

    @Transaction
    open fun createAllAlarms(
        updateHabitRemindedTimes: Boolean,
        forceToUpdateRemindedTimes: Boolean,
        listener: OnCreateAllAlarms,
        notify: OnUpdateNotifyTime
    ) {
        val cursor: List<IdAndType> = getAllUnderWay()
        for ((id, type) in cursor) {
            if (type == REMINDER || type == GOAL) {
                val reminder: Reminder? = getReminderById(id)
                if (reminder == null || !reminder.isUnderWay) {
                    continue
                }
                val notifyTime = reminder.getNotifyTime()
                if (notifyTime < System.currentTimeMillis()) {
                    reminder.setState(Reminder.EXPIRED)
                    updateReminder(reminder)
                } else {
                    listener.sendAlarmBroadcast(id, notifyTime)
                }
            } else if (type == HABIT) {
                // 直接将习惯的提醒时间更新到最新时刻
                // 当用户收到提醒但未完成一次，备份应用，并在下一个周期恢复时，该习惯将不会为这一次添加记录"0"
                updateHabitToLatest(id, updateHabitRemindedTimes, false, notify)
            }
        }
        listener.end()
    }

    @Query("SELECT * FROM HabitReminder WHERE id = :uid LIMIT 1")
    abstract fun getHabitReminderById(uid: Long): HabitReminder?

    @Query("UPDATE Habit SET record = :record WHERE id =:id")
    abstract fun updateRecordOfHabit(id: Long, record: String)

    @Query("UPDATE Habit SET remindedTimes = :remindedTimes WHERE id =:id")
    abstract fun updateHabitRemindedTimes(id: Long, remindedTimes: Long)

    @Transaction
    open fun updateHabitToLatest(
        id: Long,
        updateRemindedTimes: Boolean,
        forceToUpdateRemindedTimes: Boolean,
        notify: OnUpdateNotifyTime
    ) {
        // This may happen if the universe boom, so we should consider it strictly.
        val habit = getHabit(id) ?: return

        val recordTimes = habit.record.length
        if (updateRemindedTimes && forceToUpdateRemindedTimes) {
            // This will prevent this habit from finishing in this T if it was notified but
            // user didn't finish it at once.
            updateHabitRemindedTimes(id, recordTimes.toLong())
        }

        var habitReminders = habit.habitReminders
        val hrIds: MutableList<Long> = ArrayList()
        for (habitReminder in habitReminders) {
            hrIds.add(habitReminder.id)
        }

        habit.initHabitReminders() // habitReminders have become latest.

        // You may see that Habit#initHabitReminders() will also set member firstTime again,
        // but this makes no change here because we don't update habit to database.

        // You may see that Habit#initHabitReminders() will also set member firstTime again,
        // but this makes no change here because we don't update habit to database.
        habitReminders = habit.habitReminders
        val hrIdsSize = hrIds.size
        if (hrIdsSize != habitReminders.size) {
            /**
             * it seems that this cannot happen but it did happen according to a user log.
             * I've tried to solve this problem by ensuring old habit is deleted successfully
             * when updating a habit in DetailActivity.
             * See [com.timecat.module.everything.activities.DetailActivity.setOrUpdateHabit]
             * and [.deleteHabit] for more details.
             * Maybe I didn't actually solve it. Let's see if there are more logs about that.
             */
            return
        }
        updateNotifyTime(hrIds, habitReminders, notify)

        // 将已经提前完成的habitReminder更新至新的周期里

        // 将已经提前完成的habitReminder更新至新的周期里
        val habitType = habit.type
        val habitRecordsThisT =
            habit.habitRecordsThisT
        for (habitRecord in habitRecordsThisT) {
            val hr: HabitReminder? = getHabitReminderById(habitRecord.habitReminderId)
            hr?.let {
                val now = System.currentTimeMillis()
                val gap = DateTimeUtil.calculateTimeGap(now, hr.notifyTime, habitType)
                if (gap == 0) {
                    updateHabitReminderToNext(hr.id, notify)
                }
            }
        }

        if (updateRemindedTimes && !forceToUpdateRemindedTimes) {
            val remindedTimes = habit.remindedTimes
            if (recordTimes < remindedTimes) {
                val minTime = habit.minHabitReminderTime
                val maxTime = habit.finalHabitReminder.notifyTime
                val maxLastTime: Long =
                    DateTimeUtil.getHabitReminderTime(habitType, maxTime, -1)
                val curTime = System.currentTimeMillis()
                if (curTime in (maxLastTime + 1) until minTime) {
                    if (DateTimeUtil.calculateTimeGap(maxLastTime, curTime, habitType) != 0) {
                        updateHabitRemindedTimes(id, recordTimes.toLong())
                    }
                    // else 用户还能“补”掉这一次未完成的情况，因此不更新remindedTimes
                } else {
                    updateHabitRemindedTimes(id, recordTimes.toLong())
                }
            } else if (recordTimes > remindedTimes) {
                updateHabitRemindedTimes(id, recordTimes.toLong())
            }
        }
    }

    interface OnCreateAllAlarms {
        fun sendAlarmBroadcast(id: Long, notifyTime: Long)
        fun end()
    }


    @Query("UPDATE HabitReminder SET notifyTime= :notifyTime WHERE id =:id")
    abstract fun updateNotifyTime(id: Long, notifyTime: Long)

    @Transaction
    open fun updateNotifyTime(
        ids: List<Long>,
        habitReminders: List<HabitReminder>,
        notify: OnUpdateNotifyTime
    ) {
        for (i in ids.indices) {
            val newTime: Long = habitReminders.get(i).notifyTime
            val id = ids[i]
            updateNotifyTime(id, newTime)
            notify.updateHabitReminder(id, newTime)
        }
    }


    /**
     * 这次的提醒已完成，计算出下一次的提醒时间
     *
     * @param hrId 习惯 id
     */
    @Transaction
    open fun updateHabitReminderToNext(
        hrId: Long,
        notify: OnUpdateNotifyTime
    ) {
        val habitReminder = getHabitReminderById(hrId)
        val habit = getHabitById(habitReminder!!.habitId)
        val type = habit!!.type
        var time = habitReminder.notifyTime

        // do one time before loop if user finish a habit for 1 time in advance
        time = DateTimeUtil.getHabitReminderTime(type, time, 1)
        while (time < System.currentTimeMillis()) {
            time = DateTimeUtil.getHabitReminderTime(type, time, 1)
        }
        notify.updateHabitReminder(hrId, time)
    }

    interface OnUpdateNotifyTime {
        fun updateHabitReminder(hrId: Long, notifyTime: Long)
    }

    @Query("SELECT `order` FROM records ORDER BY `order` ASC LIMIT 1")
    abstract fun getMinLocation(): Int

    @Query("SELECT `order` FROM records ORDER BY `order` DESC LIMIT 1")
    abstract fun getMaxLocation(): Int

    @Query("UPDATE records SET `order` = :location WHERE id = :id")
    abstract fun updateLocation(id: Long, location: Int)

    @Transaction
    open fun updateLocations(ids: List<Long>, locations: List<Int>) {
        for (i in ids.indices) {
            updateLocation(ids[i], locations[i])
        }
    }

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType=:subType")
    abstract fun getAllBySubType_BLOCK_RECORD(subType: Int): Cursor

    @Query("SELECT title, content, attachmentItems_attachmentItems FROM records WHERE type = $BLOCK_RECORD AND subType=$NOTE")
    abstract fun getAllTitleContentAttachments_BLOCK_RECORD(): List<TitleContentAttachments>

    data class TitleContentAttachments(
        val title: String,
        val content: String,
        val attachmentItems_attachmentItems: MutableList<AttachmentItem>
    )

    @Query("SELECT count(*) FROM records WHERE type = $BLOCK_RECORD AND subType=:subType AND (status & :status > 0)")
    abstract fun getCount_BLOCK_RECORD(subType: Int, status: Long): Int

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType=$HABIT AND (status & $TASK_UNDERWAY > 0)")
    abstract fun getAllHabitUnderway_BLOCK_RECORD(): Cursor
    //endregion

    //region BLOCK_BOOK, BLOCK_PAGE
    @Query("SELECT * FROM records WHERE type = $BLOCK_DIVIDER AND parent = :bookUuid ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_PAGE(bookUuid: String): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE (type = $BLOCK_DIVIDER OR type = $BLOCK_RECORD) AND parent = :bookUuid AND (status & $TASK_DELETE) = 0 ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAllForBook_BLOCK_PAGE(bookUuid: String): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_BOOK(): MutableList<RoomRecord>
    //endregion

    //region BLOCK_PLAN
    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND parent = :planUuid AND (status & $TASK_DELETE) = 0 ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAllForPlan_BLOCK_RECORD(planUuid: String): MutableList<RoomRecord>
    //endregion

    //region BLOCK_APP
    @Query("SELECT * FROM records WHERE type = $BLOCK_APP_WebApp ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_APP_WebApp(): MutableList<RoomRecord>
    //endregion

    //region BLOCK_COLLECTION
    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_COLLECTION(): MutableList<RoomRecord>
    //endregion

    //region
    //endregion

    //region Books
    @Transaction
    open fun update(list: List<RoomRecord>) {
        for (i in list) {
            update(i)
        }
    }

    @Transaction
    open fun markAsDeleted(list: List<RoomRecord>) {
        for (i in list) {
            i.setDeleted(true)
            update(i)
        }
    }

    @Transaction
    open fun markAsEx(list: List<RoomRecord>) {
        for (i in list) {
            i.setDeleted(true)
            update(i)
        }
    }

    @Transaction
    open fun reIndex(list: List<RoomRecord>) {
        list.forEachIndexed { index, roomRecord ->
            if (roomRecord.order != index) {
                roomRecord.order = index
                update(roomRecord)
            }
        }
    }

    @Transaction
    open fun rebuildOrder(list: List<RoomRecord>, from: Int, to: Int, offset: Int) {
        list.subList(from, to).forEach {
            it.order += offset
            update(it)
        }
    }
    //endregion

    //region Things
    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType = $HABIT ORDER BY `order` DESC")
    abstract fun getAllHabit(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType = $REMINDER ORDER BY `order` DESC")
    abstract fun getAllReminder(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType = $GOAL ORDER BY `order` DESC")
    abstract fun getAllGoal(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType = $NOTE ORDER BY `order` DESC")
    abstract fun getAllNote(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY `order` DESC")
    abstract fun getAllBook(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_DIVIDER ORDER BY `order` DESC")
    abstract fun getAllPage(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND (status & :status != 0)")
    abstract fun getByState(status: Long): MutableList<RoomRecord>?

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN (:subTypes) AND ((status & :status) != 0) AND ((status & :falseStatus) = 0) AND createTime < :before ORDER BY updateTime DESC")
    abstract fun getForDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long,
        before: Long
    ): MutableList<RoomRecord>

    @Transaction
    open fun getBetween_BLOCK_RECORD(date: DateTime): MutableList<RoomRecord> {
        val endTime = date.withMillisOfDay(0).plusDays(1).millis
        //1. can finish but no finish, after created
        return getForDisplay_BLOCK_RECORD(
            listOf(
//                NOTE,
                REMINDER,
                HABIT,
                GOAL
            ),
            TASK_UNDERWAY or TASK_CAN_FINISH,
            TASK_MODE_FALSE,
            endTime
        )
    }

    @Transaction
    open fun getAllHabitData(listener: OnHabitDataLoaded) {
        val all = getAllHabit()
        all.sortBy { it.updateTime }
        for (i in all) {
            getHabit(i.id)?.let {
                listener.onLoadHabit(i, it)
            }
        }
    }

    @Transaction
    open fun getAllReminderData(listener: OnReminderDataLoaded) {
        val all = getAllReminder()
        all.sortBy { it.updateTime }
        for (i in all) {
            getReminderById(i.id)?.let {
                listener.onLoadReminder(i, it)
            }
        }
    }

    @Transaction
    open fun getAllGoalData(listener: OnGoalDataLoaded) {
        val all = getAllGoal()
        all.sortBy { it.updateTime }
        for (i in all) {
            getReminderById(i.id)?.let {
                listener.onLoadGoal(i, it)
            }
        }
    }

    @Transaction
    open fun getAllNoteData(listener: OnNoteDataLoaded) {
        val all = getAllNote()
        all.sortBy { it.updateTime }
        for (i in all) {
            listener.onLoadNote(i)
        }
    }

    interface OnHabitDataLoaded {
        fun onLoadHabit(record: RoomRecord, habit: Habit)
    }

    interface OnReminderDataLoaded {
        fun onLoadReminder(record: RoomRecord, reminder: Reminder)
    }

    interface OnNoteDataLoaded {
        fun onLoadNote(record: RoomRecord)
    }

    interface OnGoalDataLoaded {
        fun onLoadGoal(record: RoomRecord, reminder: Reminder)
    }
    //endregion

    //region rebuildHabitOrder
    @Transaction
    open fun rebuildHabitOrder() {
        val list = getAllHabit()
        var pos = 0
        for (r in list) {
            r.order = pos++
            replace(r)
        }
    }
    //endregion

    //region removeAll
    @Query("DELETE FROM records WHERE type = $BLOCK_RECORD AND subType = $HABIT")
    abstract fun removeAllHabit()

    @Query("DELETE FROM habitrecord")
    abstract fun removeAllHabitRecord()

    @Transaction
    open fun removeAll() {
        removeAllHabit()
        removeAllHabitRecord()
    }
    //endregion

    //region Things
    @Query("SELECT * FROM Habit WHERE id = :uid LIMIT 1")
    abstract fun getHabitById(uid: Long): Habit?

    @Query("SELECT * FROM Reminder WHERE id = :uid LIMIT 1")
    abstract fun getReminderById(uid: Long): Reminder?

    @Transaction
    open fun getRecordData(date: DateTime, listener: OnRecordDataLoaded) {
        val all = getBetween_BLOCK_RECORD(date)
        all.sortBy { it.subType }
        for (i in all) {
            when (i.subType) {
                NOTE -> listener.onLoadNote(i)
                REMINDER -> getReminderById(i.id)?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> getHabit(i.id)?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> getReminderById(i.id)?.let {
                    listener.onLoadGoal(i, it)
                }
            }
        }
    }

    @Transaction
    open fun getAllRecordData(listener: OnRecordDataLoaded) {
        val all = getAll_BLOCK_RECORD()
        all.sortBy { it.subType }
        for (i in all) {
            when (i.subType) {
                NOTE -> listener.onLoadNote(i)
                REMINDER -> getReminderById(i.id)?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> getHabit(i.id)?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> getReminderById(i.id)?.let {
                    listener.onLoadGoal(i, it)
                }
            }
        }
    }

    @Transaction
    open fun getAllTimeRecordData(
        fromTS: Long,
        toTS: Long,
        listener: OnTimeRecordDataLoaded
    ) {
        val all = getAllTime_BLOCK_RECORD(fromTS * 1000, toTS * 1000)
        all.sortBy { it.subType }
        for (i in all) {
            when (i.subType) {
                REMINDER -> getReminderById(i.id)?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> getHabit(i.id)?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> getReminderById(i.id)?.let {
                    listener.onLoadGoal(i, it)
                }
            }
        }
    }

    @Transaction
    open fun getAllTimeRecordData(listener: OnTimeRecordDataLoaded) {
        val all = getAllTime_BLOCK_RECORD()
        all.sortBy { it.subType }
        for (i in all) {
            when (i.subType) {
                REMINDER -> getReminderById(i.id)?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> getHabit(i.id)?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> getReminderById(i.id)?.let {
                    listener.onLoadGoal(i, it)
                }
            }
        }
    }

    @Transaction
    open fun getAllTimeRecordData(all: List<RoomRecord>, listener: OnTimeRecordDataLoaded) {
        for (i in all) {
            when (i.subType) {
                REMINDER -> getReminderById(i.id)?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> getHabit(i.id)?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> getReminderById(i.id)?.let {
                    listener.onLoadGoal(i, it)
                }
            }
        }
    }

    @Transaction
    open fun getAllRecordData(all: List<RoomRecord>, listener: OnRecordDataLoaded) {
        for (i in all) {
            when (i.subType) {
                NOTE -> listener.onLoadNote(i)
                REMINDER -> getReminderById(i.id)?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> getHabit(i.id)?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> getReminderById(i.id)?.let {
                    listener.onLoadGoal(i, it)
                }
            }
        }
    }

    @Transaction
    open fun getAllData(all: List<RoomRecord>, listener: OnDataLoaded) {
        for (i in all) {
            when (i.type) {
                BLOCK_RECORD -> {
                    when (i.subType) {
                        NOTE -> listener.onLoadNote(i)
                        REMINDER -> getReminderById(i.id)?.let {
                            listener.onLoadReminder(i, it)
                        }
                        HABIT -> getHabit(i.id)?.let {
                            listener.onLoadHabit(i, it)
                        }
                        GOAL -> getReminderById(i.id)?.let {
                            listener.onLoadGoal(i, it)
                        }
                    }
                }
                BLOCK_CONVERSATION -> {
                    listener.onLoadConversation(i)
                }
                BLOCK_CONTAINER -> {
                    listener.onLoadContainer(i)
                }
            }

        }
    }

    interface OnDataLoaded : OnConversationLoaded,
        OnHabitDataLoaded, OnReminderDataLoaded, OnNoteDataLoaded, OnGoalDataLoaded, OnContainerLoaded

    interface OnConversationLoaded {
        fun onLoadConversation(record: RoomRecord)
    }

    interface OnContainerLoaded {
        fun onLoadContainer(record: RoomRecord)
    }

    @Query("SELECT * FROM HabitReminder WHERE habitId = :habitId")
    abstract fun getHabitRemindersByHabit(habitId: Long): List<HabitReminder>?

    @Transaction
    open fun getHabit(id: Long): Habit? {
        val habit: Habit = getHabitById(id) ?: return null
        habit.habitReminders = getHabitRemindersByHabit(id)
        habit.habitRecords = getHabitRecordsByHabit(id)
        return habit
    }

    @Transaction
    open fun getHabit(uuid: String, onHabitDataLoaded: OnHabitDataLoaded) {
        val record = getByUuid(uuid) ?: return
        if (record.type == BLOCK_RECORD && record.subType == HABIT) {
            val habit: Habit = getHabitById(record.id) ?: return
            habit.habitReminders = getHabitRemindersByHabit(record.id)
            habit.habitRecords = getHabitRecordsByHabit(record.id)
            onHabitDataLoaded.onLoadHabit(record, habit)
        }
    }

    @Query("SELECT * FROM HabitRecord WHERE habitId = :habitId AND (type = ${HabitRecord.TYPE_FINISHED} or type = ${HabitRecord.TYPE_FAKE_FINISHED}) ORDER BY recordTime ASC")
    abstract fun getHabitRecordsByHabit(habitId: Long): List<HabitRecord>

    interface OnTimeRecordDataLoaded
        : OnHabitDataLoaded, OnReminderDataLoaded, OnGoalDataLoaded

    interface OnRecordDataLoaded
        : OnHabitDataLoaded, OnReminderDataLoaded, OnNoteDataLoaded, OnGoalDataLoaded
    //endregion

    //region SimpleRecord
    @Query("SELECT uuid, type, subType, title FROM records WHERE uuid = :uuid LIMIT 1")
    abstract fun getSimpleRecordByUuid(uuid: String): SimpleRecord?

    data class SimpleRecord(val uuid: String, val type: Int, val subType: Int, val title: String)
    //endregion

    //region
    //endregion

    companion object {
        const val LIST_SIZE = 512
    }
}