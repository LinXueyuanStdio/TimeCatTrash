package com.timecat.data.room.record

import androidx.room.*
import com.timecat.identity.data.base.RENDER_TYPE_MARKDOWN
import com.timecat.identity.data.base.TASK_DELETE
import com.timecat.identity.data.base.TASK_PIN
import com.timecat.identity.data.base.TASK_PRIVATE
import com.timecat.identity.data.block.type.BLOCK_RECORD

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/11
 * @description null
 * @usage null
 */
@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: RoomRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(vararg note: RoomRecord)

    @Delete
    fun delete(note: RoomRecord?)

    @Query("SELECT count(*) FROM view_markdown")
    fun getCount(): Int

    @Query("SELECT * FROM view_markdown ORDER BY (status & $TASK_PIN) DESC, createTime DESC")
    fun getAll(): List<RoomRecord>

//    @Query("SELECT * FROM view_markdown WHERE state in (:states) ORDER BY (status & $TASK_PIN) DESC, createTime DESC")
//    fun getByNoteState(states: Array<String>): List<RoomRecord>

    @Query("SELECT * FROM view_markdown WHERE (status & $TASK_DELETE <> 0) AND updateTime < :timestamp")
    fun getOldTrashedNotes(timestamp: Long): List<RoomRecord>

    @Query("SELECT * FROM view_markdown WHERE (status & $TASK_PRIVATE <> 0) ORDER BY (status & $TASK_PIN) DESC, createTime DESC")
    fun getNoteByLocked(): List<RoomRecord>

    @Query("SELECT * FROM view_markdown WHERE tags LIKE :uuidRegex ORDER BY (status & $TASK_PIN) DESC, createTime DESC")
    fun getNoteByTag(uuidRegex: String?): List<RoomRecord>

    @Query("SELECT COUNT(*) FROM view_markdown WHERE tags LIKE :uuidRegex ORDER BY (status & $TASK_PIN) DESC, createTime DESC")
    fun getNoteCountByTag(uuidRegex: String): Int

    @Query("SELECT * FROM view_markdown WHERE id = :uid LIMIT 1")
    fun getByID(uid: Int): RoomRecord?

    @Query("SELECT * FROM view_markdown WHERE uuid = :uuid LIMIT 1")
    fun getByUUID(uuid: String): RoomRecord?

    @Query("SELECT uuid FROM view_markdown")
    fun getAllUUIDs(): List<String>

    @Query("SELECT updateTime FROM view_markdown ORDER BY updateTime DESC LIMIT 1")
    fun getLastTimestamp(): Long

    @Query("UPDATE records SET status=(status & ~$TASK_PRIVATE) WHERE type = $BLOCK_RECORD AND render_type = $RENDER_TYPE_MARKDOWN AND (status & $TASK_PRIVATE != 0)")
    fun unlockAll()
}