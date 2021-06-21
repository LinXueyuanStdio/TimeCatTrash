package com.timecat.self.data.service

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-25
 * @description null
 * @usage null
 */
interface OnFindListener<T> {
    fun success(data: List<T>)
    fun error(e: DataError)
}