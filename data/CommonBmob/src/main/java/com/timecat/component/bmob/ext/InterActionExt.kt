package com.timecat.component.bmob.ext

import cn.bmob.v3.datatype.BmobDate
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Block
import com.timecat.component.bmob.data.common.InterAction
import com.timecat.identity.data.action.INTERACTION_Auth_Identity
import com.timecat.identity.data.action.INTERACTION_Auth_Permission
import com.timecat.identity.data.action.INTERACTION_Auth_Role
import com.timecat.identity.data.action.InterActionType
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/7
 * @description null
 * @usage null
 */
fun interaction(
    author: _User,
    source: Block,
    target: _User,
    @InterActionType type: Int
) = InterAction(author, source, target, type, BmobDate(Date()), BmobDate(Date()), "", 0)

infix fun _User.auth_Identity(p: Pair<Block, _User>): InterAction {
    return interaction(this, p.first, p.second, INTERACTION_Auth_Identity)
}

infix fun _User.auth_Role(p: Pair<Block, _User>): InterAction {
    return interaction(this, p.first, p.second, INTERACTION_Auth_Role)
}

infix fun _User.auth_Permission(p: Pair<Block, _User>): InterAction {
    return interaction(this, p.first, p.second, INTERACTION_Auth_Permission)
}