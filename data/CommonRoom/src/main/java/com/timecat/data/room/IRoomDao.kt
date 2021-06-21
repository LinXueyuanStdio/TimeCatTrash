package com.timecat.data.room

import io.reactivex.Observable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-25
 * @description null
 * @usage null
 */
interface IRoomDao<T> {
    fun getFromRoom(): Observable<List<T>>
    fun getFromRoom(objectId: String): Observable<T>
    fun saveToRoom(data: T)
}