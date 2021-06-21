package com.timecat.component.bmob.ext

import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Action
import com.timecat.component.bmob.data.common.Block
import com.timecat.identity.data.action.ACTION_FOCUS
import com.timecat.identity.data.action.ACTION_RECOMMEND

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
fun action(user: _User, block: Block, type: Int) = Action(user, block, type, "", 0)

infix fun _User.follow(target: Block): Action = action(this, target, ACTION_FOCUS)
infix fun _User.recommend(target: Block): Action = action(this, target, ACTION_RECOMMEND)
