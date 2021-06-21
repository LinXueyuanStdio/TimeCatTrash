package com.timecat.data.room.habit

import androidx.room.*
import com.timecat.data.room.BaseDao

@Dao
abstract class RepetitionDao : BaseDao<RoomRepetition>{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOrReplace(vararg widget: RoomRepetition)

    @Insert
    abstract fun newRep(widget: RoomRepetition): Long

    @Query("SELECT * FROM RoomRepetition WHERE id = :uid LIMIT 1")
    abstract fun getByID(uid: Int): RoomRepetition?

    @Query("SELECT * FROM RoomRepetition WHERE recordId = :recordId")
    abstract fun getByNote(recordId: Long): List<RoomRepetition>

    @Query("SELECT * FROM RoomRepetition")
    abstract fun getAll(): List<RoomRepetition>

    @Query("DELETE FROM RoomRepetition WHERE recordId = :recordId AND timestamp = :timestamp")
    abstract fun remove(recordId: Long, timestamp: Long)

    @Query("SELECT count(*) FROM RoomRepetition WHERE recordId = :recordId")
    abstract fun count(recordId: Long): Int

    @Query("DELETE FROM RoomRepetition WHERE recordId = :recordId")
    abstract fun delete(recordId: Long)
}