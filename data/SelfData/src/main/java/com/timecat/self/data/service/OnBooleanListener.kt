package com.timecat.self.data.service


/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-09
 * @description null
 * @usage null
 */
interface OnBooleanListener {
    fun success(yes: Boolean)
    fun error(e: DataError)
}