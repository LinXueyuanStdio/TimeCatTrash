package com.timecat.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.timecat.data.room.doing.DoingRecord
import com.timecat.data.room.habit.Habit
import com.timecat.data.room.habit.HabitRecord
import com.timecat.data.room.habit.HabitReminder
import com.timecat.data.room.habit.RoomRepetition
import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.reminder.Reminder
import com.timecat.data.room.tag.Tag

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/6
 * @description null
 * @usage null
 */
@Dao
interface TransDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceRoomRecord(data: Collection<RoomRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceTag(data: Collection<Tag>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceHabit(data: Collection<Habit>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceHabitRecord(data: Collection<HabitRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceHabitReminder(data: Collection<HabitReminder>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceDoingRecord(data: Collection<DoingRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceReminder(data: Collection<Reminder>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceRoomRepetition(data: Collection<RoomRepetition>)

    @Transaction
    fun importFromFile(
        notes: Collection<RoomRecord>,
        tags: Collection<Tag>,
        habit: Collection<Habit>,
        habitRecord: Collection<HabitRecord>,
        habitReminder: Collection<HabitReminder>,
        doingRecord: Collection<DoingRecord>,
        reminder: Collection<Reminder>,
        roomRepetition: Collection<RoomRepetition>
    ) {
        replaceRoomRecord(notes)
        replaceTag(tags)
        replaceHabit(habit)
        replaceHabitRecord(habitRecord)
        replaceHabitReminder(habitReminder)
        replaceDoingRecord(doingRecord)
        replaceReminder(reminder)
        replaceRoomRepetition(roomRepetition)
    }
}