package com.timecat.data.room.habit

import androidx.room.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-17
 * @description null
 * @usage null
 */

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widget: Habit): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(vararg widget: Habit)

    @Delete
    fun delete(tag: Habit)

    @Query("SELECT * FROM Habit WHERE id = :uid LIMIT 1")
    fun getByID(uid: Long): Habit?

    @Query("SELECT detail FROM Habit WHERE id = :uid LIMIT 1")
    fun getDetailByID(uid: Long): String?

    @Query("SELECT * FROM Habit")
    fun getAll(): List<Habit>

    @Query("DELETE FROM Habit WHERE id = :id")
    fun delete(id: Long)

    @Query("UPDATE Habit SET record = :record WHERE id =:id")
    fun updateRecordOfHabit(id: Long, record: String)

    @Query("UPDATE Habit SET remindedTimes = :remindedTimes WHERE id =:id")
    fun updateHabitRemindedTimes(id: Long, remindedTimes: Long)

    @Query("UPDATE Habit SET intervalInfo = :intervalInfo WHERE id =:id")
    fun updateHabitIntervalInfo(id: Long, intervalInfo: String)

    @Transaction
    fun addHabitIntervalInfo(id: Long, intervalInfoToAdd: String) {
        getByID(id)?.let {
            updateHabitIntervalInfo(id, it.intervalInfo + intervalInfoToAdd)
        }
    }

    @Transaction
    fun removeLastHabitIntervalInfo(id: Long) {
        getByID(id)?.let {
            var interval = it.intervalInfo
            interval = interval.substring(
                0,
                interval.lastIndexOf(if (interval.endsWith(";")) "," else ";") + 1
            )
            updateHabitIntervalInfo(id, interval)
        }
    }

    @Query("UPDATE Habit SET type = :type , remindedTimes = :remindedTimes , detail=:detail , record = :record , intervalInfo = :intervalInfo WHERE id =:id")
    fun updateHabit(
        id: Long,
        type: Int,
        remindedTimes: Int,
        detail: String,
        record: String,
        intervalInfo: String
    )

    @Query("SELECT id FROM HabitReminder ORDER BY id DESC")
    fun getHabitReminderNewestId(): Long?

    //region transaction 1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabitReminder(reminder: HabitReminder): Long

    @Transaction
    fun createHabitReminder(habitReminders: List<HabitReminder>, listener: OnTransactionFinish) {
        for (habitReminder in habitReminders) {
            val mHabitReminderId = listener.getHabitReminderId()
            val notifyTime = habitReminder.notifyTime
            insertHabitReminder(HabitReminder(mHabitReminderId, habitReminder.habitId, notifyTime))
            listener.setHabitReminderAlarm(mHabitReminderId, notifyTime)
        }
    }

    @Transaction
    fun createHabit(habit: Habit, listener: OnTransactionFinish) {
        insert(habit)
        for (habitReminder in habit.habitReminders) {
            val mHabitReminderId = listener.getHabitReminderId()
            val notifyTime = habitReminder.notifyTime
            insertHabitReminder(HabitReminder(mHabitReminderId, habitReminder.habitId, notifyTime))
            listener.setHabitReminderAlarm(mHabitReminderId, notifyTime)
        }
    }

    interface OnTransactionFinish {
        fun getHabitReminderId(): Long
        fun setHabitReminderAlarm(id: Long, notifyTime: Long)
    }
    //endregion

    //region transaction 2
    @Transaction
    fun deleteHabit(id: Long, listener: OnTransactionDeleteHabit) {
        delete(id)
        getHabitRemindersByHabit(id)?.let {
            listener.deleteHabitReminderAlarm(it)
        }
        deleteHabitReminderByHabit(id)
        deleteHabitRecordByHabit(id)
        listener.initDefaultHabitReminderId()
        getNewestHabitReminderId()?.let {
            listener.setNewestHabitReminderId(it)
        }
        getNewestHabitRecordId()?.let {
            listener.setNewestHabitRecordId(it)
        }
    }

    @Query("SELECT * FROM HabitReminder WHERE habitId = :habitId")
    fun getHabitRemindersByHabit(habitId: Long): List<HabitReminder>?

    @Query("DELETE FROM HabitReminder WHERE habitId = :habitId")
    fun deleteHabitReminderByHabit(habitId: Long)

    @Query("DELETE FROM HabitRecord WHERE habitId = :habitId")
    fun deleteHabitRecordByHabit(habitId: Long)

    @Query("SELECT id FROM HabitReminder ORDER BY id DESC LIMIT 1")
    fun getNewestHabitReminderId(): Long?

    @Query("SELECT id FROM HabitRecord ORDER BY id DESC LIMIT 1")
    fun getNewestHabitRecordId(): Long?

    interface OnTransactionDeleteHabitReminder {
        fun deleteHabitReminderAlarm(habitReminders: List<HabitReminder>)
    }

    interface OnTransactionDeleteHabit : OnTransactionDeleteHabitReminder {
        fun initDefaultHabitReminderId()
        fun setNewestHabitReminderId(id: Long)
        fun setNewestHabitRecordId(id: Long)
    }

    //endregion
    //region transaction 3
    @Transaction
    fun recreateHabit(id: Long, habit: Habit, listener: OnTransactionRecreateHabit) {
        // 1. delete Habit
        delete(id)
        // 2. delete HabitReminders
        getHabitRemindersByHabit(id)?.let {
            listener.deleteHabitReminderAlarm(it)
        }
        deleteHabitReminderByHabit(id)
        // 3. delete HabitRecords
        deleteHabitRecordByHabit(id)
        // 4. refresh
        listener.initDefaultHabitReminderId()
        getNewestHabitReminderId()?.let {
            listener.setNewestHabitReminderId(it)
        }
        getNewestHabitRecordId()?.let {
            listener.setNewestHabitRecordId(it)
        }

        // 5. create Habit
        insert(habit)
        // 6. create HabitReminders
        for (habitReminder in habit.habitReminders) {
            val mHabitReminderId = listener.getHabitReminderId()
            val notifyTime = habitReminder.notifyTime
            insertHabitReminder(HabitReminder(mHabitReminderId, habitReminder.habitId, notifyTime))
            listener.setHabitReminderAlarm(mHabitReminderId, notifyTime)
        }
    }

    interface OnTransactionRecreateHabit : OnTransactionDeleteHabit, OnTransactionFinish

    //endregion
    //region transaction 4
    @Transaction
    fun recreateHabitReminders(habit: Habit, listener: OnTransactionRecreateHabitReminders) {
        val id = habit.id
        // 1. update Habit
        updateHabit(
            habit.id,
            habit.type,
            habit.remindedTimes,
            habit.detail,
            habit.record,
            habit.intervalInfo
        );

        // 2. delete HabitReminders
        getHabitRemindersByHabit(id)?.let {
            listener.deleteHabitReminderAlarm(it)
        }
        deleteHabitReminderByHabit(id)

        // 3. reInit HabitReminders
        habit.initHabitReminders()

        // 4. create HabitReminders
        for (habitReminder in habit.habitReminders) {
            val mHabitReminderId = listener.getHabitReminderId()
            val notifyTime = habitReminder.notifyTime
            insertHabitReminder(HabitReminder(mHabitReminderId, habitReminder.habitId, notifyTime))
            listener.setHabitReminderAlarm(mHabitReminderId, notifyTime)
        }
    }

    interface OnTransactionRecreateHabitReminders
        : OnTransactionFinish, OnTransactionDeleteHabitReminder

    //endregion
    //region transaction 5
    @Transaction
    fun getHabit(id: Long): Habit? {
        val habit: Habit = getByID(id) ?: return null
        habit.habitReminders = getHabitRemindersByHabit(id)
        habit.habitRecords = getHabitRecordsByHabit(id)
        return habit
    }

    @Query("SELECT * FROM HabitRecord WHERE habitId = :habitId AND (type = ${HabitRecord.TYPE_FINISHED} or type = ${HabitRecord.TYPE_FAKE_FINISHED}) ORDER BY recordTime ASC")
    fun getHabitRecordsByHabit(habitId: Long): List<HabitRecord>
    //endregion


}
