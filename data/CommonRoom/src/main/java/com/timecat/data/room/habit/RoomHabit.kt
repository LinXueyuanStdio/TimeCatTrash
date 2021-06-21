package com.timecat.data.room.habit

import android.content.Context
import com.timecat.data.room.TimeCatRoomDatabase
import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.reminder.Reminder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-05
 * @description null
 * @usage null
 */
var RoomRecord.position: Int
    get() = extension.getInteger("position") ?: 0
    set(value) {
        extension.put("position", value)
    }

fun RoomRecord.getReminder(context: Context): Reminder? {
    return TimeCatRoomDatabase.forFile(context).reminderDao().getByID(id)
}

fun RoomRecord.getHabit(context: Context): Habit? {
    return TimeCatRoomDatabase.forFile(context).habitDao().getHabit(id)
}
