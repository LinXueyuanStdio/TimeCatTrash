package com.timecat.data.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-29
 * @description null
 * @usage null
 */
interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param entity the entity to be inserted
     */
    @Insert
    fun insert(entity: T): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param entities entities to be inserted
     */
    @Insert
    fun insert(vararg entities: T)

    /**
     * Insert an array of objects in the database.
     *
     * @param entities entities to be inserted
     */
    @Insert
    fun insert(entities: Collection<T>)

    /**
     * Replace an array of objects in the database.
     *
     * @param entities entities to be replaced
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(vararg entities: T)

    /**
     * Replace an array of objects in the database.
     *
     * @param entities entities to be replaced
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(entities: Collection<T>)

    /**
     * Update an object from the database.
     *
     * @param entity the entity to be updated
     */
    @Update
    fun update(entity: T): Int

    /**
     * Delete an object from the database
     *
     * @param entity the entity to be deleted
     */
    @Delete
    fun delete(entity: T): Int
}