package com.timecat.data.manager.repo

import com.timecat.data.room.IRoomDao
import com.timecat.identity.data.one.User
import io.reactivex.Observable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-25
 * @description null
 * @usage null
 */
class UserRepository(val userDao: IRoomDao<User>) {

    fun getUsers(): Observable<List<User>> {
        return Observable.concatArray(
            userDao.getFromRoom()
        )
    }
}