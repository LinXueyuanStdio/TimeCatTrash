package com.timecat.self.data.service

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
interface OnSaveListener<T> {
    fun success(data: T)
    fun error(e: DataError)
}