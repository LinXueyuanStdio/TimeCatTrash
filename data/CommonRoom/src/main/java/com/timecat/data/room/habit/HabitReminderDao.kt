package com.timecat.data.room.habit

import androidx.room.*

@Dao
interface HabitReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widget: HabitReminder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(vararg widget: HabitReminder)

    @Delete
    fun delete(tag: HabitReminder?)

    @Query("SELECT * FROM HabitReminder WHERE id = :uid LIMIT 1")
    fun getByID(uid: Long): HabitReminder?

    @Query("SELECT id FROM HabitReminder ORDER BY id DESC")
    fun getNewestId(): Long?

    @Query("SELECT * FROM HabitReminder WHERE habitId = :habitId")
    fun getByHabit(habitId: Long): List<HabitReminder>

    @Query("SELECT * FROM HabitReminder")
    fun getAll(): List<HabitReminder>

    @Query("DELETE FROM HabitReminder WHERE id = :recordId")
    fun delete(recordId: Long)

    @Query("DELETE FROM HabitReminder WHERE habitId = :habitId")
    fun deleteByHabit(habitId: Long)

    @Query("UPDATE HabitReminder SET notifyTime= :notifyTime WHERE id =:id")
    fun updateNotifyTime(id: Long, notifyTime: Long)

    @Transaction
    fun updateNotifyTime(
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

    interface OnUpdateNotifyTime {
        fun updateHabitReminder(hrId: Long, notifyTime: Long)
    }
}