package com.timecat.data.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getUsers(): Single<List<RoomUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(roomUser: RoomUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(roomUsers: List<RoomUser>)
}