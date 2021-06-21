package com.timecat.component.bmob.data.common

import cn.bmob.v3.BmobObject
import com.timecat.component.bmob.data._User
import com.timecat.identity.data.user_user.User2UserType
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description 人与人之间的关系
 * @usage null
 */
data class User2User(
    var author: _User,
    var target: _User? = null,
    @User2UserType var type: Int = 0,
    var structure: String = "",
    var status: Long = 0
) : BmobObject("User2User"), Serializable {
    init {
        tableName = "User2User"
    }
}
