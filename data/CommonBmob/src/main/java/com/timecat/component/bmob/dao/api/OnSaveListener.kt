package com.timecat.component.bmob.dao.api

import cn.bmob.v3.exception.BmobException

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
interface OnSaveListener<T> {
    fun done(obj: T?, e: BmobException?)
}