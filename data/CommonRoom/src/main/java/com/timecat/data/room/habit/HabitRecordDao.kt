package com.timecat.data.room.habit

import androidx.room.*

@Dao
interface HabitRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widget: HabitRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(vararg widget: HabitRecord)

    @Delete
    fun delete(tag: HabitRecord)

    @Query("SELECT * FROM HabitRecord WHERE id = :uid LIMIT 1")
    fun getByID(uid: Long): HabitRecord?

    @Query("SELECT id FROM HabitRecord ORDER BY id DESC")
    fun getNewestId(): Long?

    @Query("SELECT * FROM HabitRecord WHERE habitId = :habitId AND (type = ${HabitRecord.TYPE_FINISHED} or type = ${HabitRecord.TYPE_FAKE_FINISHED}) ORDER BY recordTime ASC")
    fun getByHabit(habitId: Long): List<HabitRecord>

    @Query("SELECT * FROM HabitRecord WHERE habitId = :habitId AND (type = ${HabitRecord.TYPE_FINISHED} or type = ${HabitRecord.TYPE_FAKE_FINISHED}) ORDER BY recordTime DESC LIMIT :limited")
    fun getByHabitLimited(habitId: Long, limited:Int): List<HabitRecord>

    @Query("SELECT * FROM HabitRecord")
    fun getAll(): List<HabitRecord>

    @Query("DELETE FROM HabitRecord WHERE id = :uid")
    fun delete(uid: Long)

    @Query("DELETE FROM HabitRecord WHERE habitId = :habitId")
    fun deleteByHabit(habitId: Long)


    @Update
    fun update(reminder: HabitRecord): Int
}