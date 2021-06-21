package com.timecat.component.bmob.ext.net

import cn.bmob.v3.BmobQuery
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/8
 * @description null
 * @usage null
 */
fun allUsers(query: String? = null) = BmobQuery<_User>().apply {
    order("-createdAt")
}

fun oneUserOf(id: String) = BmobQuery<_User>().apply {
    addWhereEqualTo("objectId", id)
    order("-createdAt")
    setLimit(1)
}

fun blocksOf(user: _User) = BmobQuery<Block>().apply {
    order("-createdAt")
    addWhereEqualTo("user", user)
}