package com.timecat.data.room.record

import androidx.room.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/11
 * @description null
 * @usage null
 */
@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolder(note: RoomRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolders(vararg note: RoomRecord)

    @Delete
    fun delete(note: RoomRecord?)

    @Query("SELECT count(*) FROM view_page")
    fun getCount(): Int

    @Query("SELECT * FROM view_page ORDER BY createTime DESC")
    fun getAll(): List<RoomRecord>

    @Query("SELECT * FROM view_page WHERE id = :uid LIMIT 1")
    fun getByID(uid: Int): RoomRecord?

    @Query("SELECT * FROM view_page WHERE uuid = :uuid LIMIT 1")
    fun getByUUID(uuid: String): RoomRecord?

    @Query("SELECT uuid FROM view_page")
    fun getAllUUIDs(): List<String>
}