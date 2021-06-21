package com.timecat.self.data.base

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description null
 * @usage null
 */
/**
 * 用 16 进制管理状态
 */
interface IStatus {
    fun addStatus(status: Long)
    fun removeStatus(status: Long)
    fun isStatusEnabled(status: Long): Boolean
    fun updateStatus(s: Long, yes: Boolean) {
        if (yes) addStatus(s) else removeStatus(s)
    }
    fun statusDescription(): String
}