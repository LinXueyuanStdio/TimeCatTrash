package com.timecat.component.bmob.ext.net

import cn.bmob.v3.BmobQuery
import com.timecat.component.bmob.data._User
import com.timecat.component.bmob.data.common.Action
import com.timecat.component.bmob.data.common.Block
import com.timecat.identity.data.action.ACTION_DING
import com.timecat.identity.data.action.ACTION_FOCUS
import com.timecat.identity.data.action.ACTION_LIKE
import com.timecat.identity.data.action.ACTION_RECOMMEND

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
//TODO 没有加include，要哪个字段自己加
fun globalActionByType(
    type: Int,
    block: Block? = null
): BmobQuery<Action> {
    val q = BmobQuery<Action>()
    q.order("-createdAt")
    q.addWhereEqualTo("type", type)
    if (block != null) q.addWhereEqualTo("block", block)
    return q
}
fun globalRecommendBlock(block: Block? = null) = globalActionByType(ACTION_RECOMMEND, block)

fun _User.allActionByType(
    type: Int,
    block: Block? = null
): BmobQuery<Action> {
    val q = BmobQuery<Action>()
    q.addWhereEqualTo("user", this)
    q.order("-createdAt")
    q.addWhereEqualTo("type", type)
    if (block != null) q.addWhereEqualTo("block", block)
    return q
}

fun _User.allFollowBlock(block: Block? = null) = allActionByType(ACTION_FOCUS, block)
fun _User.allRecommendBlock(block: Block? = null) = allActionByType(ACTION_RECOMMEND, block)
fun _User.allLikeBlock(block: Block? = null) = allActionByType(ACTION_LIKE, block)
fun _User.allDingBlock(block: Block? = null) = allActionByType(ACTION_DING, block)

fun Block.allActionByType(
    type: Int,
    user: _User? = null
): BmobQuery<Action> {
    val q = BmobQuery<Action>()
    q.addWhereEqualTo("block", this)
    q.order("-createdAt")
    q.addWhereEqualTo("type", type)
    if (user != null) q.addWhereEqualTo("user", user)
    return q
}


fun Block.allFans(user: _User? = null) = allActionByType(ACTION_FOCUS, user)
fun Block.allLikes(user: _User? = null) = allActionByType(ACTION_LIKE, user)
fun Block.allDings(user: _User? = null) = allActionByType(ACTION_DING, user)


fun _User.allAction(): BmobQuery<Action> {
    val q = BmobQuery<Action>()
    q.addWhereEqualTo("user", this)
    q.order("-createdAt")
    return q
}